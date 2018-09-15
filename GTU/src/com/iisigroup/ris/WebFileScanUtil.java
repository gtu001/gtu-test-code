/*
 * Copyright (c) 2010-2020 IISI. All rights reserved.
 * 
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.ris;

import gtu.file.FileUtil;
import gtu.reflect.ToStringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.qos.logback.classic.Level;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * 可直接選擇要連結的頁面
 */
// @RequestScoped
//##########################
//xmlns:c="http://java.sun.com/jsp/jstl/core"
//        <h:panelGroup rendered="true">
//        <ul id="treemenu_test" class="treeview">
//            <li><div>
//                    <a><img />新增異動</a>
//                </div>
//                <ul>
//                    <c:forEach var="page" items="#{webFileScan.svnWebPages2}">
//                    <li><a href="#{request.contextPath}/faces#{page[0]}">#{page[1]}</a></li>
//                    </c:forEach>
//                </ul>
//            </li>
//            <li><div>
//                    <a><img />我上傳</a>
//                </div>
//                <ul>
//                    <c:forEach var="page" items="#{webFileScan.svnWebPages2_myWork}">
//                    <li><a href="#{request.contextPath}/faces#{page[0]}">#{page[1]}</a></li>
//                    </c:forEach>
//                </ul>
//            </li>
//        </ul>
//        <script type="text/javascript">
//            simpleTreeMenu.createTree("treemenu_test", true);
//        </script>
//        </h:panelGroup>
//##########################
@SessionScoped
@Scope("session")
@Controller(value = "webFileScan")
public class WebFileScanUtil implements Serializable {

    private static final String[] SOURCE_DIR = Config.SRC_DIR;
    private static final long RECENT_TIME = Config.RECENT_TIME;
    private static final String SVN_ACCOUNT = Config.SVN_ACCOUNT;

    private static final long serialVersionUID = 1L;

    private static transient Logger log = LoggerFactory.getLogger(WebFileScanUtil.class);

    private static final String[] IGNORE_FILE_ENDS_WITH = (String[]) ArrayUtils.addAll(new String[] { ".svn-base" }, Config.IGNORE_FILE_ENDS_WITH);

    private static Map<String, Object> props;

    private ThreadGroup scanThreadGroup;

    static boolean DEBUG_MODE = false;
    static String DEBUG_WEBROOTDIR = "C:/apps/@server/apache-tomcat-7.0.26_self/webapps/ris3rl2";

    private WebFileScanUtil() {
        fullWebPages = new ArrayList<PageInfo>();
        srcFiles = new ArrayList<SourceCodeFile>();
    }

    static {
        ((ch.qos.logback.classic.Logger) log).setLevel(Level.TRACE);
        props = new HashMap<String, Object>();
    }

    /** 掃到所有的頁面放在這 */
    private List<PageInfo> fullWebPages;
    private List<Object[]> fullWebPages2;

    /** 所有source code放這 */
    private List<SourceCodeFile> srcFiles;
    /** svn 的檔案 */
    private List<File> svnNewOrModifyFileList;
    private List<File> svnNewOrModifyFileList_myWork;
    private List<PageInfo> svnWebPages;
    private List<PageInfo> svnWebPages_myWork;
    private List<PageInfo> recentModifys;
    private List<Object[]> svnWebPages2;
    private List<Object[]> svnWebPages2_myWork;
    private List<Object[]> recentModifys2;

    private String redirect;

    private static File webRootDir;

