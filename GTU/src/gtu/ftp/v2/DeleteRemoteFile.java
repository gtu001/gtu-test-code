package gtu.ftp.v2;

import java.io.FileInputStream;
import java.util.Properties;

import javax.tools.FileObject;

public class DeleteRemoteFile {

    static Properties props;

    public static void main(String[] args) {
        DeleteRemoteFile getMyFiles = new DeleteRemoteFile();
        if (args.length < 1) {
            System.err.println("Usage: java " + getMyFiles.getClass().getName()
                    + " Properties_filename File_To_Delete ");
            System.exit(1);
        }

        String propertiesFilename = args[0].trim();
        String fileToDownload = args[1].trim();
        getMyFiles.startFTP(propertiesFilename, fileToDownload);
    }

    public boolean startFTP(String propertiesFilename, String fileToDownload) {
        props = new Properties();
        StandardFileSystemManager manager = new StandardFileSystemManager();
        try {

            props.load(new FileInputStream("properties/" + propertiesFilename));
            String serverAddress = props.getProperty("serverAddress").trim();
            String userId = props.getProperty("userId").trim();
            String password = props.getProperty("password").trim();
            String remoteDirectory = props.getProperty("remoteDirectory").trim();

            // Initializes the file manager
            manager.init();

            // Setup our SFTP configuration
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
            SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

            // Create the SFTP URI using the host name, userid, password, remote
            // path and file name
            String sftpUri = "sftp://" + userId + ":" + password + "@" + serverAddress + "/" + remoteDirectory
                    + fileToDownload;

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(sftpUri, opts);

            // Check if the file exists
            if (remoteFile.exists()) {
                remoteFile.delete();
                System.out.println("File delete successful");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            manager.close();
        }

        return true;
    }
}