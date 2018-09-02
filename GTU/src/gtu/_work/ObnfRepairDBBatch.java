package gtu._work;

import gtu.apache.BeanUtilGtu;
import gtu.file.FileUtil;
import gtu.hibernate.ReadDomainJarTableConfig;
import gtu.string.StringCompressUtil;
import gtu.string.StringUtilForDb;
import gtu.xml.xstream.iisi.XmlParserImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import tw.gov.moi.ae.jms.JmsMessageNew;

public class ObnfRepairDBBatch {
    
    private PrintStream out = System.out;
    
    public static void main(String[] args) {
        ObnfRepairDBBatch test = new ObnfRepairDBBatch();
        test.setDomainJar(new File(test.fetchDomainJar()));
//        test.execute("", new File("G:/temp/本日RC"));
        test.executeDecrypt(new File("C:/Users/gtu001/Desktop/xxxxxxxxxxxxxxx.txt"));
        System.out.println("done...");
    }
    
    public void execute(String messageGroup, File exceptionErrorDir) {
        try {
            List<String> messageList = new ArrayList<String>();
            // BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(messageIdFile)));
            BufferedReader reader = new BufferedReader(new StringReader(messageGroup));
            for (String line = null; (line = reader.readLine()) != null;) {
                messageList.add(line);
            }
            reader.close();
            out.println("messageList => " + messageList);

            List<File> fileList = new ArrayList<File>();
            List<File> matchMessageFileList = new ArrayList<File>();
            FileUtil.searchFilefind(exceptionErrorDir, ".*", fileList);
            
            boolean notFound = false;
            if(!messageList.isEmpty()){
                for (String messageId : messageList) {
                    File messageFile = null;
                    for (File f : fileList) {
                        if (f.getName().contains(messageId)) {
                            messageFile = f;
                            break;
                        }
                    }
                    if (messageFile == null) {
                        out.println("!!找不到對應檔案=>" + messageId);
                        notFound = true;
                    } else {
                        matchMessageFileList.add(messageFile);
                    }
                }
            }else{
                matchMessageFileList.addAll(fileList);
            }

            out.println("全部共=" + fileList.size());
            fileList.removeAll(matchMessageFileList);
            out.println("可以刪除的檔=" + fileList.size());
            out.println("要處理的檔=" + matchMessageFileList.size());
            out.println("全部找到messageId => " + (notFound ? "否" : "是"));
            
            if (!notFound) {
                out.println("#### 開始產生批次報表!! ####");
                batchReaderInfo(messageList, matchMessageFileList, exceptionErrorDir);
                out.println("#### 產生批次報表完成!! ####");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void executeDecrypt(File file){
        String tmpXml = null;
        try{
            Validate.isTrue(file.isFile(), "必須是檔案!");
            
            File outputSqlFile = new File(file.getParent(), "outputSqlFile_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".sql");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputSqlFile),
                    "utf8"));
            
            XmlParserImpl xmlParserImpl = new XmlParserImpl();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            
            int index = 0;
            for (String line = null; (line = reader.readLine()) != null;) {
                final String jmsMessageXML = StringCompressUtil.uncompress(line);
                JmsMessageNew jmsMessageNew = (JmsMessageNew) xmlParserImpl.parseToObj(jmsMessageXML);
                line = jmsMessageNew.getMessageXML();
                tmpXml = line;
                
                String sql = readInfo(line);
                
                writer.write("--##########################################################################Start");
                writer.newLine();
                writer.write("--第" + (++index) + "筆");
                writer.newLine();
                writer.write(sql);
                writer.newLine();
                writer.write("--##########################################################################Start");
                writer.newLine();
            }
            reader.close();
            
            writer.flush();
            writer.close();
        }catch(Exception ex){
            System.out.println(tmpXml);
            throw new RuntimeException(ex);
        }
    }

    private void batchReaderInfo(List<String> messageList, List<File> matchMessageFileList, File outputDir) {
        File tempFile = null;
        try {
            File outputSqlFile = new File(outputDir, "outputSqlFile_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".sql");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputSqlFile),
                    "utf8"));

            int index = 0;
            for (File f : matchMessageFileList) {
                tempFile = f;
                StringBuffer sb = new StringBuffer();
                StringBuffer exSb = new StringBuffer();
                int startLineNumber = -1;
                LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(f), "utf8"));
                for (String line = null; (line = reader.readLine()) != null;) {
                    if(startLineNumber == -1){
                        this.startWithAppendLine(line, exSb);
                    }
                    if (line.contains("---通報OBNFDTO內容---")) {
                        startLineNumber = reader.getLineNumber() + 1;
                    }
                    if (startLineNumber != -1 && startLineNumber <= reader.getLineNumber()) {
                        sb.append(line + "\n");
                    }
                }
                reader.close();

                String sql = readInfo(sb.toString());

                String messageCurrentId = null;
                for (String messageId : messageList) {
                    if(f.getName().contains(messageId)){
                        messageCurrentId = messageId;
                        break;
                    }
                }
                if(messageCurrentId == null){
                    messageCurrentId = f.getName();
                }
                
                out.println("產生:" + messageCurrentId);

                writer.write("--##########################################################################Start");
                writer.newLine();
                writer.write("--第" + (++index) + "筆 : " + messageCurrentId);
                writer.newLine();
                writer.write("--錯誤訊息↓↓↓");
                writer.newLine();
                writer.write(exSb.toString());
                writer.write("--錯誤訊息↑↑↑");
                writer.newLine();
                BufferedReader reader1 = new BufferedReader(new StringReader(sql));
                for (String line = null; (line = reader1.readLine()) != null;) {
                    writer.write(line);
                    writer.newLine();
                }
                reader1.close();
                writer.write("--##########################################################################End");
                writer.newLine();
            }

            writer.flush();
            writer.close();
        } catch (Exception ex) {
            out.println("error file => " + tempFile);
            throw new RuntimeException(ex);
        }
    }
    
    public String executeSingleXml(String inputStr){
        return this.readInfo(inputStr);
    }

    private String readInfo(String inputStr) {
        try {
            Document doc = new SAXReader().read(new StringReader(inputStr), "");
            StringBuilder sb = new StringBuilder();
            try {
                Element e1 = (Element) doc.selectSingleNode("//tw.gov.moi.rs.dto.SendMessageDTO/executant/system/site");
                sb.append("--發送戶所 : " + e1.selectSingleNode("code").getText() + " , "
                        + e1.selectSingleNode("name").getText() + "\n");

                Element e2 = (Element) doc.selectSingleNode("//tw.gov.moi.rs.dto.SendMessageDTO/executant/operation");
                sb.append("--執行作業 : " + e2.selectSingleNode("taskName").getText() + " , "
                        + e2.selectSingleNode("taskCode").getText() + "\n");
                sb.append("--transactionId : " + e2.selectSingleNode("transactionId").getText() + "\n");
            } catch (Exception ex) {
                sb.append("--無法取得executant資訊\n");
            }

            List<ObnfDTO> obnfList = new ArrayList<ObnfDTO>();
            List<Element> nodeList = (List<Element>) doc
                    .selectNodes("//tw.gov.moi.rs.dto.SendMessageDTO/wkDataList/tw.gov.moi.rs.dto.ObnfDTO");
            if(nodeList == null || nodeList.isEmpty()){
                nodeList = (List<Element>) doc.selectNodes("//list/tw.gov.moi.rs.dto.ObnfDTO");
            }
            
            for (Node node : nodeList) {
                Element element = (Element) node;
                Map<String, String> wkDataObjectMap = new LinkedHashMap<String, String>();
                Map<String, String> wkKeyMap = new LinkedHashMap<String, String>();

                ObnfDTO obnf = new ObnfDTO();
                for (Iterator it = element.elementIterator(); it.hasNext();) {
                    Element elem = (Element) it.next();
                    if (elem.getName().matches("(wkKey|wkDataObject|wkNoticeDateTime)")) {
                        if (elem.getName().equals("wkNoticeDateTime")) {
                            try{
                                String year = StringUtils.leftPad(elem.selectSingleNode("date/year").getText(), 3, '0');
                                String month = StringUtils.leftPad(elem.selectSingleNode("date/month").getText(), 2, '0');
                                String day = StringUtils.leftPad(elem.selectSingleNode("date/day").getText(), 2, '0');
                                String hour = StringUtils.leftPad(elem.selectSingleNode("time/hour").getText(), 2, '0');
                                String minute = StringUtils.leftPad(elem.selectSingleNode("time/minute").getText(), 2, '0');
                                String second = StringUtils.leftPad(elem.selectSingleNode("time/second").getText(), 2, '0');
                                obnf.wkNoticeDateTime = year+"/"+month+"/"+day+" "+hour+":"+minute+":"+second;
                            }catch(Exception ex){
                                out.println("obnf.wkNoticeDateTime取得錯誤!");
                            }
                        }
                        if(elem.getName().equals("wkDataObject")){
                            List<Element> pairEntList = (List<Element>)elem.selectNodes("tw.gov.moi.rs.dto.PairEntry");
                            for(Element pairEnt : pairEntList){
                                this.setKeyValueToWkDataMap(pairEnt, wkDataObjectMap);
                            }
                        }
                        if(elem.getName().equals("wkKey")){
                            List<Element> pairEntList = (List<Element>)elem.selectNodes("tw.gov.moi.rs.dto.PairEntry");
                            for(Element pairEnt : pairEntList){
                                this.setKeyValueToWkDataMap(pairEnt, wkKeyMap);
                            }
                        }
                        continue;
                    }
                    try {
                        Field field = ObnfDTO.class.getDeclaredField(elem.getName());
                        field.setAccessible(true);
                        field.set(obnf, elem.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                obnf.wkKeyMap = wkKeyMap;
                obnf.wkDataObjectMap = wkDataObjectMap;

                obnfList.add(obnf);
            }
            
            //referencePairEntry remove
            for (ObnfDTO obnf : obnfList) {
                for(String key : new ArrayList<String>(obnf.wkKeyMap.keySet())){
                    if(key.contains("..")){
                        this.repairReferenceValue(key, obnf, obnfList);
                    }
                }
                for(String key : new ArrayList<String>(obnf.wkDataObjectMap.keySet())){
                    if(key.contains("..")){
                        this.repairReferenceValue(key, obnf, obnfList);
                    }
                }
            }

            for (ObnfDTO obnf : obnfList) {
                processSQL(obnf, sb);
            }

            return sb.toString();
        } catch (Exception ex) {
            FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, "errorlog_Test16.txt"), inputStr, "utf8");
            throw new RuntimeException(ex);
        }
    }
    
    private void setKeyValueToWkDataMap(Element pairEnt, Map<String, String> map){
        if(pairEnt.attribute("reference") == null){
            String key = null;
            String value = null;
            try{
                key = pairEnt.selectSingleNode("key").getText();
            }catch(Exception ex){
                return;
            }
            try{
                value = pairEnt.selectSingleNode("value").getText();
            }catch(Exception ex){
            }
            map.put(key, value);
        }else{
            map.put(pairEnt.attribute("reference").getValue(), "");
        }
    }
    
    private static Pattern referenceKeyPattern = Pattern.compile("\\.\\.\\/\\.\\.\\/\\.\\.\\/tw\\.gov\\.moi\\.rs\\.dto\\.ObnfDTO\\/wkKey\\/tw\\.gov\\.moi\\.rs\\.dto\\.PairEntry\\[(\\d+)\\]");
    private static Pattern referenceObjectPattern = Pattern.compile("\\.\\.\\/\\.\\.\\/\\.\\.\\/tw\\.gov\\.moi\\.rs\\.dto\\.ObnfDTO\\/wkDataObject\\/tw\\.gov\\.moi\\.rs\\.dto\\.PairEntry\\[(\\d+)\\]");
    enum PairEntryReference {
        Key(referenceKeyPattern, "wkKeyMap"),//
        Object(referenceObjectPattern, "wkDataObjectMap"), //
        ;
        final Pattern pattern;
        final String mapName;
        PairEntryReference(Pattern pattern, String mapName){
            this.pattern = pattern;
            this.mapName = mapName;
        }
    }
    private void repairReferenceValue(String key, ObnfDTO obnfDTO, List<ObnfDTO> obnfList){
        Matcher matcher = null;
        for(PairEntryReference pe : PairEntryReference.values()){
            matcher = pe.pattern.matcher(key);
            if(matcher.matches()){
                int index = Integer.parseInt(matcher.group(1)) - 1;
                for(int ii = 0 ; ii < obnfList.size() ; ii++){
                    ObnfDTO obnf = obnfList.get(ii);
                    if(obnfDTO.equals(obnf)){
                        Map<String,String> map = (Map<String,String>)BeanUtilGtu.getPropertyByField(obnf, pe.mapName);
                        List<String> array = new ArrayList<String>(map.keySet());
                        String key2 = array.get(index);
                        String value = map.get(key2);
                        if(!key2.contains("..")){
                            out.println(ii + "---->" + key2 + " = " + value);
                            Map<String,String> repairMap = (Map<String,String>)BeanUtilGtu.getPropertyByField(obnfDTO, pe.mapName);
                            repairMap.remove(key);
                            repairMap.put(key2, value);
                        }
                    }
                }
            }
        }
    }

    private void processSQL(ObnfDTO obnf, StringBuilder sb) {
        Map<String, String> wkKeyMapCopy = new LinkedHashMap<String, String>();
        HashMap<String, String> wkDataObjectMapCopy = new LinkedHashMap<String, String>();

        sb.append("-------------------------------------------\n");
        sb.append("--" + obnf.toString() + "\n");
        for (String key : obnf.wkKeyMap.keySet()) {
            if (key.equals("serialVersionUID")) {
                continue;
            }
            String newKey = StringUtilForDb.javaToDbField(key);
            wkKeyMapCopy.put(newKey, obnf.wkKeyMap.get(key));
        }
        for (String key : obnf.wkDataObjectMap.keySet()) {
            if (key.equals("serialVersionUID")) {
                continue;
            }
            String newKey = StringUtilForDb.javaToDbField(key);
            wkDataObjectMapCopy.put(newKey, obnf.wkDataObjectMap.get(key));
        }
        sb.append("--wkKeyMap : " + obnf.wkKeyMap + "\n");
        sb.append("--wkDataObjectMap : " + obnf.wkDataObjectMap + "\n");

        String tableName = obnf.wkTableName.replaceAll("Type", "");
        
        //為update特別保留 ,補齊update欄位為空白是件可怕的事 XXX
        HashMap<String, String> wkDataObjectMapCopyForUpdate = //
                (HashMap<String, String>)wkDataObjectMapCopy.clone();
        this.filterTableColumn(obnf.wkChgType, tableName, wkKeyMapCopy, wkDataObjectMapCopy, sb);
        this.filterTableColumnForUpdate(obnf.wkChgType, tableName, wkKeyMapCopy, wkDataObjectMapCopyForUpdate, sb);
        
        String whereCondition = "";
        if (!wkKeyMapCopy.isEmpty()) {
            whereCondition = wkKeyMapCopy.toString();
        } else {
            whereCondition = wkDataObjectMapCopy.toString();
        }

        whereCondition = whereCondition.replaceAll(",", "' and ");
        whereCondition = whereCondition.replaceAll("=", "='");
        whereCondition = whereCondition.substring(1);
        whereCondition = whereCondition.substring(0, whereCondition.length() - 1);
        whereCondition = whereCondition + "'";

        String selectSQL = "select * from " + tableName + " where " + whereCondition;
        sb.append("--" + selectSQL + ";\n");

        String updateSetStr = wkDataObjectMapCopyForUpdate.toString();
        updateSetStr = updateSetStr.replaceAll(",", "' ,");
        updateSetStr = updateSetStr.replaceAll("=", "='");
        updateSetStr = updateSetStr.substring(1);
        updateSetStr = updateSetStr.substring(0, updateSetStr.length() - 1);
        updateSetStr = updateSetStr + "'";

        String updateSQL = "update " + tableName + " set " + updateSetStr + " where " + whereCondition;
        sb.append("--" + updateSQL + ";\n");

        List<String> insertFieldList = new ArrayList<String>();
        List<String> insertValueList = new ArrayList<String>();
        for (String key : wkDataObjectMapCopy.keySet()) {
            insertFieldList.add(key);
            insertValueList.add(wkDataObjectMapCopy.get(key));
        }
        String inf = insertFieldList.toString();
        inf = inf.replaceAll(" ", "");
        inf = inf.substring(1, inf.length() - 1);
        String inv = insertValueList.toString();
        inv = inv.replaceAll(" ", "");
        inv = inv.replaceAll(",", "','");
        inv = inv.substring(1, inv.length() - 1);
        inv = "'" + inv + "'";
        String insertSQL = "insert into " + tableName + " (" + inf + ") values (" + inv + ")";
        sb.append("--" + insertSQL + ";\n");
        sb.append("-------------------------------------------\n");
    }

    static class ObnfDTO {
        String wkProcessSeqNo;
        String wkNoticeType;
        String wkChgType;
        String wkSenderSiteId;
        String wkReceiverSiteId;
        String wkTableName;
        String wkNoticeDateTime;
        String wkSystemCode;
        String wkNoticeObjectMap;
        String wkOperationCode;
        Map<String, String> wkKeyMap;
        Map<String, String> wkDataObjectMap;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((wkChgType == null) ? 0 : wkChgType.hashCode());
            result = prime * result + ((wkNoticeObjectMap == null) ? 0 : wkNoticeObjectMap.hashCode());
            result = prime * result + ((wkNoticeType == null) ? 0 : wkNoticeType.hashCode());
            result = prime * result + ((wkOperationCode == null) ? 0 : wkOperationCode.hashCode());
            result = prime * result + ((wkProcessSeqNo == null) ? 0 : wkProcessSeqNo.hashCode());
            result = prime * result + ((wkReceiverSiteId == null) ? 0 : wkReceiverSiteId.hashCode());
            result = prime * result + ((wkSenderSiteId == null) ? 0 : wkSenderSiteId.hashCode());
            result = prime * result + ((wkSystemCode == null) ? 0 : wkSystemCode.hashCode());
            result = prime * result + ((wkTableName == null) ? 0 : wkTableName.hashCode());
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
            ObnfDTO other = (ObnfDTO) obj;
            if (wkChgType == null) {
                if (other.wkChgType != null)
                    return false;
            } else if (!wkChgType.equals(other.wkChgType))
                return false;
            if (wkNoticeObjectMap == null) {
                if (other.wkNoticeObjectMap != null)
                    return false;
            } else if (!wkNoticeObjectMap.equals(other.wkNoticeObjectMap))
                return false;
            if (wkNoticeType == null) {
                if (other.wkNoticeType != null)
                    return false;
            } else if (!wkNoticeType.equals(other.wkNoticeType))
                return false;
            if (wkOperationCode == null) {
                if (other.wkOperationCode != null)
                    return false;
            } else if (!wkOperationCode.equals(other.wkOperationCode))
                return false;
            if (wkProcessSeqNo == null) {
                if (other.wkProcessSeqNo != null)
                    return false;
            } else if (!wkProcessSeqNo.equals(other.wkProcessSeqNo))
                return false;
            if (wkReceiverSiteId == null) {
                if (other.wkReceiverSiteId != null)
                    return false;
            } else if (!wkReceiverSiteId.equals(other.wkReceiverSiteId))
                return false;
            if (wkSenderSiteId == null) {
                if (other.wkSenderSiteId != null)
                    return false;
            } else if (!wkSenderSiteId.equals(other.wkSenderSiteId))
                return false;
            if (wkSystemCode == null) {
                if (other.wkSystemCode != null)
                    return false;
            } else if (!wkSystemCode.equals(other.wkSystemCode))
                return false;
            if (wkTableName == null) {
                if (other.wkTableName != null)
                    return false;
            } else if (!wkTableName.equals(other.wkTableName))
                return false;
            return true;
        }



        @Override
        public String toString() {
            return "ObnfDTO [wkProcessSeqNo=" + wkProcessSeqNo + ", wkNoticeType=" + wkNoticeType + ", wkChgType="
                    + wkChgType + ", wkSenderSiteId=" + wkSenderSiteId + ", wkReceiverSiteId=" + wkReceiverSiteId
                    + ", wkTableName=" + wkTableName + ", wkNoticeDateTime=" + wkNoticeDateTime + ", wkSystemCode="
                    + wkSystemCode + ", wkNoticeObjectMap=" + wkNoticeObjectMap + ", wkOperationCode="
                    + wkOperationCode + ", wkKeyMap=" + wkKeyMap + ", wkDataObjectMap=" + wkDataObjectMap + "]";
        }
    }
    
    private void startWithAppendLine(String line, StringBuffer sb) {
        String[] strs = new String[] { "交易序號:", "tw.gov.moi.ae.jms.JmsErrorException:", "Caused by:", };
        for (String str : strs) {
            if (line.startsWith(str)) {
                sb.append("--" + line + "\n");
            }
        }
    }
    
    // ----------------------------------------------------------------- domainJar filter ↓↓↓↓↓
    File domainJar = null;
    static Map<String, TableInfo2> tableInfoMap = new HashMap<String, TableInfo2>();
    ReadDomainJarTableConfig readDomain = new ReadDomainJarTableConfig();
    static class TableInfo2 {
        String tableName;
        Set<String> pkColumns;
        Set<String> columns;
        @Override
        public String toString() {
            return "TableInfo2 [tableName=" + tableName + ", pkColumns=" + pkColumns + ", columns=" + columns + "]";
        }
    }
    private void filterTableColumn(String wkChgType, String tableName, Map<String, String> wkKeyMapCopy, Map<String, String> wkDataObjectMapCopy, StringBuilder sb){
        if(domainJar != null){
            out.println("用domainJar決定欄位!!");
            if(!tableInfoMap.containsKey(tableName)){
                try{
                    readDomain.execute(domainJar, tableName);
                    TableInfo2 tab = new TableInfo2();
                    tab.tableName = tableName;
                    tab.pkColumns = readDomain.getPkColumns();
                    tab.columns = readDomain.getColumns();
                    tableInfoMap.put(tableName, tab);
                }catch(Exception ex){
                    out.println("找domainJar錯誤:"+ex.getMessage());
                    return;
                }
            }
            TableInfo2 tab = tableInfoMap.get(tableName);
            List<String> addCol = keepKey(wkKeyMapCopy, tab.pkColumns, "");
            if (!addCol.isEmpty()) {
                sb.append("--<AU>請注意\"通報的內容缺PK欄位\",特別補上,值為空白請自行填入:" + addCol + "\n");
            }
            List<String> addCol2 = keepKey(wkDataObjectMapCopy, tab.columns, "");
            if (!addCol.isEmpty()) {
                sb.append("--<AU>請注意\"通報的內容缺Value欄位\",特別補上,值為空白請自行填入:" + addCol2 + "\n");
            }
        }
    }
    private void filterTableColumnForUpdate(String wkChgType, String tableName, Map<String, String> wkKeyMapCopy, Map<String, String> wkDataObjectMapCopy, StringBuilder sb){
        if(domainJar != null){
            out.println("用domainJar決定欄位!!");
            if(!tableInfoMap.containsKey(tableName)){
                try{
                    readDomain.execute(domainJar, tableName);
                    TableInfo2 tab = new TableInfo2();
                    tab.tableName = tableName;
                    tab.pkColumns = readDomain.getPkColumns();
                    tab.columns = readDomain.getColumns();
                    tableInfoMap.put(tableName, tab);
                }catch(Exception ex){
                    out.println("找domainJar錯誤:"+ex.getMessage());
                    return;
                }
            }
            TableInfo2 tab = tableInfoMap.get(tableName);
            List<String> addCol = keepKey(wkKeyMapCopy, tab.pkColumns, "");
            if (!addCol.isEmpty()) {
                sb.append("--<M>請注意\"通報的內容缺PK欄位\",特別補上,值為空白請自行填入:" + addCol + "\n");
            }
            List<String> removeList = new ArrayList<String>();
            Set<String> set = new HashSet<String>(wkDataObjectMapCopy.keySet());
            for(String key : set){
                if(!tab.columns.contains(key)){
                    wkDataObjectMapCopy.remove(key);
                    removeList.add(key);
                }
            }
            if (!removeList.isEmpty()) {
                sb.append("--<M>請注意\"通報的內容多Value欄位\",特別移除:" + removeList + "\n");
            }
        }
    }
    
    private <V> List<String> keepKey(Map<String,V> map, Set<String> keySet, V defaultVal){
        List<String> addCol = new ArrayList<String>();
        List<String> remCol = new ArrayList<String>();
        for(String k : keySet){
            if(!map.containsKey(k)){
                map.put(k, defaultVal);
                addCol.add(k);
            }
        }
        for(String k : map.keySet()){
            if(!keySet.contains(k)){
                remCol.add(k);
            }
        }
        for(String k : remCol){
            map.remove(k);
        }
        out.println("\t新增=>" + addCol);
        out.println("\t移除=>" + remCol);
        return addCol;
    }
    
    public String fetchDomainJar(){
        for (File f : new File("C:/Users/").listFiles()) {
            String fileName = String
                    .format("C:/Users/%s/.m2/repository/tw/gov/sris-db-domain/2.5.0-SRIS-SNAPSHOT/sris-db-domain-2.5.0-SRIS-SNAPSHOT.jar",
                            f.getName());
            File ff = new File(fileName);
            if (ff.exists()) {
                domainJar = ff;
                out.println("找到domainJar = " + domainJar.exists());
                return domainJar.getAbsolutePath();
            }
        }
        return "";
    }

    public File getDomainJar() {
        return domainJar;
    }

    public void setDomainJar(File domainJar) {
        this.domainJar = domainJar;
    }
    // ----------------------------------------------------------------- domainJar filter ↑↑↑↑↑

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }
}
