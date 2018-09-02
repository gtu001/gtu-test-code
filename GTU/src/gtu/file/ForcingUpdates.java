package gtu.file;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * -----------------------------------------------------------------------------
 * This program demonstrates how to force updates to a file to a disk. In some
 * applications, such as transaction processing, it is necessary to ensure that
 * an update has been made to the disk. FileDescriptor.sunc() blocks until all
 * changes to a file are written to disk.
 * 
 * @version 1.0
 * @author Jeffrey M. Hunter (jhunter@idevelopment.info)
 * @author http://www.idevelopment.info
 *         ------------------------------------------
 *         ----------------------------------- 2012/1/8 确保文件保存到磁盘中
 */
public class ForcingUpdates {

    private static void doForceUpdate() {

        try {

            // Open or create the output file
            String fn = "outfile.txt";
            FileOutputStream os = new FileOutputStream(fn);
            FileDescriptor fd = os.getFD();
            System.out.println("Opened file: " + fn);

            // Write some data to the stream
            byte[] data = new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE };
            os.write(data);
            System.out.println("Wrote the following data to stream: " + data);

            // Flush the data from the streams and writers into system buffers.
            // The data may or may not be written to disk.
            System.out.println("Flushing from streams to system buffers.");
            os.flush();

            // Block until the system buffers have been written to disk.
            // After this method returns, the data is guaranteed to have been
            // written to disk.
            System.out.println("Blocking until system buffers have been written.");
            fd.sync();

            // Close out the file stream
            os.close();
            System.out.println("Closed file: " + fn);

        } catch (IOException e) {
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
        doForceUpdate();
    }

}