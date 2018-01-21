package gtu.jsf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

//import com.uec.util.SessionContext;

/**
 * @author Troy 2009/02/02
 * 
 */
public class InterPremitUrl {

    private static final String FACE_MAP = "faceMap";
    private static final String FACE_CONFIG = "faceConfigRule";
    private static Logger logger = Logger.getLogger(InterPremitUrl.class);

    /**
     * 取得faces-config.xml ,相關權限網址
     */
    private static Document loadFaceConfig(String path) {
        Document faceConfigRule = null;
        File thefile = new File(path);
        BufferedReader bufferedreader = null;
        StringBuffer sb = new StringBuffer();
        try {
            bufferedreader = new BufferedReader(new FileReader(thefile));
            int i = 0;
            while (bufferedreader.ready()) {
                i++;
                String tmpLine = bufferedreader.readLine();
                if (i > 5)
                    sb.append(tmpLine);
            }
            faceConfigRule = DocumentHelper.parseText(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferedreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return faceConfigRule;
    }

    static HashMap<String, String> faceMap = new HashMap<String, String>();

    public static String getPremitString(List<String> premitlst) {
        String path = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getSession().getServletContext().getRealPath("/")
                + "WEB-INF\\faces-config.xml";

        // String path =
        // "C:\\workspace_FEDI\\LandBankFacelet\\WebContent\\WEB-INF\\faces-config.xml";

        Document faceConfigRule = null;
        List list = null;

        if (!SessionContext.contains(FACE_MAP) || !SessionContext.contains(FACE_CONFIG)) {
            faceConfigRule = loadFaceConfig(path);
            list = faceConfigRule.selectNodes("//faces-config/navigation-rule/from-view-id");
            for (int i = 0; i < list.size(); i++) {
                Node node = (Node) ((Element) list.get(i));
                faceMap.put(node.getText().trim(),
                        "/" + node.getUniquePath().replaceAll("from-view-id", "navigation-case/to-view-id"));
            }
            SessionContext.setObject(FACE_MAP, faceMap);
            SessionContext.setObject(FACE_CONFIG, faceConfigRule);
        } else {
            faceMap = (HashMap<String, String>) SessionContext.getObject(FACE_MAP);
            faceConfigRule = (Document) SessionContext.getObject(FACE_CONFIG);
        }

        List<String> lst = new ArrayList<String>();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < premitlst.size(); i++) {
            if (faceMap.get(premitlst.get(i)) != null) {
                lst = gogoTree(faceMap.get(premitlst.get(i)), lst, faceConfigRule);
            } else {
                logger.debug(premitlst.get(i) + "無法取得Follow頁面.....");
                sb.append(premitlst.get(i) + ",");
            }
        }

        for (int i = 0; i < lst.size(); i++) {
            sb.append(lst.get(i) + ",");
        }
        logger.debug("核准網頁..done...");
        return sb.toString();
    }

    private static List<String> gogoTree(String xmlPath, List<String> lst, Document doc) {
        List list2 = doc.selectNodes(xmlPath);
        for (int i = 0; i < list2.size(); i++) {
            Node node = (Node) ((Element) list2.get(i));
            String url = node.getText().trim();
            if (!lst.contains(url)) {
                lst.add(url);
                if (faceMap.get(url) != null)
                    gogoTree(faceMap.get(url), lst, doc);
            }
        }
        return lst;
    }

    public static void main(String[] args) {
        // String path = ((HttpServletRequest) FacesContext.getCurrentInstance()
        // .getExternalContext().getRequest()).getSession().getServletContext()
        // .getRealPath("/")+"WEB-INF\\faces-config.xml";

        InterPremitUrl interPremitUrl = new InterPremitUrl();
        String path = (interPremitUrl.getClass().getResource("").getPath().substring(1) + "faces-config.xml")
                .replaceAll("bin", "src");
        System.out.println(path);

        Document faceConfigRule = null;
        List list = null;

        // 測試
        faceConfigRule = InterPremitUrl.loadFaceConfig(path);

        // System.out.println(faceConfigRule.asXML());

        Element element = faceConfigRule.getRootElement();

        System.out.println(faceConfigRule);
        list = faceConfigRule.selectNodes("//faces-config/navigation-rule/from-view-id");

        for (int i = 0; i < list.size(); i++) {
            Node node = (Node) ((Element) list.get(i));
            faceMap.put(node.getText().trim(),
                    "/" + node.getUniquePath().replaceAll("from-view-id", "navigation-case/to-view-id"));
        }
        // 測試

        List<String> lst = new ArrayList<String>();
        List<String> premitlst = new ArrayList<String>();
        premitlst.add("/bank/ca/customer_info_ca_create_finish.xhtml");

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < premitlst.size(); i++) {
            if (faceMap.get(premitlst.get(i)) != null) {
                lst = gogoTree(faceMap.get(premitlst.get(i)), lst, faceConfigRule);
            } else {
                System.out.println(premitlst.get(i) + "無法取得Follow頁面.....");
                sb.append(premitlst.get(i) + ",");
            }
        }

        for (int i = 0; i < lst.size(); i++) {
            System.out.println(lst.get(i));
            sb.append(lst.get(i) + ",");
        }
        System.out.println("done...");
    }
}