    enum ScanThreadManager {
        SEARCH_WORK_SPACE() {
            Thread get(final WebFileScanUtil webFile) {
                Thread searchWorkSpace = new Thread(webFile.scanThreadGroup, new Runnable() {
                    public void run() {
                        log.debug("{} start!!", this);
                        long start = System.currentTimeMillis();
                        for (String srcDirPath : SOURCE_DIR) {
                            scanSrcDir(new File(srcDirPath), webFile.srcFiles);
                        }

                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!source code scan is ready!!!");
                            log.warn("!!!scan during = " + (System.currentTimeMillis() - start));
                            log.warn("!!!size [{}]", webFile.srcFiles.size());
                            log.warn("###########################################");
                        }
                    }

                    void scanSrcDir(File file, List<SourceCodeFile> srcFiles) {
                        if (!file.exists()) {
                            return;
                        }
                        if (file.isDirectory()) {
                            for (File f : file.listFiles()) {
                                scanSrcDir(f, srcFiles);
                            }
                        } else {
                            if (isIgnoreScanFile(file)) {
                                return;
                            }
                            srcFiles.add(new SourceCodeFile(file));
                        }
                    }
                }, this.toString());
                searchWorkSpace.setDaemon(true);
                return searchWorkSpace;
            }
        },
        SEARCH_WEB_FILES() {
            Thread get(final WebFileScanUtil webFile) {
                Thread searchWebFiles = new Thread(webFile.scanThreadGroup, new Runnable() {
                    public void run() {
                        log.debug("{} start!!", this);
                        File root = webFile.getWebRootDir();
                        for (;;) {
                            if (root != null && root.exists()) {
                                break;
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                log.error(e.getMessage());
                            }
                        }

                        long start = System.currentTimeMillis();
                        filterDir(root);

                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!webapp scan is ready!!!");
                            log.warn("!!!scan during = " + (System.currentTimeMillis() - start));
                            log.warn("!!!size [{}]", webFile.fullWebPages.size());
                            log.warn("###########################################");
                        }
                    }

                    void filterDir(File file) {
                        if (file.isDirectory()) {
                            File[] subFile = file.listFiles();
                            for (int i = 0; i < subFile.length; i++) {
                                filterDir(subFile[i]);
                            }
                        } else {
                            if (isIgnoreScanFile(file)) {
                                return;
                            }
                            this.filterFile(file);
                        }
                    }

                    void filterFile(File file) {
                        String prefix = "\\";
                        // log.trace("prefix = " + prefix);
                        String replacePrefix = webFile.getWebRootDir() + File.separator;
                        // log.trace("replacePrefix = " + replacePrefix);

                        String path = file.getAbsolutePath();
                        // log.trace("path = " + path);
                        path = StringUtils.replace(path, replacePrefix, prefix);
                        String orignFullPath = path.toString();
                        path = StringUtils.replace(path, "\\", "/");

                        // log.trace("OK path = " + path);
                        String xhtmlName = file.getName();

                        PageInfo pageInfo = new PageInfo();
                        pageInfo.setFullPath(path);
                        pageInfo.setFileName(xhtmlName);
                        pageInfo.setUnfixFullPath(orignFullPath);
                        pageInfo.orignWebFile = file.getAbsoluteFile();
                        // log.trace("add : " + pageInfo);
                        webFile.fullWebPages.add(pageInfo);
                    }
                }, this.toString());
                searchWebFiles.setDaemon(true);
                return searchWebFiles;
            }
        },
        SEARCH_SVN_MODIFY_FILES() {
            Thread get(final WebFileScanUtil webFile) {
                Thread svnModfiyThread = new Thread(webFile.scanThreadGroup, new Runnable() {
                    Pattern svnPattern = Pattern.compile("^([\\?|M])\\s+\\d*\\s+(.+)$");

                    public void run() {
                        log.debug("{} start!!", this);
                        long start = System.currentTimeMillis();

                        webFile.svnNewOrModifyFileList = new ArrayList<File>();
                        for (String srcDirPath : SOURCE_DIR) {
                            try {
                                scanSrcDir(new File(srcDirPath));
                            } catch (IOException e) {
                                log.error("searchSvnModifyFiles", e);
                            }
                        }

                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!SVN scan is ready!!!");
                            log.warn("!!!scan during = " + (System.currentTimeMillis() - start));
                            log.warn("!!!size [{}]", webFile.svnNewOrModifyFileList.size());
                            log.warn("###########################################");
                        }
                    }

                    void scanSrcDir(File svnDir) throws IOException {
                        Process process = Runtime.getRuntime().exec(String.format("svn status -u \"%s\"", svnDir));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        Matcher matcher = null;
                        File file = null;
                        List<File> scanList = new ArrayList<File>();
                        for (String line = null; (line = reader.readLine()) != null;) {
                            matcher = svnPattern.matcher(line);
                            if (matcher.find()) {
                                file = new File(matcher.group(2));
                                if (file.isFile()) {
                                    webFile.svnNewOrModifyFileList.add(file);
                                }
                                if (file.isDirectory() && matcher.group(1).equals("?")) {
                                    scanList.clear();
                                    FileUtil.searchFileMatchs(file, ".*", scanList);
                                    for (File f : scanList) {
                                        if (!webFile.svnNewOrModifyFileList.contains(f)) {
                                            webFile.svnNewOrModifyFileList.add(f);
                                        }
                                    }
                                }
                            } else {
//                                System.out.println("ignore : [" + line + "]");
                            }
                        }
                        reader.close();
                    }
                }, this.toString());
                svnModfiyThread.setDaemon(true);
                return svnModfiyThread;
            }
        },
        SEARCH_SVN_MODIFY_FILES_CHECK_MY_WORK() {
            Thread get(final WebFileScanUtil webFile) {
                Thread svnModfiyThread_checkMyWork = new Thread(webFile.scanThreadGroup, new Runnable() {
                    Pattern svnPattern = Pattern.compile("^([\\?|M]?)\\s+\\d*\\s*\\d*\\s(\\w+)\\s+(.+)$");
                    public void run() {
                        log.debug("{} start!!", this);
                        long start = System.currentTimeMillis();

                        webFile.svnNewOrModifyFileList_myWork = new ArrayList<File>();
                        for (String srcDirPath : SOURCE_DIR) {
                            try {
                                scanSrcDir(new File(srcDirPath));
                            } catch (IOException e) {
                                log.error("searchSvnModifyFiles", e);
                            }
                        }

                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!SVN MyWork scan is ready!!!");
                            log.warn("!!!scan during = " + (System.currentTimeMillis() - start));
                            log.warn("!!!size [{}]", webFile.svnNewOrModifyFileList.size());
                            log.warn("###########################################");
                        }
                    }

                    void scanSrcDir(File svnDir) throws IOException {
                        Process process = Runtime.getRuntime().exec(String.format("svn status -v \"%s\"", svnDir));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        Matcher matcher = null;
                        File file = null;
                        List<File> scanList = new ArrayList<File>();
                        for (String line = null; (line = reader.readLine()) != null;) {
                            matcher = svnPattern.matcher(line);
                            if (matcher.find() && matcher.group(2).equals(SVN_ACCOUNT)) {
                                file = new File(matcher.group(3));
                                if (file.isFile()) {
                                    webFile.svnNewOrModifyFileList_myWork.add(file);
                                }
                                if (file.isDirectory() && matcher.group(1).equals("?")) {
                                    scanList.clear();
                                    FileUtil.searchFileMatchs(file, ".*", scanList);
                                    for (File f : scanList) {
                                        if (!webFile.svnNewOrModifyFileList_myWork.contains(f)) {
                                            webFile.svnNewOrModifyFileList_myWork.add(f);
                                        }
                                    }
                                }
                            } else {
//                                System.out.println("ignore : [" + line + "]");
                            }
                        }
                        reader.close();
                    }
                }, this.toString());
                svnModfiyThread_checkMyWork.setDaemon(true);
                return svnModfiyThread_checkMyWork;
            }
        },
        ;

        abstract Thread get(WebFileScanUtil webFile);
    }

    enum MergeThreadManager {
        RECENT_MODIFY_FILES() {
            Thread get(final WebFileScanUtil webFile) {
                Thread recentModifyThread = new Thread(webFile.scanThreadGroup, new Runnable() {
                    public void run() {
                        long recentTime = System.currentTimeMillis() - RECENT_TIME; 
                        
                        webFile.recentModifys = new ArrayList<PageInfo>();
                        for (PageInfo page : webFile.fullWebPages) {
                            if (page.sourceFile == null) {
                                continue;
                            }
                            if (page.sourceFile.lastModified() > recentTime) {
                                webFile.recentModifys.add(page);
                            }
                        }
                        webFile.recentModifys2 = getPageInfoToObjectAry(webFile.recentModifys);

                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!recent modify files merge completed!!! = {}", webFile.recentModifys.size());
                            log.warn("###########################################");
                        }
                    }
                }, this.toString());
                recentModifyThread.setDaemon(true);
                return recentModifyThread;
            }
        },
        WEB_FILE_AND_SOURCE_FILES() {
            Thread get(final WebFileScanUtil webFile) {
                Thread webFileAndSourceThred = new Thread(webFile.scanThreadGroup, new Runnable() {
                    public void run() {
                        //marge source
                        for (PageInfo pageInfo : webFile.fullWebPages) {
                            String orignFullPath = pageInfo.unfixFullPath;
                            if (orignFullPath.endsWith(".class")) {
                                // /WEB-INF/classes/com/iisigroup/ris/rr/domain/Rr03860DTO.class
                                // log.trace("1 scan java : [{}]", new Object[] {
                                // orignFullPath });
                                orignFullPath = orignFullPath.replaceAll(Pattern.quote("\\WEB-INF\\classes\\"), "");
                                // log.trace("2 scan java : [{}]", new Object[] {
                                // orignFullPath });
                                orignFullPath = orignFullPath.replaceAll(".class", ".java");
                                // log.trace("3 scan java : [{}]", new Object[] {
                                // orignFullPath });
                            }
                            if (orignFullPath.endsWith(".jar")) {
                                continue;
                            }
                            boolean findSrc = false;
                            for (SourceCodeFile sf : webFile.srcFiles) {
                                if (sf.absPath.indexOf(orignFullPath) != -1) {
                                    pageInfo.sourceFile = sf.file;
                                    findSrc = true;
                                    break;
                                }
                            }
                            if (!findSrc) {
                                log.warn("Source not found!! ==> sarch[{}], pageInfo.sourceFile[{}]", new Object[] { orignFullPath, pageInfo.sourceFile });
                            }
                        }
                        webFile.fullWebPages2 = getPageInfoToObjectAry(webFile.fullWebPages);
                        
                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!web and source files merge completed!!! = {}", webFile.fullWebPages.size());
                            log.warn("###########################################");
                        }
                    }
                }, this.toString());
                webFileAndSourceThred.setDaemon(true);
                return webFileAndSourceThred;
            }
        },
        SVNFILE_NEW_OR_MODIFY() {
            Thread get(final WebFileScanUtil webFile) {
                Thread svnThread = new Thread(webFile.scanThreadGroup, new Runnable() {
                    public void run() {
                        // marge svn file
                        webFile.svnWebPages = new ArrayList<PageInfo>();
                        for (File svnFile : webFile.svnNewOrModifyFileList) {
                            boolean findSrc = false;
                            for (PageInfo page : webFile.fullWebPages) {
                                if (page.sourceFile != null && page.sourceFile.getAbsolutePath().equals(svnFile.getAbsolutePath())) {
                                    webFile.svnWebPages.add(page);
                                    findSrc = true;
                                    break;
                                }
                            }
                            if (!findSrc) {
//                                log.warn("SVN not found!! ==> [{}]", svnFile);
                            }
                        }
                        webFile.svnWebPages2 = getPageInfoToObjectAry(webFile.svnWebPages);
                        
                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!svn new or modify merge completed!!! = {}", webFile.svnWebPages.size());
                            log.warn("###########################################");
                        }
                    }
                }, this.toString());
                svnThread.setDaemon(true);
                return svnThread;
            }
        },
        SVNFILE_MYWORK() {
            Thread get(final WebFileScanUtil webFile) {
                Thread svnThread = new Thread(webFile.scanThreadGroup, new Runnable() {
                    public void run() {
                        // marge svn file (my work)
                        webFile.svnWebPages_myWork = new ArrayList<PageInfo>();
                        for (File svnFile : webFile.svnNewOrModifyFileList_myWork) {
                            boolean findSrc = false;
                            for (PageInfo page : webFile.fullWebPages) {
                                if (page.sourceFile != null && page.sourceFile.getAbsolutePath().equals(svnFile.getAbsolutePath())) {
                                    webFile.svnWebPages_myWork.add(page);
                                    findSrc = true;
                                    break;
                                }
                            }
                            if (!findSrc) {
//                                log.warn("SVN MyWork not found!! ==> [{}]", svnFile);
                            }
                        }
                        webFile.svnWebPages2_myWork = getPageInfoToObjectAry(webFile.svnWebPages_myWork);
                        
                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!svn file my work merge completed!!! = {}", webFile.svnWebPages_myWork.size());
                            log.warn("###########################################");
                        }
                    }
                }, this.toString());
                svnThread.setDaemon(true);
                return svnThread;
            }
        },
        ;

        List<Object[]> getPageInfoToObjectAry(List<PageInfo> from) {
            List<Object[]> list = Lists.transform(new ArrayList<PageInfo>(from), new Function<PageInfo, Object[]>() {
                public Object[] apply(PageInfo arg0) {
                    return new Object[] { arg0.fullPath, arg0.fileName };
                }
            });
            List<Object[]> list_ = new ArrayList<Object[]>(list);
            Collections.sort(list_, new Comparator<Object[]>() {
                public int compare(Object[] paramT1, Object[] paramT2) {
                    return ((String) paramT1[1]).compareTo((String) paramT2[1]);
                }
            });
            return list_;
        }

        abstract Thread get(WebFileScanUtil webFile);
    }

    @PostConstruct
    private void init() {
        log.trace("# init ...");

        scanThreadGroup = new ThreadGroup(Thread.currentThread().getThreadGroup(), WebFileScanUtil.class.getSimpleName() + "_ThreadGroup");
        log.debug("scanThreadGroup = {}", scanThreadGroup);

        final WebFileScanUtil webFile = this;

        log.debug("webFile = {}", webFile);

        Thread mainSearchThread = new Thread(scanThreadGroup, new Runnable() {
            Thread workSpace = ScanThreadManager.SEARCH_WORK_SPACE.get(webFile);
            Thread webFiles = ScanThreadManager.SEARCH_WEB_FILES.get(webFile);
            Thread svnFiles1 = ScanThreadManager.SEARCH_SVN_MODIFY_FILES.get(webFile);
            Thread svnFiles2 = ScanThreadManager.SEARCH_SVN_MODIFY_FILES_CHECK_MY_WORK.get(webFile);

            Thread mergeWebAndSource = MergeThreadManager.WEB_FILE_AND_SOURCE_FILES.get(webFile);
            Thread mergeNewOrModify = MergeThreadManager.SVNFILE_NEW_OR_MODIFY.get(webFile);
            Thread mergeMyWork = MergeThreadManager.SVNFILE_MYWORK.get(webFile);
            Thread mergeRecentModify = MergeThreadManager.RECENT_MODIFY_FILES.get(webFile);

            boolean isTerminated(Thread thread) {
                return thread.getState() == Thread.State.TERMINATED;
            }

            void startThread(Thread thread) {
                if (thread.getState() == Thread.State.NEW) {
                    thread.start();
                }
            }
            
            void releaseMemory() {
                if (DEBUG_MODE) {
                    return;
                }
                srcFiles = Collections.emptyList();
                svnNewOrModifyFileList = Collections.emptyList();
                svnNewOrModifyFileList_myWork = Collections.emptyList();
                System.gc();
            }

            public void run() {
                workSpace.start();
                webFiles.start();
                svnFiles1.start();
                svnFiles2.start();

                for (;;) {
                    if (isTerminated(workSpace) && isTerminated(webFiles)) {
                        startThread(mergeWebAndSource);
                    }

                    if (isTerminated(mergeWebAndSource)) {
                        startThread(mergeRecentModify);
                    }

                    if (isTerminated(mergeWebAndSource) && isTerminated(svnFiles1)) {
                        startThread(mergeNewOrModify);
                    }

                    if (isTerminated(mergeWebAndSource) && isTerminated(svnFiles2)) {
                        startThread(mergeMyWork);
                    }
                    
                    if (isTerminated(workSpace) && //
                            isTerminated(webFiles) && //
                            isTerminated(svnFiles1) && //
                            isTerminated(svnFiles2) &&  //
                            isTerminated(mergeWebAndSource)  && //
                            isTerminated(mergeNewOrModify)  && //
                            isTerminated(mergeMyWork)  && //
                            isTerminated(mergeRecentModify)  //
                    ) {
                        releaseMemory();
                        synchronized (log) {
                            log.warn("###########################################");
                            log.warn("!!!WEB FILE SCAN COMPLETED!!!");
                            log.warn("SOURCE_DIR = " + Arrays.toString(SOURCE_DIR));
                            log.warn("WEB_APP = " + getWebRootDir());
                            log.warn("fullWebPages = " + fullWebPages.size());
                            log.warn("srcFiles = " + srcFiles.size());
                            log.warn("svnNewOrModifyFileList = " + svnNewOrModifyFileList.size());
                            log.warn("svnNewOrModifyFileList_myWork = " + svnNewOrModifyFileList_myWork.size());
                            log.warn("svnWebPages = " + svnWebPages.size());
                            log.warn("svnWebPages_myWork = " + svnWebPages_myWork.size());
                            log.warn("recentModifys = " + recentModifys.size());
                            log.warn("###########################################");
                        }
                        // showFullWebPages();
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        log.debug("mainSearchThread sleep", e);
                    }
                }
            }

        }, "main search thread");
        mainSearchThread.setDaemon(true);
        mainSearchThread.start();
    }

    static void setHttpRequest(HttpServletRequest request) {
        for (Method m : request.getClass().getMethods()) {
            if (m.getParameterTypes().length == 0 && m.getReturnType() != void.class) {
                try {
                    props.put(HttpRequestKey + "." + m.getName(), m.invoke(request, new Object[0]));
                } catch (Exception e) {
                    log.error("setHttpRequest", e);
                }
            }
            if (m.getName().equals("getRealPath")) {
                try {
                    props.put(HttpRequestKey + "." + m.getName(), m.invoke(request, new Object[] { "" }));
                } catch (Exception e) {
                    log.error("setHttpRequest.getRealPath", e);
                }
            }
        }
        log.trace("config map : " + props);
        Validate.isTrue(props.containsKey(HttpRequestKey + ".getRealPath"));
        Validate.isTrue(props.containsKey(HttpRequestKey + ".getRequestURL"));
        Validate.isTrue(props.containsKey(HttpRequestKey + ".getContextPath"));
    }

    static String HttpRequestKey = HttpServletRequest.class.getSimpleName();

    enum PropsConfig {
        REAL_PATH(HttpRequestKey + ".getRealPath"), //
        REQUEST_URL(HttpRequestKey + ".getRequestURL"), //
        CONTEXT_PATH(HttpRequestKey + ".getContextPath"), //
        ;

        final String key;

        PropsConfig(String key) {
            this.key = key;
        }

        Object get() {
            return props.get(key);
        }
    }

    static boolean isIgnoreScanFile(File file) {
        for (String endsWith : IGNORE_FILE_ENDS_WITH) {
            if (file.getName().endsWith(endsWith)) {
                return true;
            }
        }
        return false;
    }

    private File getWebRootDir() {
        if (webRootDir != null) {
            return webRootDir;
        }
        logt("###########################################");
        logt("#             getWebRootDir               #");
        logt("###########################################");
        String realPath = (String) PropsConfig.REAL_PATH.get();
        File classPath = getWebRootByClassPath();
        logt("realPath = " + realPath);
        logt("classPath = " + classPath);
        if (StringUtils.isNotEmpty(realPath)) {
            File file = new File(realPath).getAbsoluteFile();
            if (file.exists() && file.isDirectory()) {
                webRootDir = file;
            }
            logt("!!WebRootFile success!! = get web realPath");
        }
        if (webRootDir == null && classPath != null) {
            webRootDir = classPath;
            logt("!!WebRootFile success!! = get web war file path");
        }
        if (webRootDir == null) {
            webRootDir = new File(DEBUG_WEBROOTDIR).getAbsoluteFile();
            logt("!!WebRootFile failed!! = use default");
        }
        logt("WebRootFile = " + webRootDir);
        return webRootDir;
    }

    File getWebRootByClassPath() {
        URL url = this.getClass().getResource(this.getClass().getSimpleName() + ".class");
        logt("ORIGN URL : " + url);
        if (url == null) {
            return null;
        }
        logt("protocal : " + url.getProtocol());
        logt("getFile : " + url.getFile());
        if (url.getProtocol().equals("jar")) {
            int pos = url.getFile().indexOf("WEB-INF");
            if (pos != -1) {
                try {
                    String subStr = URLDecoder.decode(url.getFile().substring(0, pos).replaceFirst("file:/", ""), "UTF8");
                    logt("#### fix file path = " + subStr);
                    File file = new File(subStr);
                    logt("file.exists = " + file.exists());
                    logt("file.isDirectory = " + file.isDirectory());
                    logt("file.isFile = " + file.isFile());
                    if (file.exists() && file.isDirectory()) {
                        return file;
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("utf8 error", e);
                }
            }
        }
        if (url.getProtocol().equals("zip")) {
            int pos = url.getFile().indexOf("WEB-INF");
            if (pos != -1) {
                try {
                    String subStr = URLDecoder.decode(url.getFile().substring(0, pos), "UTF8");
                    if (subStr.endsWith("/")) {
                        subStr = subStr.substring(0, subStr.length() - 1);
                    }
                    logt("#### fix file path = " + subStr);
                    File file = new File(subStr);
                    logt("file.exists = " + file.exists());
                    logt("file.isDirectory = " + file.isDirectory());
                    logt("file.isFile = " + file.isFile());
                    if (file.exists() && file.isDirectory()) {
                        return file;
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("utf8 error", e);
                }
            }
        }
        return null;
    }

    static class SourceCodeFile {
        String absPath;
        File file;

        SourceCodeFile(File file) {
            this.file = file;
            this.absPath = file.getAbsolutePath();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DEBUG_MODE = true;
        WebFileScanUtil xx = new WebFileScanUtil();
        xx.init();
        Thread[] tttt = new Thread[xx.scanThreadGroup.activeCount()];
        xx.scanThreadGroup.enumerate(tttt);
        for (boolean again = true; again;) {
            boolean doContinue = false;
            for (int ii = 0; ii < tttt.length; ii++) {
                if (tttt[ii].getState() != Thread.State.TERMINATED) {
                    System.out.println(ii);
                    doContinue = true;
                    break;
                }
            }
            again = doContinue;
            Thread.sleep(1000);
        }
        System.out.println("XX ALL DONE!!!!!");
        log.debug("done...");
    }

    /**
     * 開啟原始碼
     */
    public String openSource() {
        log.trace("# openSource ...");
        List<PageInfo> redirectCompleteList = queryPageInfo(redirect);
        if (redirectCompleteList != null) {
             gtu.swing.util.JFrameUtil.setVisible(true,WebFileScanUtilBrowserUI.getInstance().sourceFileList(redirectCompleteList));
        }
        return null;
    }

    /**
     * 導頁面的按鈕 action
     * 
     * @return
     */
    public String redirectPage() {
        log.trace("# redirectPage...");
        return redirect;
    }

    /**
     * 對應 autoComplete 的 method
     * 
     * @param query
     * @return
     */
    public List<String> redirectComplete(String query) {
        log.trace("# redirectComplete ... query = [{}]", query);
        List<String> results = new ArrayList<String>();
        for (PageInfo page : queryPageInfo(query)) {
            results.add(page.fullPath);
        }
        log.trace(" query count = " + results.size());
        return results;
    }

    private List<PageInfo> queryPageInfo(String query) {
        List<PageInfo> list = new ArrayList<PageInfo>();
        Pattern ptn = Pattern.compile(query);
        Matcher matcher = null;
        for (PageInfo page : fullWebPages) {
            matcher = ptn.matcher(page.fileName);
            if (matcher.find()) {
                list.add(page);
                continue;
            }
            if (page.fullPath.indexOf(query) != -1) {
                list.add(page);
                continue;
            }
        }
        return list;
    }

    public static class PageInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private File orignWebFile;
        private String fileName;
        private String fullPath;
        private String unfixFullPath;
        private File sourceFile;

        @Override
        public String toString() {
            return "PageInfo [fileName=" + fileName + ", fullPath=" + fullPath + ", unfixFullPath=" + unfixFullPath + ", sourceFile=" + sourceFile + "]";
        }

        public File getOrignWebFile() {
            return orignWebFile;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFullPath() {
            return fullPath;
        }

        public String getUnfixFullPath() {
            return unfixFullPath;
        }

        public File getSourceFile() {
            return sourceFile;
        }

        public void setOrignWebFile(File orignWebFile) {
            this.orignWebFile = orignWebFile;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        public void setUnfixFullPath(String unfixFullPath) {
            this.unfixFullPath = unfixFullPath;
        }

        public void setSourceFile(File sourceFile) {
            this.sourceFile = sourceFile;
        }
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public List<Object[]> getFullWebPages2() {
        return fullWebPages2;
    }

    public int getTotalPageSize() {
        return fullWebPages.size();
    }

    public List<PageInfo> getFullWebPages() {
        return fullWebPages;
    }

    public List<PageInfo> getSvnWebPages() {
        return svnWebPages;
    }

    public List<Object[]> getSvnWebPages2() {
        return svnWebPages2;
    }

    public List<PageInfo> getSvnWebPages_myWork() {
        return svnWebPages_myWork;
    }

    public List<Object[]> getSvnWebPages2_myWork() {
        return svnWebPages2_myWork;
    }

    public List<PageInfo> getRecentModifys() {
        return recentModifys;
    }

    public List<Object[]> getRecentModifys2() {
        return recentModifys2;
    }

    // for debug .......................................................................................... start
    public void showFullWebPages() {
        log.trace("# showFullWebPages ...");
        if (fullWebPages == null || fullWebPages.isEmpty()) {
            log.trace("is Empty..");
            return;
        }
        for (PageInfo pageInfo : fullWebPages) {
            log.trace("{}", new Object[] { pageInfo });
        }
    }

    List<String> forDebugList = new ArrayList<String>();
    boolean jsfInitOk;
    boolean rootDirMessageOk;

    void logt(String message) {
        if (!rootDirMessageOk) {
            this.forDebugList.add(message);
        }
        log.trace(message);
    }

    public List<String> getForDebugList() {
        return forDebugList;
    }

    /**
     * 手動設定參數
     * 
     * @return
     */
    public String jsfInit() {
        if (jsfInitOk) {
            return null;
        }
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        setHttpRequest(req);
        for (String key : props.keySet()) {
            forDebugList.add(String.format("key:[%s], value:[%s]", key, props.get(key)));
        }
        jsfInitOk = true;
        return null;
    }

    public String getClassPath() {
        URL url = this.getClass().getResource(this.getClass().getSimpleName() + ".class");
        String message = ToStringUtil.toString(url);
        message = message.replaceAll("\n", "<br/>");
        return message;
    }

    // for debug .......................................................................................... end
}
