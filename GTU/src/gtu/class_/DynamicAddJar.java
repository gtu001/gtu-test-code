package gtu.class_;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

public class DynamicAddJar {

    public static void main(String[] args) {
        DynamicAddJar t = new DynamicAddJar();
    }

    public void addJarFile(File jarFile) {
        try {
            URL url = jarFile.toURI().toURL();
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addJarFileTest() throws MalformedURLException, FileNotFoundException {
        JarClassLoader jcl = new JarClassLoader();
        jcl.add("myjar.jar"); // Load jar file
        jcl.add(new URL("http://myserver.com/myjar.jar")); // Load jar from a
                                                           // URL
        jcl.add(new FileInputStream("myotherjar.jar")); // Load jar file from
                                                        // stream
        jcl.add("myclassfolder/"); // Load class folder
        jcl.add("myjarlib/"); // Recursively load all jar files in the
                              // folder/sub-folder(s)

        JclObjectFactory factory = JclObjectFactory.getInstance();
        // Create object of loaded class
        Object obj = factory.create(jcl, "mypackage.MyClass");
    }

    public ClassLoader getClassLoaderForExtraModule(File jarFile) throws IOException {
        List<URL> urls = new ArrayList<URL>(5);
        // foreach( filepath: external file *.JAR) with each external file
        // *.JAR, do as follows
        JarFile jf = new JarFile(jarFile);
        urls.add(jarFile.toURI().toURL());
        Manifest mf = jf.getManifest(); // If the jar has a class-path in it's
                                        // manifest add it's entries
        if (mf != null) {
            String cp = mf.getMainAttributes().getValue("class-path");
            if (cp != null) {
                for (String cpe : cp.split("\\s+")) {
                    File lib = new File(jarFile.getParentFile(), cpe);
                    urls.add(lib.toURI().toURL());
                }
            }
        }
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (urls.size() > 0) {
            cl = new URLClassLoader(urls.toArray(new URL[urls.size()]), ClassLoader.getSystemClassLoader());
        }
        return cl;
    }

    public void initialize(String libDir) throws Exception {
        File dependencyDirectory = new File(libDir);
        File[] files = dependencyDirectory.listFiles();
        ArrayList<URL> urls = new ArrayList<URL>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".jar")) {
                urls.add(files[i].toURL());
                // urls.add(files[i].toURI().toURL());
            }
        }
        classLoader = new JarFileClassLoader("Scheduler CL" + System.currentTimeMillis(), //
                urls.toArray(new URL[urls.size()]), //
                GFClassLoader.class.getClassLoader());//
        classLoader.loadClass(name);
    }

    public void addAndroidJar() {
        String jarFile = "path/to/jarfile.jar";
        DexClassLoader classLoader = new DexClassLoader(jarFile, //
                "/data/data/" + context.getPackageName() + "/", null, getClass().getClassLoader());
        Class<?> myClass = classLoader.loadClass("MyClass");
    }
    
    public void replaceClassLoader() throws Exception{
        String dirBase = "c:/jars";

        File file = new File(dirBase, "lib");
        String[] jars = file.list();
        URL[] jarUrls = new URL[jars.length + 1];
        int i = 0;
        for (String jar : jars) {
            File fileJar = new File(file, jar);
            jarUrls[i++] = fileJar.toURI().toURL();
            System.out.println(fileJar);
        }
        jarUrls[i] = new File(dirBase, "conf").toURI().toURL();

        URLClassLoader classLoader = new URLClassLoader(jarUrls, this.getClass().getClassLoader());

        // this is required to load file (such as spring/context.xml) into the jar
        Thread.currentThread().setContextClassLoader(classLoader);

        Class classToLoad = Class.forName("my.app.Main", true, classLoader);
        Object instance = classToLoad.newInstance();
    }
}
