/*
 * Copyright (c) 2010-2020 IISI. All rights reserved.
 * 
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import gtu._work.JarFinder;
import gtu._work.JarFinder.IfMatch;
import gtu.file.FileUtil;
import gtu.maven.MavenDenpencyJarListLoader;

/**
 * 定義task的tag 可以在設定的目錄底下找jar<br/>
 * 
 * dir 目錄 <br/>
 * id 找到的目錄清單所要設定的property id<br/>
 * packagename 搜尋的jar的關鍵字 class path<br/>
 * 
 * @author Troy
 */
public class AntJarFinder extends Task {

    private AntConfigHelper helper;

    private List<File> pomJarLst;

    /** <copyTo=""> */
    private String copyTo;
    /** <repsitory=""> */
    private String repsitory;

    /** <packagename></packagename> */
    private Set<PackageName> packagenames = new LinkedHashSet<PackageName>();

    /** <searchdir></searchdir> */
    private Set<SearchDir> searchdirs = new LinkedHashSet<SearchDir>();

    /** <searchdir></searchdir> */
    private Set<PomConfig> pomConfigs = new LinkedHashSet<PomConfig>();

    @Override
    public void execute() throws BuildException {
        boolean doExecute = true;

        helper = AntConfigHelper.of(this.getProject());

        File copyToDir = null;
        if (StringUtils.isNotBlank(copyTo)) {
            copyToDir = new File(copyTo);
            debug("設定複製目的地(copyTo) : " + copyToDir.getAbsolutePath());
        } else {
            info("!!未設定複製目的地(copyTo)");
            doExecute = false;
        }

        // 是否使用pom處理
        this.pomJarLst = new ArrayList<File>();
        if (!pomConfigs.isEmpty()) {
            PomConfig pomConfig = pomConfigs.iterator().next();
            File mavenExe = new File(helper.getParseAfterValue(pomConfig.mavenExe));
            File pomFile = new File(helper.getParseAfterValue(pomConfig.text));
            info("mavenExe : " + mavenExe + " - " + mavenExe.exists());
            info("pomFile : " + pomFile + " - " + pomFile.exists());
            List<String> pomJarLst2 = MavenDenpencyJarListLoader.newInstance().mavenExePath(mavenExe).pomFile(pomFile).build();
            for (String path : pomJarLst2) {
                pomJarLst.add(new File(path));
            }
            info("pom jar size : " + this.pomJarLst.size());
        }

        Set<File> scanDir = new HashSet<File>();

        // 新增
        searchdirs.add(new SearchDir(repsitory));
        if (searchdirs.isEmpty()) {
            searchdirs.add(new SearchDir(this.getProject().getProperty("basedir")));
        }

        for (SearchDir s : searchdirs) {
            String filePath = helper.getParseAfterValue(s.text);
            File file = new File(filePath);
            if (file.exists()) {
                scanDir.add(file);
                debug("增加搜尋目錄 : " + file);
            } else {
                info("!!搜尋目錄不存在 : " + file);
            }
        }

        if (packagenames == null || packagenames.isEmpty()) {
            info("!!無須尋找的Jar檔 , (packagename) 未設定");
            doExecute = false;
        }

        if (doExecute) {
            this.execute(scanDir, copyToDir);
        }
    }

    private SearchDir createSearchDir(String text) {
        SearchDir dir = new SearchDir();
        dir.text = text;
        return dir;
    }

    private void execute(Set<File> scanDir, File copyToDir) {
        try {
            JarFinder finder = JarFinder.newInstance();
            for (PackageName pk : packagenames) {
                final List<File> sameJarList = new ArrayList<File>();
                info("搜尋 :" + pk.text);
                finder.pattern(pk.text);

                for (File search : scanDir) {
                    finder.setDir(search);
                    debug("搜尋來源目錄 : " + search);
                    finder.ifMatchEvent(new IfMatch() {
                        public void apply(String jarFile, String targetName, Map<String, Collection<String>> matchMap) {
                            File realFile = new File(jarFile);
                            if (!sameJarList.contains(realFile)) {
                                // debug("找到 : " + realFile);
                                sameJarList.add(realFile);
                            }
                        }
                    }).execute();
                }

                if (sameJarList.size() == 0) {
                    info("!!找不到符合的Jar檔 : " + pk.text);
                    // continue;
                    System.err.println("找不到符合jar檔 : " + pk.text);
                    throw new BuildException("找不到符合jar檔 : " + pk.text);
                }

                SearchMode searchMode = null;
                if (StringUtils.isNotEmpty(pk.mode)) {
                    searchMode = SearchMode.matchMode(pk.mode);
                    debug("尋找模式 : " + searchMode);
                }
                if (searchMode == null) {
                    System.err.println("尋找模式設定錯誤 : " + pk.text + " / " + pk.mode);
                    throw new BuildException("尋找模式設定錯誤 : " + pk.text + " / " + pk.mode);
                }

                File jar = searchMode.apply(sameJarList, pk, this);

                if (jar == null) {
                    System.err.println("找不到符合jar檔 : " + pk.text);
                    throw new BuildException("找不到符合jar檔 : " + pk.text);
                }

                // 複製jar
                this.copyFile(jar, copyToDir);
                finder.clear();
            }
        } catch (Exception ex) {
            System.err.println("執行失敗!!");
            throw new BuildException("執行失敗!!", ex);
        }
    }

