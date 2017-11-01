package hashing;

import java.util.HashMap;

/**
 * Tries to find collisions in the hash functions in the HashFunction class
 * @author  Caroline Zeng
 * @version 0.2.0
 */

public class Birthday {
    static final int CHAR_POSSIBILITIES = 93;
    static final int BIT_POSSIBILITIES = 2;
    /**
     * Finds the probability of the same String being chosen when choosing strings at random
     * @param n number chosen
     * @param H total possibilities
     * @return  probability of repetition
     */
    public static double probabilityOfRepetition(int n, int H)
    {
        double approx1 = -1 * n * (n-1) / (2 * H);
        double approx2 = Math.pow(Math.E, approx1);
        double expapprox = 1 - approx2;
        return expapprox;
    }

    /**
     * Finds smallest number of values chosen to have probability p to get a collision with total number of
     * possibilities H
     * @param p probability
     * @param H total possibilities
     * @return  number of values that need to be chose
     */
    public static double numChosenforProbability(double p, int H)
    {
        return Math.sqrt(2 * H * Math.log(1 / (1 - p)));
    }

    /**
     * Finds the expected number of values that should be tested before a collision
     * @param H total possible outcomes
     * @return  expected number of values that should be tested
     */
    public static int valuesBeforeCollision(int H)
    {
        return (int)(Math.sqrt(Math.PI * H / 2));
    }

    /**
     * Finds the number of possible outcomes
     * @param possibilities number of possibilities for each place
     * @param bits          number of places
     * @return              number of possibilities
     */
    public static int getH(int possibilities, int bits) {return (int) Math.pow(possibilities, bits);}

    /**
     * Birthday attack on String hashcode function
     * @param bits  length of String
     * @return      the hash, the 2 Strings with the same hash, and the number of tries taken
     */
    public static String[] testBirthday(int bits)
    {
        HashMap<Integer, String> hashes = new HashMap<>();
        String[] collisionInfo = new String[4];
        int counter = 0;
        boolean collision = false;
        while (!collision)
        {
            counter++;
            String in  = HashFunction.generateSalt(bits);
            int hash = in.hashCode();
            if (hashes.get(hash) == null)
            {
                hashes.put(hash, in);
            } else if (!hashes.get(hash).equals(in))
            {
                collision     = true;
                collisionInfo = new String[] {hash + "", in, hashes.get(hash), "" + counter};
            }
            System.out.println(counter + "\t\t" + in + "\t" + hash);
        }
        return collisionInfo;
    }

    /**
     * Birthday attack on rollinghash
     * @param bits  number of places
     * @return      the hash, the 2 Strings with the same hash, and the number of tries taken
     */
    public static String[] birthday(int bits)
    {
        int H                           = (int) Math.pow(2, bits);
        int expected                    = (int) valuesBeforeCollision(H);
        HashMap<Long, String> hashes    = new HashMap<>();
        String[] collisionInfo          = new String[4];
        int counter                     = 0;
        boolean collision               = false;
        while (!collision)
        {
            counter++;
            int length = (int)(Math.random() * 20);
            String in  = "";
            for (int j = 0; j < length; j++)
            {
                in += (char)(int)(32 + Math.random() * 95);
            }
            long hash = HashFunction.rollingHash(in);
            if (hashes.get(hash) == null)
            {
                hashes.put(hash, in);
            } else
            {
                collision     = true;
                collisionInfo = new String[] {"" + hash, in, hashes.get(hash), "" + counter};
            }
            if (counter >= expected && collision) break;
        }
        return collisionInfo;
    }

    /**
     * Birthday attack on the rollinghash2
     * @param bits  length of String
     * @return      the hash, the 2 Strings with the same hash, and the number of tries taken
     */
    public static String[] birthday2(int bits)
    {
        int H                           = getH(CHAR_POSSIBILITIES, 32);
        int expected                    = valuesBeforeCollision(H);
        HashMap<String, String> hashes  = new HashMap<>();
        String[] collisionInfo          = new String[4];
        int counter                     = 0;
        boolean collision               = false;
        while (!collision)
        {
            counter++;
//            String in  = HashFunction.generateSalt((int)(Math.random() * bits) + 32);
            String in = HashFunction.generateSalt(bits);
            String hash = HashFunction.rollingHash2OldSet(in);
            if (hashes.get(hash) == null)
            {
                hashes.put(hash, in);
            } else if (!hashes.get(hash).equals(in))
            {
                collision     = true;
                collisionInfo = new String[] {hash, in, hashes.get(hash), "" + counter};
            }
            System.out.println(counter + "\t" + hash + "\t\t" + in);
            if (counter > expected * 1.5 && !collision)
            {
                collisionInfo = null;
                collision = true;
            }
        }
        return collisionInfo;
    }
}