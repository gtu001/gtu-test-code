package gtu.svn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckForModifications {

    public static void main(String[] args) {
        CheckForModifications.getInstance().file(new File("C:/workspace_RS430/sris-rl")).execute();
        System.out.println("done...");
    }

    private CheckForModifications() {
    }

    File file;

    public CheckForModifications file(File file) {
        this.file = file;
        return this;
    }

    Pattern svnOutputPattern = Pattern.compile("([M\\?X]?)\\s*(\\d*)\\s+(\\d+)\\s+(\\w+)\\s+([\\S]+)");

    public CheckForModifications execute() {
        try {
            Matcher matcher = null;
            SvnFile svnFile = null;
            Set<SvnFile> svnFileSet = new HashSet<SvnFile>();
            long projectLastestVersion = 0;
            Process process = Runtime.getRuntime().exec(String.format("cmd /c svn status -v \"%s\"", file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            for (String line = null; (line = reader.readLine()) != null;) {
                System.out.println(line);
                //                if (line.startsWith(" ")) {
                //                    continue;
                //                }
                //                matcher = svnOutputPattern.matcher(line);
                //                if (matcher.find()) {
                //
                //                    if (StringUtils.isNotBlank(matcher.group(1))) {
                //                        projectLastestVersion = Math.max(projectLastestVersion, Long.parseLong(matcher.group(1)));
                //                    }
                //                    svnFile = new SvnFile();
                //                    svnFile.lastestVersion = Long.parseLong(matcher.group(2));
                //                    //                    svnFile.author = matcher.group(3);
                //                    svnFile.filePath = matcher.group(4);
                //                    svnFile.file = new File(svnFile.filePath);
                //                    svnFile.fileName = svnFile.file.getName();
                //                    svnFileSet.add(svnFile);
                //                    System.out.println(svnFile);
                //                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private static CheckForModifications INSTANCE = new CheckForModifications();

    public static CheckForModifications getInstance() {
        return INSTANCE;
    }

    static class SvnFile implements Comparable<SvnFile>, Serializable {
        private static final long serialVersionUID = 1326802331489780898L;
        File file;
        String fileName;
        String filePath;
        long lastestVersion = -1L;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SvnFile other = (SvnFile) obj;
            if (filePath == null) {
                if (other.filePath != null)
                    return false;
            } else if (!filePath.equals(other.filePath))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return filePath;
        }

        @Override
        public int compareTo(SvnFile o) {
            return fileName.compareTo(o.fileName);
        }
    }
}
