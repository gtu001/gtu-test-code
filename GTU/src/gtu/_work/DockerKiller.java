package gtu._work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
        List<DockerImage> rtnLst = new ArrayList<DockerImage>();
        for (int ii = 1; ii < lst.size(); ii++) {
            String line = lst.get(ii);
            List<String> blocks = divideToBlocks(line);
            DockerImage image = blocksToImage(blocks);
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
        List<DockerContainer> rtnLst = new ArrayList<DockerContainer>();
        for (int ii = 1; ii < lst.size(); ii++) {
            String line = lst.get(ii);
            List<String> blocks = divideToBlocks(line);
            DockerContainer container = blocksToContainer(blocks);
            System.out.println(container);
            rtnLst.add(container);
        }
        return rtnLst;
    }

    private class DockerContainer {
        String CONTAINER_ID;
        String IMAGE;
        String COMMAND;
        String CREATED;
        String STATUS;
        String PORTS;
        String NAMES;

        @Override
        public String toString() {
            return "DockerContainer [CONTAINER_ID=" + CONTAINER_ID + ", IMAGE=" + IMAGE + ", COMMAND=" + COMMAND + ", CREATED=" + CREATED + ", STATUS=" + STATUS + ", PORTS=" + PORTS + ", NAMES="
                    + NAMES + "]";
        }
    }

    private class DockerImage {
        String REPOSITORY;
        String TAG;
        String IMAGE_ID;
        String CREATED;
        String SIZE;

        @Override
        public String toString() {
            return "DockerImage [REPOSITORY=" + REPOSITORY + ", TAG=" + TAG + ", IMAGE_ID=" + IMAGE_ID + ", CREATED=" + CREATED + ", SIZE=" + SIZE + "]";
        }
    }

    private DockerImage blocksToImage(List<String> lst) {
        DockerImage t = new DockerImage();
        for (int ii = 0; ii < 7; ii++) {
            switch (ii) {
            case 0:
                t.REPOSITORY = lst.get(ii);
                break;
            case 1:
                t.TAG = lst.get(ii);
                break;
            case 2:
                t.IMAGE_ID = lst.get(ii);
                break;
            case 3:
                t.CREATED = lst.get(ii);
                break;
            case 4:
                t.SIZE = lst.get(ii);
                break;
            }
        }
        return t;
    }

    private DockerContainer blocksToContainer(List<String> lst) {
        DockerContainer t = new DockerContainer();
        for (int ii = 0; ii < 7; ii++) {
            switch (ii) {
            case 0:
                t.CONTAINER_ID = lst.get(ii);
                break;
            case 1:
                t.IMAGE = lst.get(ii);
                break;
            case 2:
                t.COMMAND = lst.get(ii);
                break;
            case 3:
                t.CREATED = lst.get(ii);
                break;
            case 4:
                t.STATUS = lst.get(ii);
                break;
            case 5:
                t.PORTS = lst.get(ii);
                break;
            case 6:
                t.NAMES = lst.get(ii);
                break;
            }
        }
        return t;
    }

    private List<String> divideToBlocks(String line) {
        line = StringUtils.defaultString(line);
        String[] arry = line.split("\\s{3}", -1);
        List<String> lst = new ArrayList<String>();
        for (String strVal : arry) {
            strVal = StringUtils.trimToEmpty(strVal);
            if (StringUtils.isBlank(strVal)) {
                continue;
            }
            lst.add(strVal);
        }
        return lst;
    }
}
