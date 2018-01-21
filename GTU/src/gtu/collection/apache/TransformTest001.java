package gtu.collection.apache;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.FileUtils;

public class TransformTest001 {

    static Predicate javaFilePredicate = new Predicate() {
        @Override
        public boolean evaluate(Object object) {
            File f = (File) object;
            if (f.getName().toLowerCase().endsWith(".java")) {
                return true;
            }
            return false;
        }
    };

    static Predicate otherFilePredicate = new Predicate() {
        @Override
        public boolean evaluate(Object object) {
            File f = (File) object;
            if (!f.getName().toLowerCase().endsWith(".java")) {
                if (!f.getName().toLowerCase().endsWith(".pck")) {
                    if (!f.getName().toLowerCase().endsWith(".jsp")) {
                        return true;
                    }
                }
            }
            return false;
        }
    };

    static Predicate jspFilePredicate = new Predicate() {
        @Override
        public boolean evaluate(Object object) {
            File f = (File) object;
            if (f.getName().toLowerCase().endsWith(".jsp")) {
                return true;
            }
            return false;
        }
    };

    static Predicate pckFilePredicate = new Predicate() {
        @Override
        public boolean evaluate(Object object) {
            File f = (File) object;
            if (f.getName().toLowerCase().endsWith(".pck")) {
                return true;
            }
            return false;
        }
    };

    static Transformer realFileTransformer = new Transformer() {
        final String oldPrefix = "/trunk/LS_SRC/modules";

        final String folderPrefix = "D:/tgl38/workspace/LS_SRC/modules";

        @Override
        public Object transform(Object input) {
            String input_ = (String) input;
            input_ = input_.substring(oldPrefix.length());
            File file = new File(folderPrefix + "/" + input_);
            return file;
        }
    };

    static Transformer fetchClassTransformer = new Transformer() {
        @Override
        public Object transform(Object input) {
            File input_ = (File) input;
            ClassInfo info = new ClassInfo();
            info.orignJavaFile = input_;

            String suffixPath = input_.getAbsolutePath().substring(input_.getAbsolutePath().indexOf(ClassInfo.FIND_KEY) + ClassInfo.FIND_KEY.length());
            String middlePath = suffixPath.substring(0, suffixPath.indexOf(ClassInfo.FIND_KEY2));
            String suffixPath2 = suffixPath.substring(suffixPath.indexOf(ClassInfo.FIND_KEY2) + ClassInfo.FIND_KEY2.length());

            info.rootFolder = new File(middlePath).toString().replace('\\', '-');
            info.packagePath = new File(suffixPath2).getParentFile().toString();

            // System.out.println(info.rootFolder);
            // System.out.println(info.packagePath);

            File newClassFolder = new File(input_.getParent().replaceAll(ClassInfo.FIND_KEY2, "/target/classes/"));
            String findName = input_.getName().substring(0, input_.getName().indexOf("."));

            // System.out.println(newClassFolder);
            for (File f : newClassFolder.listFiles()) {
                if (f.getName().startsWith(findName) && f.getName().endsWith(".class")) {
                    info.classFileList.add(f);
                    System.out.println(f);
                }
            }
            return info;
        }
    };

