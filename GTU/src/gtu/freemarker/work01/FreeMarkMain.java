package gtu.freemarker.work01;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.TemplateException;
import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;

public class FreeMarkMain {

    public static void main(String[] args) throws IOException, TemplateException, ClassNotFoundException {
        InputStream inputStream = FreeMarkMain.class.getResourceAsStream("estshit_ver2.txt");
        URL srcUtl = FreeMarkMain.class.getResource("Im5105NDetail.flt");
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        List<Im5105mDo> list = (List<Im5105mDo>)ois.readObject();
        Map<String,Object> root = new HashMap<String,Object>();
        root.put("nx5105mList", list);
        File destFile = new File(FileUtil.DESKTOP_DIR, "test.html");
        FreeMarkerSimpleUtil.replace(destFile, srcUtl, root);
        Runtime.getRuntime().exec("cmd /c start " + destFile);
        System.out.println("done..");
    }
}
