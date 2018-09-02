package gtu.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 過濾所設限的檔案類型
 * 
 * @author Troy 2012/1/7
 */
public class FileFilter {

    /**
     * FNFilter - Ls directory lister modified to use FilenameFilter
     * 
     * @author Ian Darwin
     * @version $Id: FNFilter.java,v 1.3 2004/03/11 03:33:35 ian Exp $
     */
    public static void main(String[] args) {
        // Generate the selective list, with a one-use File object.
        String[] dir = new File(".").list(new OnlyJava());
        java.util.Arrays.sort(dir); // Sort it (Data Structuring chapter))
        for (int i = 0; i < dir.length; i++)
            System.out.println(dir[i]); // Print the list
    }

    /**
     * This class implements the FilenameFilter interface. The Accept method
     * returns true for .java, .class and .jar files.
     */
    private static class OnlyJava implements FilenameFilter {
        public boolean accept(File dir, String s) {
            if (s.endsWith(".java") || s.endsWith(".class") || s.endsWith(".jar"))
                return true;
            // others: projects, ... ?
            return false;
        }
    }
}
