package gtu._work;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.ArrayUtils;

import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.string.StringUtil_;

public class DockerKiller {

    public static void main(String[] args) {
        DockerKiller t = new DockerKiller();
        t.images();
    }

    public List<DockerImage> images() {
        RuntimeBatPromptModeUtil _inst = RuntimeBatPromptModeUtil.newInstance();
        _inst.runInBatFile(false);
        _inst.command("docker images");
        ProcessWatcher proc = ProcessWatcher.newInstance(_inst.apply());
        proc.getStreamSync();
        String errorMsg = proc.getErrorStreamToString();
        String inputMsg = proc.getInputStreamToString();
        System.out.println("ERR : " + errorMsg);
        System.out.println("OUTPUT : " + inputMsg);
        List<String> lst = StringUtil_.readContentToList(inputMsg, false, false, false);
        if (lst.size() <= 1) {
            return Collections.EMPTY_LIST;
        }
        Map<String, Integer> firstBlocks = fistBlockSetting(lst.get(0));
        List<DockerImage> rtnLst = new ArrayList<DockerImage>();
        for (int ii = 1; ii < lst.size(); ii++) {
            String line = lst.get(ii);
            Map<String, String> blocks = divideToBlocks(line, firstBlocks);
            DockerImage image = blocksToBean(blocks, DockerImage.class);
            System.out.println(image);
            rtnLst.add(image);
        }
        return rtnLst;
    }

    public List<DockerContainer> ps_minus_a() {
        RuntimeBatPromptModeUtil _inst = RuntimeBatPromptModeUtil.newInstance();
        _inst.runInBatFile(false);
        _inst.command("docker ps -a");
        ProcessWatcher proc = ProcessWatcher.newInstance(_inst.apply());
        proc.getStreamSync();
        String errorMsg = proc.getErrorStreamToString();
        String inputMsg = proc.getInputStreamToString();
        System.out.println("ERR : " + errorMsg);
        System.out.println("OUTPUT : " + inputMsg);
        List<String> lst = StringUtil_.readContentToList(inputMsg, false, false, false);
        if (lst.size() <= 1) {
            return Collections.EMPTY_LIST;
        }
        Map<String, Integer> firstBlocks = fistBlockSetting(lst.get(0));
        List<DockerContainer> rtnLst = new ArrayList<DockerContainer>();
        for (int ii = 1; ii < lst.size(); ii++) {
            String line = lst.get(ii);
            Map<String, String> blocks = divideToBlocks(line, firstBlocks);
            DockerContainer container = blocksToBean(blocks, DockerContainer.class);
            System.out.println(container);
            rtnLst.add(container);
        }
        return rtnLst;
    }

    public void deleteContainer(String containerId) {
        RuntimeBatPromptModeUtil _inst = RuntimeBatPromptModeUtil.newInstance();
        _inst.runInBatFile(false);
        _inst.command("docker rm " + containerId);
        ProcessWatcher proc = ProcessWatcher.newInstance(_inst.apply());
        proc.getStreamSync();
        String errorMsg = proc.getErrorStreamToString();
        String inputMsg = proc.getInputStreamToString();
        System.out.println("ERR : " + errorMsg);
        System.out.println("OUTPUT : " + inputMsg);
    }

    public void deleteImage(String imageId) {
        RuntimeBatPromptModeUtil _inst = RuntimeBatPromptModeUtil.newInstance();
        _inst.runInBatFile(false);
        _inst.command("docker rmi " + imageId);
        ProcessWatcher proc = ProcessWatcher.newInstance(_inst.apply());
        proc.getStreamSync();
        String errorMsg = proc.getErrorStreamToString();
        String inputMsg = proc.getInputStreamToString();
        System.out.println("ERR : " + errorMsg);
        System.out.println("OUTPUT : " + inputMsg);
    }

    private Map<String, Integer> fistBlockSetting(String line) {
        Map<String, Integer> setting = new LinkedHashMap<String, Integer>();
        Pattern ptn = Pattern.compile("\\w+");
        Matcher mth = ptn.matcher(line);
        int latestPos = -1;
        int latestEndPos = -1;
        String tmpKey = "";
        for (; mth.find();) {
            boolean ignoreLatestPos = false;
            latestEndPos = mth.start();
            if (StringUtils.isNotBlank(tmpKey)) {
                if (ArrayUtils.contains(new String[] { "CONTAINER", "IMAGE" }, tmpKey) && //
                        StringUtils.equals(mth.group(), "ID")) {
                    tmpKey = tmpKey + "_ID";
                    ignoreLatestPos = true;
                } else {
                    setting.put(tmpKey, Math.abs(latestPos - latestEndPos));
                }
            }
            if (!ignoreLatestPos) {
                latestPos = mth.start();
                tmpKey = mth.group();
            }
        }
        if (StringUtils.isNotBlank(tmpKey)) {
            setting.put(tmpKey, line.substring(latestEndPos).length());
        }
        System.out.println("setting : " + setting);
        return setting;
    }

