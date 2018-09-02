package gtu.class_;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CompilingClassLoader extends ClassLoader {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException, SecurityException, NoSuchMethodException {
        // The first argument is the Java program (class) the user
        // wants to run
        args = new String[] { "gtu.class_.classloader.Bar" };

        String progClass = args[0];

        // And the arguments to that program are just
        // arguments 1..n, so separate those out into
        // their own array
        String progArgs[] = new String[args.length - 1];
        System.arraycopy(args, 1, progArgs, 0, progArgs.length);

        // Create a CompilingClassLoader
        CompilingClassLoader ccl = new CompilingClassLoader();

        // Load the main class through our CCL
        Class clas = ccl.loadClass(progClass);

        // Use reflection to call its main() method, and to
        // pass the arguments in.

        // Get a class representing the type of the main method's argument
        Class mainArgType[] = { (new String[0]).getClass() };

        // Find the standard main method in the class
        Method main = clas.getMethod("main", mainArgType);

        // Create a list containing the arguments -- in this case,
        // an array of strings
        Object argsArray[] = { progArgs };

        // Call the method
        main.invoke(null, argsArray);
    }

    private byte[] getBytes(String filename) throws IOException {
        File file = new File(filename);
        long len = file.length();
        byte raw[] = new byte[(int) len];
        FileInputStream fin = new FileInputStream(file);
        int r = fin.read(raw);
        if (r != len)
            throw new IOException("Can't read all, " + r + " != " + len);
        fin.close();
        return raw;
    }

    private boolean compile(String javaFile) throws IOException {
        System.out.println("CCL: Compiling " + javaFile + "...");
        Process p = Runtime.getRuntime().exec("javac " + javaFile);
        try {
            p.waitFor();
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        int ret = p.exitValue();
        return ret == 0;
    }

    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clas = null;
        clas = findLoadedClass(name);
        System.out.println("1-findLoadedClass: " + clas);
        String fileStub = name.replace('.', '/');
        String javaFilename = fileStub + ".java";
        String classFilename = fileStub + ".class";

        File javaFile = new File(javaFilename);
        File classFile = new File(classFilename);

        System.out.println("javaFile.exists() = " + javaFile.exists());
        System.out.println("classFile.exists() = " + classFile.exists());
        System.out.println("classFile.lastModified() = " + javaFile.lastModified());
        System.out.println("classFile.lastModified() = " + classFile.lastModified());

        if (javaFile.exists() && (!classFile.exists() || javaFile.lastModified() > classFile.lastModified())) {
            System.out.println("2....");
            try {
                boolean compileResult = compile(javaFilename);
                System.out.println("3 compileResult = " + compileResult);
                System.out.println("3 classFile.exists() = " + classFile.exists());
                if (!compileResult || !classFile.exists()) {
                    throw new ClassNotFoundException("Compile failed: " + javaFilename);
                }
            } catch (IOException ie) {
                throw new ClassNotFoundException(ie.toString());
            }
        }

        try {
            byte raw[] = getBytes(classFilename);
            clas = defineClass(name, raw, 0, raw.length);
        } catch (IOException ie) {
            // This is not a failure! If we reach here, it might
            // mean that we are dealing with a class in a library,
            // such as java.lang.Object
            ie.printStackTrace();
        }

        System.out.println("4 defineClass: " + clas);

        // Maybe the class is in a library -- try loading
        // the normal way
        if (clas == null) {
            clas = findSystemClass(name);
        }

        System.out.println("5 findSystemClass: " + clas);

        // Resolve the class, if any, but only if the "resolve"
        // flag is set to true
        if (resolve && clas != null)
            resolveClass(clas);

        if (clas == null)
            throw new ClassNotFoundException(name);

        return clas;
    }
}