package gtu.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ResourceUtil {

    private static final String VFS_PROTOCOL = "vfs";

    /**
     * Extracts tesseract resources to temp folder.
     *
     * @param resourceName
     *            name of file or directory
     * @return target path, which could be file or directory
     */
    public static synchronized File extractTessResources(String resourceName, File targetPath) {
        try {
            Enumeration<URL> resources = ResourceUtil.class.getClassLoader().getResources(resourceName);
            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                copyResources(resourceUrl, targetPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("extractTessResources Err : " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("extractTessResources Err : " + e.getMessage(), e);
        }
        return targetPath;
    }

    /**
     * Copies resources to target folder.
     *
     * @param resourceUrl
     * @param targetPath
     * @return
     */
    public static void copyResources(URL resourceUrl, File targetPath) throws IOException, URISyntaxException {
        if (resourceUrl == null) {
            return;
        }

        URLConnection urlConnection = resourceUrl.openConnection();

        /**
         * Copy resources either from inside jar or from project folder.
         */
        if (urlConnection instanceof JarURLConnection) {
            copyJarResourceToPath((JarURLConnection) urlConnection, targetPath);
        } else if (VFS_PROTOCOL.equals(resourceUrl.getProtocol())) {
            VirtualFile virtualFileOrFolder = VFS.getChild(resourceUrl.toURI());
            copyFromWarToFolder(virtualFileOrFolder, targetPath);
        } else {
            File file = new File(resourceUrl.getPath());
            if (file.isDirectory()) {
                for (File resourceFile : FileUtils.listFiles(file, null, true)) {
                    int index = resourceFile.getPath().lastIndexOf(targetPath.getName()) + targetPath.getName().length();
                    File targetFile = new File(targetPath, resourceFile.getPath().substring(index));
                    if (!targetFile.exists() || targetFile.length() != resourceFile.length()) {
                        if (resourceFile.isFile()) {
                            FileUtils.copyFile(resourceFile, targetFile);
                        }
                    }
                }
            } else {
                if (!targetPath.exists() || targetPath.length() != file.length()) {
                    FileUtils.copyFile(file, targetPath);
                }
            }
        }
    }

    /**
     * Copies resources from the jar file of the current thread and extract it
     * to the destination path.
     *
     * @param jarConnection
     * @param destPath
     *            destination file or directory
     */
    public static void copyJarResourceToPath(JarURLConnection jarConnection, File destPath) {
        JarFile jarFile = null;
        try {
            jarFile = jarConnection.getJarFile();
            String jarConnectionEntryName = jarConnection.getEntryName();

            /**
             * Iterate all entries in the jar file.
             */
            for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
                JarEntry jarEntry = e.nextElement();
                String jarEntryName = jarEntry.getName();

                /**
                 * Extract files only if they match the path.
                 */
                if (jarEntryName.startsWith(jarConnectionEntryName + "/")) {
                    String filename = jarEntryName.substring(jarConnectionEntryName.length());
                    File targetFile = new File(destPath, filename);

                    if (jarEntry.isDirectory()) {
                        targetFile.mkdirs();
                    } else {
                        if (!targetFile.exists() || targetFile.length() != jarEntry.getSize()) {
                            InputStream is = null;
                            OutputStream out = null;
                            try {
                                is = jarFile.getInputStream(jarEntry);
                                out = FileUtils.openOutputStream(targetFile);
                                IOUtils.copy(is, out);
                            } catch (Exception ex) {
                                throw new RuntimeException("copyJarResourceToPath Err : " + ex.getMessage(), ex);
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("copyJarResourceToPath Err : " + ex.getMessage(), ex);
        }
    }

    /**
     * Copies resources from WAR to target folder.
     *
     * @param virtualFileOrFolder
     * @param targetFolder
     * @throws IOException
     */
    public static void copyFromWarToFolder(VirtualFile virtualFileOrFolder, File targetFolder) throws IOException {
        if (virtualFileOrFolder.isDirectory() && !virtualFileOrFolder.getName().contains(".")) {
            if (targetFolder.getName().equalsIgnoreCase(virtualFileOrFolder.getName())) {
                for (VirtualFile innerFileOrFolder : virtualFileOrFolder.getChildren()) {
                    copyFromWarToFolder(innerFileOrFolder, targetFolder);
                }
            } else {
                File innerTargetFolder = new File(targetFolder, virtualFileOrFolder.getName());
                innerTargetFolder.mkdir();
                for (VirtualFile innerFileOrFolder : virtualFileOrFolder.getChildren()) {
                    copyFromWarToFolder(innerFileOrFolder, innerTargetFolder);
                }
            }
        } else {
            File targetFile = new File(targetFolder, virtualFileOrFolder.getName());
            if (!targetFile.exists() || targetFile.length() != virtualFileOrFolder.getSize()) {
                FileUtils.copyURLToFile(virtualFileOrFolder.asFileURL(), targetFile);
            }
        }
    }
}
