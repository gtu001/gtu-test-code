/*
 * Copyright (c) 1997, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 only, as published by
 * the Free Software Foundation. Oracle designates this particular file as
 * subject to the "Classpath" exception as provided by Oracle in the LICENSE
 * file that accompanied this code.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License version 2 for more
 * details (a copy is included in the LICENSE file that accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA or
 * visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java_.net;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandlerFactory;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Resource;
import sun.misc.URLClassPath;
import sun.net.www.ParseUtil;
import sun.security.util.SecurityConstants;

/**
 * This class loader is used to load classes and resources from a search path of
 * URLs referring to both JAR files and directories. Any URL that ends with a
 * '/' is assumed to refer to a directory. Otherwise, the URL is assumed to
 * refer to a JAR file which will be opened as needed.
 * <p>
 * The AccessControlContext of the thread that created the instance of
 * URLClassLoader will be used when subsequently loading classes and resources.
 * <p>
 * The classes that are loaded are by default granted permission only to access
 * the URLs specified when the URLClassLoader was created.
 * 
 * @author David Connelly
 * @since 1.2
 */
public class _URLClassLoader extends SecureClassLoader {
    /**
     * Logger for this class
     */
    private static Logger logger = LoggerFactory.getLogger(_URLClassLoader.class);

    /* The search path for classes and resources */
    URLClassPath ucp;

    /* The context to be used when loading classes and resources */
    private AccessControlContext acc;

    /**
     * Constructs a new URLClassLoader for the given URLs. The URLs will be
     * searched in the order specified for classes and resources after first
     * searching in the specified parent class loader. Any URL that ends with a
     * '/' is assumed to refer to a directory. Otherwise, the URL is assumed to
     * refer to a JAR file which will be downloaded and opened as needed.
     * 
     * <p>
     * If there is a security manager, this method first calls the security
     * manager's <code>checkCreateClassLoader</code> method to ensure creation
     * of a class loader is allowed.
     * 
     * @param urls
     *            the URLs from which to load classes and resources
     * @param parent
     *            the parent class loader for delegation
     * @exception SecurityException
     *                if a security manager exists and its
     *                <code>checkCreateClassLoader</code> method doesn't allow
     *                creation of a class loader.
     * @see SecurityManager#checkCreateClassLoader
     */
    public _URLClassLoader(URL[] urls, ClassLoader parent) {
        super(parent);
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        ucp = new URLClassPath(urls);
        acc = AccessController.getContext();
    }

    /**
     * Constructs a new URLClassLoader for the specified URLs using the default
     * delegation parent <code>ClassLoader</code>. The URLs will be searched in
     * the order specified for classes and resources after first searching in
     * the parent class loader. Any URL that ends with a '/' is assumed to refer
     * to a directory. Otherwise, the URL is assumed to refer to a JAR file
     * which will be downloaded and opened as needed.
     * 
     * <p>
     * If there is a security manager, this method first calls the security
     * manager's <code>checkCreateClassLoader</code> method to ensure creation
     * of a class loader is allowed.
     * 
     * @param urls
     *            the URLs from which to load classes and resources
     * 
     * @exception SecurityException
     *                if a security manager exists and its
     *                <code>checkCreateClassLoader</code> method doesn't allow
     *                creation of a class loader.
     * @see SecurityManager#checkCreateClassLoader
     */
    public _URLClassLoader(URL[] urls) {
        super();
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        ucp = new URLClassPath(urls);
        acc = AccessController.getContext();
    }