    enum SearchMode {
        SMALL("small") {
            File apply(List<File> fileList, PackageName pk, AntJarFinder _this) {
                _this.info("[尋找最小的Jar]");
                File small = null;
                long size = 0;
                for (File f : fileList) {
                    _this.debug("檔案 : " + f.getAbsolutePath() + " => 大小 : " + _this.fileSize(f));
                    if (size == 0) {
                        size = f.length();
                    }
                    if (f.length() <= size) {
                        size = f.length();
                        small = f;
                    }
                }
                _this.log("符合的Jar : " + small.getAbsolutePath() + " => 大小 : " + _this.fileSize(small));
                return small;
            }

            public String toString() {
                return "最小";
            }
        },
        NEW("new") {
            File apply(List<File> fileList, PackageName pk, AntJarFinder _this) {
                _this.info("[尋找最新的Jar]");
                File lastestFile = null;
                long time = 0;
                for (File f : fileList) {
                    _this.debug("檔案 : " + f.getAbsolutePath() + " => 最後時間 : " + DateFormatUtils.format(f.lastModified(), "yyyy/MM/dd"));
                    if (time == 0) {
                        time = f.lastModified();
                    }
                    if (f.lastModified() >= time) {
                        time = f.lastModified();
                        lastestFile = f;
                    }
                }
                _this.log("符合的Jar : " + lastestFile.getAbsolutePath() + " => 時間 : " + DateFormatUtils.format(lastestFile.lastModified(), "yyyy/MM/dd") + " => 大小 : " + _this.fileSize(lastestFile));
                return lastestFile;
            }

            public String toString() {
                return "最新";
            }
        },
        OLD("old") {
            File apply(List<File> fileList, PackageName pk, AntJarFinder _this) {
                _this.info("[尋找最舊的Jar]");
                File oldFile = null;
                long time = 0;
                for (File f : fileList) {
                    _this.debug("檔案 : " + f.getAbsolutePath() + " => 最後時間 : " + DateFormatUtils.format(f.lastModified(), "yyyy/MM/dd"));
                    if (time == 0) {
                        time = f.lastModified();
                    }
                    if (f.lastModified() <= time) {
                        time = f.lastModified();
                        oldFile = f;
                    }
                }
                _this.log("符合的Jar : " + oldFile.getAbsolutePath() + " => 時間 : " + DateFormatUtils.format(oldFile.lastModified(), "yyyy/MM/dd") + " => 大小 : " + _this.fileSize(oldFile));
                return oldFile;
            }

            public String toString() {
                return "最舊";
            }
        },
        EXACT("exact") {
            File apply(List<File> fileList, PackageName pk, AntJarFinder _this) {
                _this.info("[尋找名子一致]");
                if (StringUtils.isBlank(pk.name)) {
                    String msg = "請設定attr \"name\", 放jar檔名";
                    _this.info(msg);
                    System.err.println(msg);
                    throw new BuildException(msg);
                }
                File jarFile = null;
                for (File f : fileList) {
                    _this.debug("檔案 : " + f.getAbsolutePath() + " => 最後時間 : " + DateFormatUtils.format(f.lastModified(), "yyyy/MM/dd"));
                    if (StringUtils.equals(pk.name, f.getName())) {
                        jarFile = f;
                    }
                }
                if (jarFile == null) {
                    System.err.println("找不到相同檔名 : " + pk.name);
                    throw new BuildException("找不到相同檔名 : " + pk.name);
                }
                _this.log("符合的Jar : " + jarFile.getAbsolutePath() + " => 大小 : " + _this.fileSize(jarFile));
                return jarFile;
            }

            public String toString() {
                return "名稱一致";
            }
        },
        POM("pom") {
            File apply(List<File> fileList, PackageName pk, AntJarFinder _this) {
                _this.info("[尋找與POM一致]");
                if (_this.pomJarLst.isEmpty()) {
                    String msg = "請設定PomConfig";
                    _this.info(msg);
                    System.err.println(msg);
                    throw new BuildException(msg);
                }
                File jarFile = null;
                for (File f : fileList) {
                    _this.debug("檔案 : " + f.getAbsolutePath());
                    for (File pomJarFile : _this.pomJarLst) {
                        if (StringUtils.equals(pomJarFile.getName(), f.getName())) {
                            jarFile = f;
                            break;
                        }
                    }
                }
                if (jarFile == null) {
                    System.err.println("找不到相同檔名 !!");
                    throw new BuildException("找不到相同檔名!!");
                }
                _this.log("符合的Jar : " + jarFile.getAbsolutePath() + " => 大小 : " + _this.fileSize(jarFile));
                return jarFile;
            }

            public String toString() {
                return "與POM一致";
            }
        },;
        final String mode;

