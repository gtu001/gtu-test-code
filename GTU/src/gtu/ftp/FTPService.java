package gtu.ftp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;


public class FTPService {
	private static final Logger logger = Logger.getLogger(FTPService.class);

	private FTPClient ftpClient;
	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;

	public FTPService() {

	}
	public void connectServer(String server, String user, String password) throws SocketException, IOException {
		ftpClient = new FTPClient();
		ftpClient.connect(server);
		logger.info("Connected to FTP Server:" + server + ".");
		logger.info("FTP Server ReplyCode:" + ftpClient.getReplyCode());
		boolean loginflag = ftpClient.login(user, password);
		logger.info("FTP Server Login:" + loginflag);
	}

	/**
	 * Set transform type FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE
	 * 
	 * @param fileType
	 * @return
	 * @throws IOException
	 * @author Fernando
	 * @Date 2014/4/1
	 */
	public boolean setFileType(int fileType) throws IOException {
		return ftpClient.setFileType(fileType);
	}

	public void closeServer() throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
			logger.info("FTP Server disconnect..");
		}
	}

	public int getReplyCode() {
		return ftpClient.getReplyCode();
	}

	public boolean download(String remoteFileName, String localFileName) throws IOException {
		boolean flag = false;
		//File outfile = new File(localFileName);
		OutputStream oStream = null;
		logger.info("remoteFileName:"+remoteFileName);
		logger.info("localFileName:"+localFileName);
		oStream = new FileOutputStream(localFileName);
		ftpClient.enterLocalPassiveMode();
		flag = ftpClient.retrieveFile(remoteFileName, oStream);

		if (oStream != null)
			oStream.close();

		return flag;
	}

	
	
	
	/**
	 * Download File for InputStream
	 * 
	 * @param sourceFileName
	 * @return
	 * @throws IOException
	 * @author Fernando
	 * @Date 2014/4/1
	 */
	public InputStream downloadFile(String sourceFileName) throws IOException {
		ftpClient.enterLocalPassiveMode();
		InputStream in = ftpClient.retrieveFileStream(sourceFileName);

		if (in == null) {
			logger.warn(sourceFileName + " File has Failed to download .");
			return null;
		} else {
			// Call completePendingCommand() method to complete transaction.
			logger.info(sourceFileName + " File has been downloaded successfully.");
		}
		return in;
	}
	public void completePendingCommand() throws IOException {
		ftpClient.completePendingCommand();
	}

	public ArrayList<InputStream> downloadMultipleFiles(List<String> filenamelist) throws IOException {
		ArrayList<InputStream> ins = new ArrayList<InputStream>();
		ftpClient.enterLocalPassiveMode();

		if (filenamelist != null && filenamelist.size() > 0) {
			// loop thru files
			for (String filename : filenamelist) {

				InputStream in = ftpClient.retrieveFileStream(filename);
				if (in != null) {
					// Call completePendingCommand() method to complete transaction.
					ftpClient.completePendingCommand();
					logger.debug(filename + " File has been downloaded successfully.");
					ins.add(in);
				} else {
					logger.warn(filename + " File has Failed to download .");
					continue;
				}
			}
		}

		return ins;
	}

	public boolean uploadFile(String fileName, String newName) throws IOException {
		boolean flag = false;
		InputStream iStream = null;

		iStream = new FileInputStream(fileName);
		flag = ftpClient.storeFile(newName, iStream);

		if (iStream != null)
			iStream.close();

		return flag;
	}

	public boolean uploadFile(String fileName) throws IOException {
		return uploadFile(fileName, fileName);
	}

	/**
	 * Upload File From InputStream
	 * 
	 * @param iStream
	 * @param newName
	 * @return
	 * @throws IOException
	 * @author Fernando
	 * @Date 2014/4/1
	 */
	public boolean uploadFile(InputStream iStream, String newName) throws IOException {
		boolean flag = false;

		// can execute [OutputStream storeFileStream(String remote)]
		// Above method return's value is the local file stream.
		flag = ftpClient.storeFile(newName, iStream);

		if (iStream != null) {
			iStream.close();
		}

		return flag;
	}

	// =======================================================================
	// == About directory =====
	// The following method using relative path better.
	// =======================================================================

	public String printWorkingDirectory() throws IOException {
		return ftpClient.printWorkingDirectory();
	}

	public boolean changeDirectory(String path) throws IOException {
		return ftpClient.changeWorkingDirectory(path);
	}

	public boolean createDirectory(String pathName) throws IOException {
		return ftpClient.makeDirectory(pathName);
	}

	public boolean removeDirectory(String path) throws IOException {
		return ftpClient.removeDirectory(path);
	}

	// delete all subDirectory and files.
	public boolean removeDirectory(String path, boolean isAll) throws IOException {

		if (!isAll) {
			return removeDirectory(path);
		}

		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return removeDirectory(path);
		}
		//
		for (FTPFile ftpFile : ftpFileArr) {
			String name = ftpFile.getName();
			if (ftpFile.isDirectory()) {
				logger.info("* [sD]Delete subPath [" + path + "/" + name + "]");
				removeDirectory(path + "/" + name, true);
			} else if (ftpFile.isFile()) {
				logger.info("* [sF]Delete file [" + path + "/" + name + "]");
				deleteFile(path + "/" + name);
			} else if (ftpFile.isSymbolicLink()) {

			} else if (ftpFile.isUnknown()) {

			}
		}
		return ftpClient.removeDirectory(path);
	}

	// Check the path is exist; exist return true, else false.
	public boolean existDirectory(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		for (FTPFile ftpFile : ftpFileArr) {
			if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	// =======================================================================
	// == About file =====
	// Download and Upload file using
	// ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE) better!
	// =======================================================================

	// #1. list operation
	// Not contains directory
	public List<String> getFilenameList() throws IOException {
		// listFiles return contains directory and file, it's FTPFile instance
		// listNames() contains directory, so using following to file directory.
		// String[] fileNameArr = ftpClient.listNames(path);
		FTPFile[] ftpFiles = ftpClient.listFiles();

		List<String> retList = new ArrayList<String>();
		if (ftpFiles == null || ftpFiles.length == 0) {
			return retList;
		}
		for (FTPFile ftpFile : ftpFiles) {
			if (ftpFile.isFile()) {
				retList.add(ftpFile.getName());
			}
		}
		return retList;
	}

	// delete operation
	public boolean deleteFile(String pathName) throws IOException {
		return ftpClient.deleteFile(pathName);
	}

	//move file operation
	public boolean moveFile(String sourcepath, String targetpath, String ftpfilename) throws IOException {		
		return ftpClient.rename(sourcepath + "/" + ftpfilename, targetpath + "/" + ftpfilename);
	}
}