    /**
     * Constructs a new URLClassLoader for the specified URLs, parent class
     * loader, and URLStreamHandlerFactory. The parent argument will be used as
     * the parent class loader for delegation. The factory argument will be used
     * as the stream handler factory to obtain protocol handlers when creating
     * new jar URLs.
     * 
     * <p>
     * If there is a security manager, this method first calls the security
     * manager's <code>checkCreateClassLoader</code> method to ensure creation
     * of a class loader is allowed.
     * 
     * @param urls
     *            the URLs from which to load classes and resources
     * @param parent
     *            the parent class loader for delegation
     * @param factory
     *            the URLStreamHandlerFactory to use when creating URLs
     * 
     * @exception SecurityException
     *                if a security manager exists and its
     *                <code>checkCreateClassLoader</code> method doesn't allow
     *                creation of a class loader.
     * @see SecurityManager#checkCreateClassLoader
     */
    public _URLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(parent);
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        ucp = new URLClassPath(urls, factory);
        acc = AccessController.getContext();
    }

    /**
     * Appends the specified URL to the list of URLs to search for classes and
     * resources.
     * 
     * @param url
     *            the URL to be added to the search path of URLs
     */
    protected void addURL(URL url) {
        if (logger.isDebugEnabled()) {
            logger.debug("addURL(URL) - start");
        }

        ucp.addURL(url);

        if (logger.isDebugEnabled()) {
            logger.debug("addURL(URL) - end");
        }
    }

    /**
     * Returns the search path of URLs for loading classes and resources. This
     * includes the original list of URLs specified to the constructor, along
     * with any URLs subsequently appended by the addURL() method.
     * 
     * @return the search path of URLs for loading classes and resources.
     */
    public URL[] getURLs() {
        if (logger.isDebugEnabled()) {
            logger.debug("getURLs() - start");
        }

        URL[] returnURLArray = ucp.getURLs();
        if (logger.isDebugEnabled()) {
            logger.debug("getURLs() - end");
        }
        return returnURLArray;
    }

    /**
     * Finds and loads the class with the specified name from the URL search
     * path. Any URLs referring to JAR files are loaded and opened as needed
     * until the class is found.
     * 
     * @param name
     *            the name of the class
     * @return the resulting class
     * @exception ClassNotFoundException
     *                if the class could not be found
     */
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (logger.isDebugEnabled()) {
            logger.debug("findClass(String) - start");
        }

        logger.debug("current findClass = [{}]", name);

        try {
            Class<?> returnClass = (Class) AccessController.<Class> doPrivileged(new PrivilegedExceptionAction<Class>() {
                public Class run() throws ClassNotFoundException {
                    if (logger.isDebugEnabled()) {
                        logger.debug("run() - start");
                    }

                    String path = name.replace('.', '/').concat(".class");
                    Resource res = ucp.getResource(path, false);
                    if (res != null) {
                        try {
                            Object returnObject = defineClass(name, res);
                            if (logger.isDebugEnabled()) {
                                logger.debug("run() - end");
                            }
                            return (Class) returnObject;
                        } catch (IOException e) {
                            logger.error("run()", e);
                            logger.error("do throw execption1 = " + e.getMessage());
                            System.err.println("do throw execption1 = " + e.getMessage());
                            throw new ClassNotFoundException(name, e);
                        }
                    } else {
                        logger.error("do throw execption2 = " + name);
                        System.err.println("do throw execption2 = " + name);
                        return Class.forName(name);
                        //                        throw new ClassNotFoundException(name);
                    }
                }
            }, acc);
            if (logger.isDebugEnabled()) {
                logger.debug("findClass(String) - end");
            }
            return returnClass;
        } catch (java.security.PrivilegedActionException pae) {
            logger.error("findClass(String)", pae);

            throw (ClassNotFoundException) pae.getException();
        }
    }

    /*
     * Defines a Class using the class bytes obtained from the specified
     * Resource. The resulting Class must be resolved before it can be used.
     */
    private Class defineClass(String name, Resource res) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("defineClass(String, Resource) - start");
        }

        int i = name.lastIndexOf('.');
        URL url = res.getCodeSourceURL();
        if (i != -1) {
            String pkgname = name.substring(0, i);
            // Check if package already loaded.
            Package pkg = getPackage(pkgname);
            Manifest man = res.getManifest();
            if (pkg != null) {
                // Package found, so check package sealing.
                if (pkg.isSealed()) {
                    // Verify that code source URL is the same.
                    if (!pkg.isSealed(url)) {
                        throw new SecurityException("sealing violation: package " + pkgname + " is sealed");
                    }

                } else {
                    // Make sure we are not attempting to seal the package
                    // at this code source URL.
                    if ((man != null) && isSealed(pkgname, man)) {
                        throw new SecurityException("sealing violation: can't seal package " + pkgname + ": already loaded");
                    }
                }
            } else {
                if (man != null) {
                    definePackage(pkgname, man, url);
                } else {
                    definePackage(pkgname, null, null, null, null, null, null, null);
                }
            }
        }
        // Now read the class bytes and define the class
        java.nio.ByteBuffer bb = res.getByteBuffer();
        if (bb != null) {
            // Use (direct) ByteBuffer:
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            Class returnClass = defineClass(name, bb, cs);
            if (logger.isDebugEnabled()) {
                logger.debug("defineClass(String, Resource) - end");
            }
            return returnClass;
        } else {
            byte[] b = res.getBytes();
            // must read certificates AFTER reading bytes.
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            Class returnClass = defineClass(name, b, 0, b.length, cs);
            if (logger.isDebugEnabled()) {
                logger.debug("defineClass(String, Resource) - end");
            }
            return returnClass;
        }
    }

    /**
     * Defines a new package by name in this ClassLoader. The attributes
     * contained in the specified Manifest will be used to obtain package
     * version and sealing information. For sealed packages, the additional URL
     * specifies the code source URL from which the package was loaded.
     * 
     * @param name
     *            the package name
     * @param man
     *            the Manifest containing package version and sealing
     *            information
     * @param url
     *            the code source url for the package, or null if none
     * @exception IllegalArgumentException
     *                if the package name duplicates an existing package either
     *                in this class loader or one of its ancestors
     * @return the newly defined Package object
     */
    protected Package definePackage(String name, Manifest man, URL url) throws IllegalArgumentException {
        if (logger.isDebugEnabled()) {
            logger.debug("definePackage(String, Manifest, URL) - start");
        }

        String path = name.replace('.', '/').concat("/");
        String specTitle = null, specVersion = null, specVendor = null;
        String implTitle = null, implVersion = null, implVendor = null;
        String sealed = null;
        URL sealBase = null;

        Attributes attr = man.getAttributes(path);
        if (attr != null) {
            specTitle = attr.getValue(Name.SPECIFICATION_TITLE);
            specVersion = attr.getValue(Name.SPECIFICATION_VERSION);
            specVendor = attr.getValue(Name.SPECIFICATION_VENDOR);
            implTitle = attr.getValue(Name.IMPLEMENTATION_TITLE);
            implVersion = attr.getValue(Name.IMPLEMENTATION_VERSION);
            implVendor = attr.getValue(Name.IMPLEMENTATION_VENDOR);
            sealed = attr.getValue(Name.SEALED);
        }
        attr = man.getMainAttributes();
        if (attr != null) {
            if (specTitle == null) {
                specTitle = attr.getValue(Name.SPECIFICATION_TITLE);
            }
            if (specVersion == null) {
                specVersion = attr.getValue(Name.SPECIFICATION_VERSION);
            }
            if (specVendor == null) {
                specVendor = attr.getValue(Name.SPECIFICATION_VENDOR);
            }
            if (implTitle == null) {
                implTitle = attr.getValue(Name.IMPLEMENTATION_TITLE);
            }
            if (implVersion == null) {
                implVersion = attr.getValue(Name.IMPLEMENTATION_VERSION);
            }
            if (implVendor == null) {
                implVendor = attr.getValue(Name.IMPLEMENTATION_VENDOR);
            }
            if (sealed == null) {
                sealed = attr.getValue(Name.SEALED);
            }
        }
        if ("true".equalsIgnoreCase(sealed)) {
            sealBase = url;
        }
        Package returnPackage = definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
        if (logger.isDebugEnabled()) {
            logger.debug("definePackage(String, Manifest, URL) - end");
        }
        return returnPackage;
    }

    /*
     * Returns true if the specified package name is sealed according to the
     * given manifest.
     */
    private boolean isSealed(String name, Manifest man) {
        if (logger.isDebugEnabled()) {
            logger.debug("isSealed(String, Manifest) - start");
        }

        String path = name.replace('.', '/').concat("/");
        Attributes attr = man.getAttributes(path);
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Name.SEALED);
            }
        }
        boolean returnboolean = "true".equalsIgnoreCase(sealed);
        if (logger.isDebugEnabled()) {
            logger.debug("isSealed(String, Manifest) - end");
        }
        return returnboolean;
    }

    /**
     * Finds the resource with the specified name on the URL search path.
     * 
     * @param name
     *            the name of the resource
     * @return a <code>URL</code> for the resource, or <code>null</code> if the
     *         resource could not be found.
     */
    public URL findResource(final String name) {
        if (logger.isDebugEnabled()) {
            logger.debug("findResource(String) - start");
        }

        /*
         * The same restriction to finding classes applies to resources
         */
        URL url = (URL) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                if (logger.isDebugEnabled()) {
                    logger.debug("run() - start");
                }

                Object returnObject = ucp.findResource(name, true);
                if (logger.isDebugEnabled()) {
                    logger.debug("run() - end");
                }
                return returnObject;
            }
        }, acc);

        URL returnURL = url != null ? ucp.checkURL(url) : null;
        if (logger.isDebugEnabled()) {
            logger.debug("findResource(String) - end");
        }
        return returnURL;
    }

    /**
     * Returns an Enumeration of URLs representing all of the resources on the
     * URL search path having the specified name.
     * 
     * @param name
     *            the resource name
     * @exception IOException
     *                if an I/O exception occurs
     * @return an <code>Enumeration</code> of <code>URL</code>s
     */
    public Enumeration<URL> findResources(final String name) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("findResources(String) - start");
        }

        final Enumeration e = ucp.findResources(name, true);

        Enumeration<URL> returnEnumeration = new Enumeration<URL>() {
            private URL url = null;

            private boolean next() {
                if (logger.isDebugEnabled()) {
                    logger.debug("next() - start");
                }

                if (url != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("next() - end");
                    }
                    return true;
                }
                do {
                    URL u = (URL) AccessController.doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            if (logger.isDebugEnabled()) {
                                logger.debug("run() - start");
                            }

                            if (!e.hasMoreElements())
                                return null;
                            Object returnObject = e.nextElement();
                            if (logger.isDebugEnabled()) {
                                logger.debug("run() - end");
                            }
                            return returnObject;
                        }
                    }, acc);
                    if (u == null)
                        break;
                    url = ucp.checkURL(u);
                } while (url == null);
                boolean returnboolean = url != null;
                if (logger.isDebugEnabled()) {
                    logger.debug("next() - end");
                }
                return returnboolean;
            }

            public URL nextElement() {
                if (logger.isDebugEnabled()) {
                    logger.debug("nextElement() - start");
                }

                if (!next()) {
                    throw new NoSuchElementException();
                }
                URL u = url;
                url = null;

                if (logger.isDebugEnabled()) {
                    logger.debug("nextElement() - end");
                }
                return u;
            }

            public boolean hasMoreElements() {
                if (logger.isDebugEnabled()) {
                    logger.debug("hasMoreElements() - start");
                }

                boolean returnboolean = next();
                if (logger.isDebugEnabled()) {
                    logger.debug("hasMoreElements() - end");
                }
                return returnboolean;
            }
        };
        if (logger.isDebugEnabled()) {
            logger.debug("findResources(String) - end");
        }
        return returnEnumeration;
    }

    /**
     * Returns the permissions for the given codesource object. The
     * implementation of this method first calls super.getPermissions and then
     * adds permissions based on the URL of the codesource.
     * <p>
     * If the protocol of this URL is "jar", then the permission granted is
     * based on the permission that is required by the URL of the Jar file.
     * <p>
     * If the protocol is "file" and the path specifies a file, then permission
     * to read that file is granted. If protocol is "file" and the path is a
     * directory, permission is granted to read all files and (recursively) all
     * files and subdirectories contained in that directory.
     * <p>
     * If the protocol is not "file", then permission to connect to and accept
     * connections from the URL's host is granted.
     * 
     * @param codesource
     *            the codesource
     * @return the permissions granted to the codesource
     */
    protected PermissionCollection getPermissions(CodeSource codesource) {
        if (logger.isDebugEnabled()) {
            logger.debug("getPermissions(CodeSource) - start");
        }

        PermissionCollection perms = super.getPermissions(codesource);

        URL url = codesource.getLocation();

        Permission p;
        URLConnection urlConnection;

        try {
            urlConnection = url.openConnection();
            p = urlConnection.getPermission();
        } catch (java.io.IOException ioe) {
            logger.error("getPermissions(CodeSource)", ioe);

            p = null;
            urlConnection = null;
        }

        if (p instanceof FilePermission) {
            // if the permission has a separator char on the end,
            // it means the codebase is a directory, and we need
            // to add an additional permission to read recursively
            String path = p.getName();
            if (path.endsWith(File.separator)) {
                path += "-";
                p = new FilePermission(path, SecurityConstants.FILE_READ_ACTION);
            }
        } else if ((p == null) && (url.getProtocol().equals("file"))) {
            String path = url.getFile().replace('/', File.separatorChar);
            path = ParseUtil.decode(path);
            if (path.endsWith(File.separator))
                path += "-";
            p = new FilePermission(path, SecurityConstants.FILE_READ_ACTION);
        } else {
            /**
             * Not loading from a 'file:' URL so we want to give the class
             * permission to connect to and accept from the remote host after
             * we've made sure the host is the correct one and is valid.
             */
            URL locUrl = url;
            if (urlConnection instanceof JarURLConnection) {
                locUrl = ((JarURLConnection) urlConnection).getJarFileURL();
            }
            String host = locUrl.getHost();
            if (host != null && (host.length() > 0))
                p = new SocketPermission(host, SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION);
        }

        // make sure the person that created this class loader
        // would have this permission

        if (p != null) {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                final Permission fp = p;
                AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() throws SecurityException {
                        if (logger.isDebugEnabled()) {
                            logger.debug("run() - start");
                        }

                        sm.checkPermission(fp);

                        if (logger.isDebugEnabled()) {
                            logger.debug("run() - end");
                        }
                        return null;
                    }
                }, acc);
            }
            perms.add(p);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getPermissions(CodeSource) - end");
        }
        return perms;
    }

    /**
     * Creates a new instance of URLClassLoader for the specified URLs and
     * parent class loader. If a security manager is installed, the
     * <code>loadClass</code> method of the URLClassLoader returned by this
     * method will invoke the <code>SecurityManager.checkPackageAccess</code>
     * method before loading the class.
     * 
     * @param urls
     *            the URLs to search for classes and resources
     * @param parent
     *            the parent class loader for delegation
     * @return the resulting class loader
     */
    public static _URLClassLoader newInstance(final URL[] urls, final ClassLoader parent) {
        if (logger.isDebugEnabled()) {
            logger.debug("newInstance(URL[], ClassLoader) - start");
        }

        // Save the caller's context
        AccessControlContext acc = AccessController.getContext();
        // Need a privileged block to create the class loader
        _URLClassLoader ucl = (_URLClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                if (logger.isDebugEnabled()) {
                    logger.debug("run() - start");
                }

                Object returnObject = new _FactoryURLClassLoader(urls, parent);
                if (logger.isDebugEnabled()) {
                    logger.debug("run() - end");
                }
                return returnObject;
            }
        });
        // Now set the context on the loader using the one we saved,
        // not the one inside the privileged block...
        ucl.acc = acc;

        if (logger.isDebugEnabled()) {
            logger.debug("newInstance(URL[], ClassLoader) - end");
        }
        return ucl;
    }

    /**
     * Creates a new instance of URLClassLoader for the specified URLs and
     * default parent class loader. If a security manager is installed, the
     * <code>loadClass</code> method of the URLClassLoader returned by this
     * method will invoke the <code>SecurityManager.checkPackageAccess</code>
     * before loading the class.
     * 
     * @param urls
     *            the URLs to search for classes and resources
     * @return the resulting class loader
     */
    public static _URLClassLoader newInstance(final URL[] urls) {
        if (logger.isDebugEnabled()) {
            logger.debug("newInstance(URL[]) - start");
        }

        // Save the caller's context
        AccessControlContext acc = AccessController.getContext();
        // Need a privileged block to create the class loader
        _URLClassLoader ucl = (_URLClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                if (logger.isDebugEnabled()) {
                    logger.debug("run() - start");
                }

                Object returnObject = new _FactoryURLClassLoader(urls);
                if (logger.isDebugEnabled()) {
                    logger.debug("run() - end");
                }
                return returnObject;
            }
        });

        // Now set the context on the loader using the one we saved,
        // not the one inside the privileged block...
        ucl.acc = acc;

        if (logger.isDebugEnabled()) {
            logger.debug("newInstance(URL[]) - end");
        }
        return ucl;
    }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 我加的
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        logger.debug("# loadClass ...");
        Class<?> c = findLoadedClass(name);
        logger.debug("loadClass.name = [{}]", name);
        logger.debug("1.c = [{}]", c);
        if (c == null) {
            c = findClass(name);
            logger.debug("2.c = [{}]", c);
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    //    protected synchronized Class<?> loadClass(String name, boolean resolve)
    //            throws ClassNotFoundException
    //        {
    //            // First, check if the class has already been loaded
    //            Class c = findLoadedClass(name);
    //            if (c == null) {
    //                try {
    //                    if (parent != null) {
    //                        c = parent.loadClass(name, false);
    //                    } else {
    //                        c = findBootstrapClass0(name);
    //                    }
    //                } catch (ClassNotFoundException e) {
    //                    // If still not found, then invoke findClass in order
    //                    // to find the class.
    //                    c = findClass(name);
    //                }
    //            }
    //            if (resolve) {
    //                resolveClass(c);
    //            }
    //            return c;
    //        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    static {
        //因為有錯所以拿掉
        //        sun.misc.SharedSecrets.setJavaNetAccess(new sun.misc.JavaNetAccess() {
        //            public URLClassPath getURLClassPath(URLClassLoader u) {
        //                if (logger.isDebugEnabled()) {
        //                    logger.debug("getURLClassPath(URLClassLoader) - start");
        //                }
        //
        //                if (logger.isDebugEnabled()) {
        //                    logger.debug("getURLClassPath(URLClassLoader) - end");
        //                }
        //                return u.ucp;
        //            }
        //        });
    }
}

final class _FactoryURLClassLoader extends java_.net._URLClassLoader {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(_FactoryURLClassLoader.class);

    _FactoryURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    _FactoryURLClassLoader(URL[] urls) {
        super(urls);
    }

    public final synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (logger.isDebugEnabled()) {
            logger.debug("loadClass(String, boolean) - start");
        }

        logger.debug("name = [{}]", name);
        logger.debug("resolve = [{}]", resolve);
        // First check if we have permission to access the package. This
        // should go away once we've added support for exported packages.
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            int i = name.lastIndexOf('.');
            if (i != -1) {
                sm.checkPackageAccess(name.substring(0, i));
            }
        }
        Class returnClass = super.loadClass(name, resolve);
        if (logger.isDebugEnabled()) {
            logger.debug("loadClass(String, boolean) - end");
        }
        return returnClass;
    }
}