    public static class DockerContainer {
        String CONTAINER_ID;
        String IMAGE;
        String COMMAND;
        String CREATED;
        String STATUS;
        String PORTS;
        String NAMES;

        public Object[] toRows() {
            return new Object[] { CONTAINER_ID, IMAGE, COMMAND, CREATED, STATUS, PORTS, NAMES, this };
        }

        @Override
        public String toString() {
            return "DockerContainer [CONTAINER_ID=" + CONTAINER_ID + ", IMAGE=" + IMAGE + ", COMMAND=" + COMMAND + ", CREATED=" + CREATED + ", STATUS=" + STATUS + ", PORTS=" + PORTS + ", NAMES="
                    + NAMES + "]";
        }

        public String getCONTAINER_ID() {
            return CONTAINER_ID;
        }

        public void setCONTAINER_ID(String cONTAINER_ID) {
            CONTAINER_ID = cONTAINER_ID;
        }

        public String getIMAGE() {
            return IMAGE;
        }

        public void setIMAGE(String iMAGE) {
            IMAGE = iMAGE;
        }

        public String getCOMMAND() {
            return COMMAND;
        }

        public void setCOMMAND(String cOMMAND) {
            COMMAND = cOMMAND;
        }

        public String getCREATED() {
            return CREATED;
        }

        public void setCREATED(String cREATED) {
            CREATED = cREATED;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String sTATUS) {
            STATUS = sTATUS;
        }

        public String getPORTS() {
            return PORTS;
        }

        public void setPORTS(String pORTS) {
            PORTS = pORTS;
        }

        public String getNAMES() {
            return NAMES;
        }

        public void setNAMES(String nAMES) {
            NAMES = nAMES;
        }
    }

    public static class DockerImage {
        String REPOSITORY;
        String TAG;
        String IMAGE_ID;
        String CREATED;
        String SIZE;

        public Object[] toRows() {
            return new Object[] { REPOSITORY, TAG, IMAGE_ID, CREATED, SIZE, this };
        }

        @Override
        public String toString() {
            return "DockerImage [REPOSITORY=" + REPOSITORY + ", TAG=" + TAG + ", IMAGE_ID=" + IMAGE_ID + ", CREATED=" + CREATED + ", SIZE=" + SIZE + "]";
        }

        public String getREPOSITORY() {
            return REPOSITORY;
        }

        public void setREPOSITORY(String rEPOSITORY) {
            REPOSITORY = rEPOSITORY;
        }

        public String getTAG() {
            return TAG;
        }

        public void setTAG(String tAG) {
            TAG = tAG;
        }

        public String getIMAGE_ID() {
            return IMAGE_ID;
        }

        public void setIMAGE_ID(String iMAGE_ID) {
            IMAGE_ID = iMAGE_ID;
        }

        public String getCREATED() {
            return CREATED;
        }

        public void setCREATED(String cREATED) {
            CREATED = cREATED;
        }

        public String getSIZE() {
            return SIZE;
        }

        public void setSIZE(String sIZE) {
            SIZE = sIZE;
        }
    }

    private String fixBlockTitleName(String fname) {
        return fname.replaceAll("\\s", "_");
    }

    private <T> T blocksToBean(Map<String, String> blocks, Class<T> clz) {
        String fname = "";
        try {
            T obj = clz.newInstance();
            for (String key : blocks.keySet()) {
                fname = key;
                String strVal = blocks.get(key);
                fname = fixBlockTitleName(fname);
                Field f = FieldUtils.getDeclaredField(clz, fname, true);
                f.set(obj, strVal);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[blocksToBean] ERR :" + e.getMessage() + " -- " + fname, e);
        }
    }

    private Map<String, String> divideToBlocks(String line, Map<String, Integer> firstBlocks) {
        Map<String, String> rtnMap = new LinkedHashMap<String, String>();
        int startPos = 0;
        int index = 0;
        for (String key : firstBlocks.keySet()) {
            int length = firstBlocks.get(key);
            String strVal = line.substring(startPos, startPos + length);
            if (index == firstBlocks.size() - 1) {
                strVal = line.substring(startPos);
            }
            System.out.println(key + "\t" + strVal);
            startPos += length;
            rtnMap.put(key, StringUtils.trimToEmpty(strVal));
            index++;
        }
        return rtnMap;
    }
}
