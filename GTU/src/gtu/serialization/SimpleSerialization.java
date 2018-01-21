package gtu.serialization;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 序列化 (Serializable) 的例子
 * 
 * @author Troy 2012/1/7
 */
public class SimpleSerialization {

    /**
     * Create a simple Hashtable and serialize it to a file called
     * HTExample.ser.
     */
    private static void doSave() {

        System.out.println();
        System.out.println("+------------------------------+");
        System.out.println("| doSave Method                |");
        System.out.println("+------------------------------+");
        System.out.println();

        Hashtable h = new Hashtable();
        h.put("string", "Oracle / Java Programming");
        h.put("int", new Integer(36));
        h.put("double", new Double(Math.PI));

        try {

            System.out.println("Creating File/Object output stream...");

            FileOutputStream fileOut = new FileOutputStream("HTExample.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            System.out.println("Writing Hashtable Object...");
            out.writeObject(h);

            System.out.println("Closing all output streams...\n");
            out.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the contents of a previously serialized object from a file called
     * HTExample.ser.
     */
    private static void doLoad() {

        System.out.println();
        System.out.println("+------------------------------+");
        System.out.println("| doLoad Method                |");
        System.out.println("+------------------------------+");
        System.out.println();

        Hashtable h = null;

        try {

            System.out.println("Creating File/Object input stream...");

            FileInputStream fileIn = new FileInputStream("HTExample.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            System.out.println("Loading Hashtable Object...");
            h = (Hashtable) in.readObject();

            System.out.println("Closing all input streams...\n");
            in.close();
            fileIn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Printing out loaded elements...");
        for (Enumeration e = h.keys(); e.hasMoreElements();) {
            Object obj = e.nextElement();
            System.out.println("  - Element(" + obj + ") = " + h.get(obj));
        }
        System.out.println();

    }

    /**
     * Sole entry point to the class and application.
     * 
     * @param args
     *            Array of String arguments.
     */
    public static void main(String[] args) {
        doSave();
        doLoad();
    }

}