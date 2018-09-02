package gtu.jni;

public class JniTest001 {

    public static void main(String[] args) {

        System.loadLibrary("libaiengine.so");
        
        // Load native library at runtime //
        // hello.dll (Windows) or libhello.so (Unixes)

        String[] paths = System.getProperty("java.library.path").split(";", -1);
        for (String path : paths) {
            System.out.println(path);
        }
        System.out.println("done...");
    }

}