        SearchMode(String mode) {
            this.mode = mode;
        }

        static SearchMode matchMode(String mode) {
            for (SearchMode s : SearchMode.values()) {
                if (s.mode.equalsIgnoreCase(mode)) {
                    return s;
                }
            }
            return null;
        }

        abstract File apply(List<File> fileList, PackageName pk, AntJarFinder _this);
    }

    private void copyFile(File src, File copyToDir) throws IOException {
        if (copyToDir != null && copyToDir.exists() && src != null) {
            File dest = new File(copyToDir.getAbsolutePath(), src.getName());
            if (dest.isFile() && dest.exists() && dest.length() == src.length()) {
                debug("目的地已存在檔案(跳過) : " + dest.getName());
                return;
            }
            boolean copyResult = FileUtil.copyFile(src, dest);
            info("[複製檔案]" + (copyResult ? "[成功]" : "[失敗]") + " : " + dest + " 大小 : " + fileSize(dest));
        } else {
            info("[複製檔案失敗] : (copyTo) = " + copyToDir + " , (src) = " + src);
        }
    }

    String fileSize(File file) {
        return file.length() / 1024 + "k";
    }

    void debug(Object message) {
        System.out.println("AntJarFinder - " + message);
    }

    void info(Object message) {
        System.err.println("AntJarFinder - " + message);
    }

    public PackageName createPackageName() {
        PackageName pkg = new PackageName();
        packagenames.add(pkg);
        return pkg;
    }

    public SearchDir createSearchDir() {
        SearchDir sch = new SearchDir();
        searchdirs.add(sch);
        return sch;
    }

    public PomConfig createPomConfig() {
        PomConfig sch = new PomConfig();
        pomConfigs.add(sch);
        return sch;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }

    public void setRepsitory(String repsitory) {
        this.repsitory = repsitory;
    }

    public class SearchDir {
        private String text;

        public SearchDir() {
        }

        public SearchDir(String text) {
            this.text = text;
        }

        public void addText(String text) {
            this.text = text;
        }

        private AntJarFinder getOuterType() {
            return AntJarFinder.this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((text == null) ? 0 : text.hashCode());
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
            SearchDir other = (SearchDir) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (text == null) {
                if (other.text != null)
                    return false;
            } else if (!text.equals(other.text))
                return false;
            return true;
        }
    }

    public class PackageName {
        private String text;
        private String mode;
        private String name;

        public void addText(String text) {
            this.text = text;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        private AntJarFinder getOuterType() {
            return AntJarFinder.this;
        }
    }

    public class PomConfig {
        private String text;
        private String mavenExe;

        public void addText(String text) {
            this.text = text;
        }

        public void setMavenExe(String mavenExe) {
            this.mavenExe = mavenExe;
        }

        private AntJarFinder getOuterType() {
            return AntJarFinder.this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((mavenExe == null) ? 0 : mavenExe.hashCode());
            result = prime * result + ((text == null) ? 0 : text.hashCode());
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
            PomConfig other = (PomConfig) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (mavenExe == null) {
                if (other.mavenExe != null)
                    return false;
            } else if (!mavenExe.equals(other.mavenExe))
                return false;
            if (text == null) {
                if (other.text != null)
                    return false;
            } else if (!text.equals(other.text))
                return false;
            return true;
        }
    }
}
