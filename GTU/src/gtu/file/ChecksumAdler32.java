package gtu.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

/**
 * -----------------------------------------------------------------------------
 * Used to provide an example of how to calculate the checksum of a file using
 * the Adler-32 checksum engine.
 * 
 * @version 1.0
 * @author Jeffrey M. Hunter (jhunter@idevelopment.info)
 * @author http://www.idevelopment.info
 *         ------------------------------------------
 *         ----------------------------------- 2012/1/8 对文件进行 Adler32 校验
 */
public class ChecksumAdler32 {

    private static void doChecksum(String fileName) {

        try {

            CheckedInputStream cis = null;
            long fileSize = 0;
            try {
                // Computer Adler-32 checksum
                cis = new CheckedInputStream(new FileInputStream(fileName), new Adler32());

                fileSize = new File(fileName).length();

            } catch (FileNotFoundException e) {
                System.err.println("File not found.");
                System.exit(1);
            }

            byte[] buf = new byte[128];
            while (cis.read(buf) >= 0) {
            }

            long checksum = cis.getChecksum().getValue();
            System.out.println(checksum + " " + fileSize + " " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Sole entry point to the class and application.
     * 
     * @param args
     *            Array of String arguments.
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java ChecksumAdler32 filename");
        } else {
            doChecksum(args[0]);
        }

    }

}