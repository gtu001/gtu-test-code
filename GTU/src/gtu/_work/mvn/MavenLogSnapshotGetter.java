package gtu._work.mvn;
import gtu.file.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MavenLogSnapshotGetter {

    //        [DEBUG] Extension realms for project com.iisigroup:ml-web:war:0.0.1-SNAPSHOT: (none)
    //        [DEBUG] Extension realms for project com.iisigroup:ris3cupertino:jar:1.0.2-SNAPSHOT: (none)
    static Pattern pattern = Pattern.compile("\\[DEBUG\\]\\sExtension\\srealms\\sfor\\sproject\\s([^:]+):([^:]+):(\\w+):([^:]+):.*");

    // cmd : mvn -U -X clean -l xxxx.txt

    /**
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Matcher matcher = null;
        File logFile = new File("C:/workspace/backup/xxxx.txt");
        File m2Dir = new File("C:/Users/Troy/.m2/repository");
        File copyTo = new File("C:/Jar/iisi");
        File snapshotFile = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), "UTF8"));

        Set<File> snapshotSet = new HashSet<File>();
        for (String line = null; (line = reader.readLine()) != null;) {
            matcher = pattern.matcher(line);
            if (matcher.matches()) {
                //                ====================
                //                        com.iisigroup
                //                        sris
                //                        pom
                //                        0.0.1-SNAPSHOT
                //                    ====================

                String subPath = matcher.group(1).replaceAll(Pattern.quote("."), "/");
                String fileName = matcher.group(2);
                String fileSubName = matcher.group(3);
                String version = matcher.group(4);

                System.out.println("====================");
                System.out.format("\t%s\n", subPath);
                System.out.format("\t%s\n", fileName);
                System.out.format("\t%s\n", matcher.group(3));
                System.out.format("\t%s\n", version);

                String subDir = subPath + "/" + fileName + "/" + version + "/";
                String fName = fileName + "-" + version + "." + fileSubName;

                if (fileSubName.equals("pom")) {
                    continue;
                }

                snapshotFile = new File(m2Dir, subDir + fName);
                if (snapshotFile.exists()) {
                    System.out.println("find : " + snapshotFile);
                    snapshotSet.add(snapshotFile);
                }
            }
        }
        reader.close();

        File newFile = null;
        for (File f : snapshotSet) {
            newFile = new File(copyTo, f.getName());
            FileUtil.copyFile(f, newFile);
            System.out.println(newFile);
        }
    }

}