    static Transformer fetchJspTransformer = new Transformer() {
        final String FIND_KEY2 = "jsp";

        @Override
        public Object transform(Object input) {
            File input_ = (File) input;
            ResourceInfo info = new ResourceInfo();
            try {
                info.orignJavaFile = input_;
                String suffixPath = input_.getAbsolutePath().substring(input_.getAbsolutePath().indexOf(ClassInfo.FIND_KEY) + ClassInfo.FIND_KEY.length());
                String middlePath = suffixPath.substring(0, suffixPath.indexOf(FIND_KEY2));
                String suffixPath2 = suffixPath.substring(suffixPath.indexOf(FIND_KEY2) + FIND_KEY2.length());

                info.rootFolder = new File(middlePath).toString().replace('\\', '-');
                info.packagePath = new File(suffixPath2).getParentFile().toString();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage() + " : " + input, ex);
            }
            return info;
        }
    };

    static Transformer fetchResourceTransformer = new Transformer() {
        @Override
        public Object transform(Object input) {
            File input_ = (File) input;
            ResourceInfo info = new ResourceInfo();
            try {
                info.orignJavaFile = input_;
                String suffixPath = input_.getAbsolutePath().substring(input_.getAbsolutePath().indexOf(ClassInfo.FIND_KEY) + ClassInfo.FIND_KEY.length());
                String middlePath = suffixPath.substring(0, suffixPath.indexOf(ClassInfo.FIND_KEY2));
                String suffixPath2 = suffixPath.substring(suffixPath.indexOf(ClassInfo.FIND_KEY2) + ClassInfo.FIND_KEY2.length());

                info.rootFolder = new File(middlePath).toString().replace('\\', '-');
                info.packagePath = new File(suffixPath2).getParentFile().toString();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage() + " : " + input, ex);
            }
            return info;
        }
    };

    static Closure copyClassToDestClosure = new Closure() {
        @Override
        public void execute(Object input) {
            ClassInfo info = (ClassInfo) input;
            File destDir = new File(exportDir, "/" + info.rootFolder + "/" + info.packagePath + "/");
            destDir.mkdirs();

            for (File f : info.classFileList) {
                File newF = new File(destDir, f.getName());
                try {
                    FileUtils.copyFile(f, newF);
                    System.out.println("copy to : " + newF);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage() + " : " + f, e);
                }
            }
        }
    };

    static Closure copyResourceToDestClosure = new Closure() {
        @Override
        public void execute(Object input) {
            ResourceInfo info = (ResourceInfo) input;
            File destDir = new File(exportDir, "/" + info.rootFolder + "/" + info.packagePath + "/");
            destDir.mkdirs();
            File newF = null;
            try {
                newF = new File(destDir, info.orignJavaFile.getName());
                FileUtils.copyFile(info.orignJavaFile, newF);
                System.out.println("copy to : " + newF);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage() + " : " + newF, e);
            }
        }
    };
    
    static Closure copyJspToDestClosure = new Closure() {
        @Override
        public void execute(Object input) {
            ResourceInfo info = (ResourceInfo) input;
            File destDir = new File(exportDir, "/" + info.rootFolder + "_Jsp/" + info.packagePath + "/");
            destDir.mkdirs();
            File newF = null;
            try {
                newF = new File(destDir, info.orignJavaFile.getName());
                FileUtils.copyFile(info.orignJavaFile, newF);
                System.out.println("copy to : " + newF);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage() + " : " + newF, e);
            }
        }
    };

    static Closure copyPckToDestClosure = new Closure() {
        @Override
        public void execute(Object input) {
            File info = (File) input;
            File destDir = new File(exportDir, "/" + "plsql_pkg" + "/");
            destDir.mkdirs();
            File newF = null;
            try {
                newF = new File(destDir, info.getName());
                FileUtils.copyFile(info, newF);
                System.out.println("copy to : " + newF);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage() + " : " + newF, e);
            }
        }
    };

    static Closure sysOuClosure = new Closure() {
        @Override
        public void execute(Object input) {
            System.out.println("____________" + input);
        }
    };

    static class ClassInfo {
        static final String FIND_KEY = "LS_SRC\\modules\\";

        static final String FIND_KEY2 = "java";

        File orignJavaFile;

        String rootFolder;

        String packagePath;

        List<File> classFileList = new ArrayList<File>();
    }

    static class ResourceInfo {
        static final String FIND_KEY = "LS_SRC\\modules\\";

        static final String FIND_KEY2 = "java";

        File orignJavaFile;

        String rootFolder;

        String packagePath;

        boolean isPkg;
    }

    static final File inputFile = new File("D:/My Documents/獢/hotfix.txt");;

    static final File exportDir = new File("D:/hotfix/");;

    public static void main(String[] args) throws IOException {
        List<String> fileList = FileUtils.readLines(inputFile);

        exportDir.mkdirs();

        Collection<File> fileList2 = CollectionUtils.collect(fileList, realFileTransformer);

        Collection<File> fileListJava = new ArrayList<File>(fileList2);
        CollectionUtils.filter(fileListJava, javaFilePredicate);
        Collection<ClassInfo> classInfoList = CollectionUtils.collect(fileListJava, fetchClassTransformer);

        Collection<File> fileListJsp = new ArrayList<File>(fileList2);
        CollectionUtils.filter(fileListJsp, jspFilePredicate);
        Collection<ResourceInfo> jspInfoList = CollectionUtils.collect(fileListJsp, fetchJspTransformer);
        
        Collection<File> fileListOther = new ArrayList<File>(fileList2);
        CollectionUtils.filter(fileListOther, otherFilePredicate);
        Collection<ResourceInfo> resourceInfoList = CollectionUtils.collect(fileListOther, fetchResourceTransformer);

        Collection<File> fileListPck = new ArrayList<File>(fileList2);
        CollectionUtils.filter(fileListPck, pckFilePredicate);

        CollectionUtils.forAllDo(classInfoList, copyClassToDestClosure);
        CollectionUtils.forAllDo(resourceInfoList, copyResourceToDestClosure);
        CollectionUtils.forAllDo(fileListPck, copyPckToDestClosure);
        CollectionUtils.forAllDo(jspInfoList, copyJspToDestClosure);

        System.out.println("done...");
    }
}
