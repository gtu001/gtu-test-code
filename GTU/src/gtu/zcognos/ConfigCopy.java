package gtu.zcognos;

import gtu.file.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ConfigCopy {

    private ConfigCopy() {
    }

    public ConfigCopy applyBaseDir(File baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public ConfigCopy applyProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public static ConfigCopy getInstance() {
        return INSTANCE;
    }

    public void execute() {
        try {
            copyToTargetDir("archive-log.xml");
            copyToTargetDir("customdata.xml");
            copyToTargetDir("IDLog.xml");
            copyToTargetDir("log.xml");
            copyToTargetDir("Preferences.xml");
            copyToTargetDir("sample.cpf");
            copyToTargetDir("session-log-backup.xml");
            copyToTargetDir("session-log.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File baseDir = new File(FileUtil.DESKTOP_PATH);
    String projectId;

    void copyToTargetDir(String fileName) throws IOException {
        URL url = this.getClass().getResource("config/" + fileName);
        String name = new File(url.getFile()).getName();
        if (name.endsWith(".cpf")) {
            name = projectId + ".cpf";
        }

        File parent = new File(baseDir.getAbsolutePath() + "\\" + projectId + "\\");
        if (!parent.exists()) {
            parent.mkdirs();
        }

        String targetFileName = parent.getAbsolutePath() + "\\" + name;

        byte[] arrayOfByte = new byte[4096];
        BufferedInputStream input = new BufferedInputStream(url.openStream());
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(targetFileName, false));
        int i;
        while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
            output.write(arrayOfByte, 0, i);
        }
        output.close();
        input.close();
    }

    private static final ConfigCopy INSTANCE = new ConfigCopy();
}
