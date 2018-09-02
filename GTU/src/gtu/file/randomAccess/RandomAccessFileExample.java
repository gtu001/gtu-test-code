package gtu.file.randomAccess;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * -----------------------------------------------------------------------------
 * This program demonstrates how to randomly access a file using the
 * RandomAccessFile class.
 * 
 * @version 1.0
 * @author Jeffrey M. Hunter (jhunter@idevelopment.info)
 * @author http://www.idevelopment.info
 *         ------------------------------------------
 *         -----------------------------------
 */

/**
 * @author Troy 2012/1/7
 * 
 */
public class RandomAccessFileExample {

    private void doAccess() {

        try {
            String fileName = this.getClass().getResource("").getPath() + "\\RandomAccessFileExample.out";
            File file = new File(fileName);
            System.out.println("fileName = " + file);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");

            // Read a character
            byte ch = raf.readByte();
            System.out.println("Read first character of file: " + (char) ch);

            // Now read the remaining portion of the line.
            // This will print out from where the file pointer is located
            // (just after the '+' character) and print all remaining characters
            // up until the end of line character.
            System.out.println("Read full line: " + raf.readLine());

            // Seek to the end of file
            raf.seek(file.length());

            // Append to the end of the file
            raf.write(0x0A);
            raf.writeBytes("This will complete the example");

            raf.close();

        } catch (IOException e) {
            System.out.println("IOException:");
            e.printStackTrace();
        }
    }

    /**
     * Sole entry point to the class and application.
     * 
     * @param args
     *            Array of String arguments.
     */
    public static void main(String[] args) {
        new RandomAccessFileExample().doAccess();
    }

}