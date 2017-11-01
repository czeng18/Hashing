package hashing;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Does username and password storage in a file
 * @author  Caroline Zeng
 * @version 0.1.1
 */

public class FileProcessing {
    File file;
    ObjectOutputStream out;
    ObjectInputStream in;
    HashMap<String, Object[]> map = new HashMap<>();

    /**
     * Constructor for FileProcessing
     */
    public FileProcessing()
    {
        try {
            // Open file, or create file if it doesn't exist
            file = new File("hashes.txt");
            if(!(file.exists() && !file.isDirectory()))
            {
                // Open Streams
                out  = new ObjectOutputStream(new FileOutputStream(file, true));
                in   = new ObjectInputStream(new FileInputStream(file));
                out.flush();
                out.close();
            } else
            {
                in = new ObjectInputStream(new FileInputStream(file));
            }
            // Number of Objects in the file
            long length = in.readLong();
            String read = "";
            // Read all Objects in the file, and store in a Hashmap
            for (int i = 0; i < length; i++)
            {
                read            = (String) in.readObject();
                String[] parts  = read.split(",");
                String key      = parts[0];
                String salt     = parts[1];
                String hash     = parts[2];
                Object[] value  = new Object[] {salt, hash};
                map.put(key, value);
            }
            in.close();
        } catch (Exception e) {}
    }

    /**
     * Constructor for FileProcessing for a given file
     * @param fileName  name of file
     */
    public FileProcessing(String fileName)
    {
        try {
            // Open file, or create file if it doesn't exist
            file = new File(fileName);
            if(!(file.exists() && !file.isDirectory()))
            {
                // Open Streams
                out  = new ObjectOutputStream(new FileOutputStream(file, true));
                in   = new ObjectInputStream(new FileInputStream(file));
                out.flush();
                out.close();
            } else
            {
                in = new ObjectInputStream(new FileInputStream(file));
            }
            // Number of Objects in the file
            long length = in.readLong();
            String read = "";
            // Read all Objects in the file, and store in a Hashmap
            for (int i = 0; i < length; i++)
            {
                read            = (String) in.readObject();
                String[] parts  = read.split(",");
                String key      = parts[0];
                String salt     = parts[1];
                String hash     = parts[2];
                Object[] value  = new Object[] {salt, hash};
                map.put(key, value);
            }
            in.close();
        } catch (Exception e) {}
    }

    /**
     * Add an entry to the Hashmap
     * @param uName username to store
     * @param pass  password to store associated to username
     * @return      true if storage is successful, and the username is not already stored
     *              false if storage failed, and username already exists in file
     */
    public boolean putInfo(String uName, String pass)
    {
        // Check if username is already stored in the file
        Object[] value = map.get(uName);
        if (value == null)
        {
            // Generate information for hashing the password
            String[] passHash   = HashFunction.rollingHash2New(pass);
            value    = new Object[2];
            value[0] = passHash[0];     //Hash
            value[1] = passHash[1];     //Salt
            map.put(uName, value);
            return true;
        }
        return false;
    }

    /**
     * Find the information associated with a username
     * @param uName username
     * @return      information associated with username
     */
    public Object[] find(String uName)
    {
        Object[] value = map.get(uName);
        return value;
    }

    /**
     * Checks if a password is correct for a username
     * If username is not stored, returns false
     * @param uName username
     * @param pass  password to test
     * @return      true if password matches stored password
     *              false if username is not in the file, or does not match the stored password
     */
    public boolean checkPassword(String uName, String pass)
    {
        Object[] value = find(uName);
        if (value != null)
        {
            String hash   = HashFunction.rollingHash2Old(pass, value[1] + "");
            if (hash.equals((String)(value[0]))) return true;
        }
        return false;
    }

    /**
     * Close the FileProcessing
     * Store information in a file
     */
    public void close()
    {
        Iterator it = map.entrySet().iterator();
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            // Number of Objects in file
            out.writeLong(map.size());
            while (it.hasNext()) {
                Map.Entry pair  = (Map.Entry) it.next();
                String key      = (String) pair.getKey();
                Object[] value  = (Object[]) pair.getValue();
                String output   = key + "," + value[0] + "," + value[1];
                out.writeObject(output);
                it.remove();
            }
            out.flush();
            out.close();
        } catch (IOException e) {}
    }

    /**
     * Get all stored usernames
     * @return  String of all usernames
     */
    public String getUserNames()
    {
        Iterator it = map.entrySet().iterator();
        String out = "";
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            String key = (String) pair.getKey();
            out += key + "\n";
        }
        return out;
    }
}