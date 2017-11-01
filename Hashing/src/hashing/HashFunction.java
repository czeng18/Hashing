package hashing;

/**
 * Functions to generate hashing information
 * @author  Caroline Zeng
 * @version 0.2.0
 */

public class HashFunction {
    static final int HASH_LENGTH = 32;
    /**
     * Hashes a String using a rolling hash
     * @param s String to hash
     * @return  hash value
     */
    public static long rollingHash(String s)
    {
        int hashValue = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            hashValue += (int)c * Math.pow(3, s.length() - i);
        }
        return hashValue >>> 4;
    }

    /**
     * Generates a random salt to add to a String before hashing
     * @param length    length of salt wanted
     * @return          random salt
     */
    public static String generateSalt(int length)
    {
        String salt = "";
        for (int i = 0; i < length; i++)
        {
            char c = (char)((int) (Math.random() * 93) + 33);
            salt += c;
        }
        return salt;
    }

    /**
     * Generates a new hash value for input String using a hash function
     * @param s String to hash
     * @return  hash value and the salt added to the String
     */
    public static String[] rollingHash2New(String s)
    {
        int length = s.length();
        int diff = 32 - length;
        String input = s;
        String salt = "";
        if (diff > 0)
        {
            salt = generateSalt(diff);
            input += salt;
        }
        char[] hash = new char[HASH_LENGTH];
        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            int a = (int)c;
            long b = (long)(Math.pow(7, input.length() - i)) % 500;
            long index = ((a * b) % 93);
            int sum = (int)index;
            if ((int)(hash[i % HASH_LENGTH]) != 0) {
                sum += (int) (hash[i % HASH_LENGTH]) - 33;
            }
            hash[i % HASH_LENGTH] = (char)(sum % 93 + 33);
        }
        return new String[] {new String(hash), salt};
    }

    /**
     * Generates a hash value for input String using a hash function
     * @param s     String to hash
     * @param salt  salt added to the String
     * @return      hash value
     */
    public static String rollingHash2Old(String s, String salt)
    {
        String input    = s + salt;
        char[] hash     = new char[input.length()];
        for (int i = 0; i < input.length(); i++)
        {
            char c      = input.charAt(i);
            int a       = (int)c;
            long b      = (long)(Math.pow(7, input.length() - i)) % 500;
            long index  = ((a * b) % 93);
            int sum     = (int)index;
            if ((int)(hash[i % HASH_LENGTH]) != 0) {
                sum += (int) (hash[i % HASH_LENGTH]) - 33;
            }
            hash[i % HASH_LENGTH] = (char)(sum % 93 + 33);
        }
        return new String(hash);
    }

    /**
     * Generates a hash value for input String using a hash function with a set output length
     * @param s String to hash
     * @return  hash value
     */
    public static String rollingHash2OldSet(String s)
    {
        String input    = s;
        char[] hash     = new char[HASH_LENGTH];
        for (int i = 0; i < input.length(); i++)
        {
            char c      = input.charAt(i);
            int a       = (int)c;
            long b      = (long)(Math.pow(7, input.length() - i)) % 500;
            long index  = ((a * b) % 93);
            int sum     = (int)index;
            if ((int)(hash[i % HASH_LENGTH]) != 0) {
                sum += (int) (hash[i % HASH_LENGTH]) - 33;
            }
            hash[i % HASH_LENGTH] = (char)(sum % 93 + 33);
        }
        return new String(hash);
    }
}