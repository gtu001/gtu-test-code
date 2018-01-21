/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.ant;

import gtu.runtime.ProcessWatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Troy
 */
public class AntClassLoader extends Task {

    private String clz;

    @Override
    public void execute() throws BuildException {
        MyLoader myLoader = new MyLoader();
        try {
            myLoader.loadClass(clz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        super.execute();
        System.out.println("test OK!!");
    }

    public static void main(String[] args) throws ClassNotFoundException {
        AntClassLoader ant = new AntClassLoader();
        // ant.setClz("gtu._work.ui.project.xml.CreateHibernateXmlUI");
        ant.setClz("C:\\workspace\\GTU\\src\\gtu\\_work\\ui\\project\\xml\\CreateHibernateXmlUI");
        ant.execute();
    }

    class MyLoader extends ClassLoader {

        @Override
        protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            Class<?> clas = null;
            clas = findLoadedClass(name);
            System.out.println("1-findLoadedClass: " + clas);
            String javaFilename = name + ".java";
            String classFilename = name + ".class";

            File javaFile = new File(javaFilename);
            File classFile = new File(classFilename);

            System.out.println("javaFile - " + javaFile.getAbsolutePath());
            System.out.println("classFile - " + javaFile.getAbsolutePath());

            System.out.println("\tjavaFile.exists() = " + javaFile.exists());
            System.out.println("\tclassFile.exists() = " + classFile.exists());
            System.out.println("\tclassFile.lastModified() = " + javaFile.lastModified());
            System.out.println("\tclassFile.lastModified() = " + classFile.lastModified());

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

        private boolean compile(String javaFile) throws IOException {
            System.out.println("CCL: Compiling " + javaFile + "...");
            Process p = Runtime.getRuntime().exec("javac " + javaFile);
            ProcessWatcher pp = ProcessWatcher.newInstance(p).getStream();
            System.out.println("input stream = " + pp.getInputStreamToString());
            System.out.println("error stream = " + pp.getErrorStreamToString());
            try {
                p.waitFor();
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            int ret = p.exitValue();
            return ret == 0;
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
    }

    public void setClz(String clz) {
        this.clz = clz;
    }
}
