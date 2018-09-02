package gtu.tomcat;

// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2008/9/18 �U�� 02:06:22
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   fillMd5Pwd.java

import java.io.File;
import java.security.MessageDigest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Troy 2009/02/02
 * 
 */
public class fillMd5Pwd {

    private static final char hex[] = "0123456789abcdef".toCharArray();
    private static final String ROLE = "fediuser";
    // private static String filePath = "tomcat-users.xml";
    private static String filePath;

    public fillMd5Pwd() {
        String basePath = this.getClass().getResource("").getPath() + "tomcat-users.xml";
        filePath = basePath.substring(basePath.indexOf("/") + 1);
        System.out.println(filePath);
    }

    public String digest(String cleartext, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(cleartext.getBytes());
        byte digest[] = md.digest();
        StringBuffer sb = new StringBuffer(2 * digest.length);
        for (int i = 0; i < digest.length; i++) {
            int high = (digest[i] & 0xf0) >> 4;
            int low = digest[i] & 0xf;
            sb.append(hex[high]);
            sb.append(hex[low]);
        }
        return sb.toString();
    }

    private Document readFileToXml(String sSource) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new File(sSource));
        return doc;
    }

    public static void main(String args[]) throws Exception {
        String sOldPassword;
        String sNewPassword;
        String sChkPassword;
        String sRole;
        // if (args.length != 4)
        // return;
        // // break MISSING_BLOCK_LABEL_55;
        // sOldPassword = args[0];
        // sNewPassword = args[1];
        // sChkPassword = args[2];

        sOldPassword = "1234";
        sNewPassword = "123";
        sChkPassword = "123";
        sRole = "fediuser";
        if (!sNewPassword.equals(sChkPassword))
            return;
        try {
            (new fillMd5Pwd()).go(sOldPassword, sNewPassword, sChkPassword, sRole);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void go(String sOldPassword, String sNewPassword, String sChkPassword, String sRole) throws Exception {
        String sPath = "";
        Document doc = readFileToXml(filePath);
        NodeList nodeLst = doc.getDocumentElement().getChildNodes();
        boolean isNeedSave = false;
        boolean isUserFound = false;
        for (int ii = 0; ii < nodeLst.getLength(); ii++) {
            Node node = nodeLst.item(ii);
            if (node.getNodeType() == 1 && node.getNodeName().equalsIgnoreCase("user")
                    && node.getAttributes().getNamedItem("username").getNodeValue().equals(sRole)) {
                isUserFound = true;
                if (node.getAttributes().getNamedItem("password").getNodeValue()
                        .equalsIgnoreCase(digest(sOldPassword, "MD5"))) {
                    String sNewDegest = digest(sNewPassword, "MD5");
                    node.getAttributes().getNamedItem("password").setNodeValue(sNewDegest);
                    isNeedSave = true;
                } else {
                    System.out.println("check old password fail !");
                }
            }
        }

        if (!isUserFound)
            System.out.println("user not found !");
        if (isNeedSave) {
            javax.xml.transform.Source source = new DOMSource(doc);
            File file = new File(filePath);
            javax.xml.transform.Result result = new StreamResult(file);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();

            System.out.println("change password success!");
        }
    }
}