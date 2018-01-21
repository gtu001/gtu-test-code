package gtu._work;

import gtu._work.ExcelToSql.SysDateData;
import gtu._work.ExcelToSql.SysTimeData;
import gtu.apache.BeanUtilGtu;
import gtu.file.FileUtil;
import gtu.hibernate.ReadDomainJarTableConfig;
import gtu.log.Log;
import gtu.string.StringUtilForDb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.google.common.collect.HashMultimap;

public class ObnfRepairDBBatch_forAndyThai {
    
    ReadDomainJarTableConfig readDomain = new ReadDomainJarTableConfig();
    File domainJar;
    
    public static void main(String[] args) throws IOException{
        ObnfRepairDBBatch_forAndyThai test = new ObnfRepairDBBatch_forAndyThai();
        test.execute(
//                new File("H:/TXGLRL1402281419397834_RC_00000000_33.xml"),
                new File("C:/Users/gtu001/Desktop/宜蘭縣員山鄉1030331門牌整編生效XML"),
                new File(
                        "C:/Users/gtu001/.m2/repository/tw/gov/sris-db-domain/2.5.0-SRIS-SNAPSHOT/sris-db-domain-2.5.0-SRIS-SNAPSHOT.jar"),
                new JTextArea());
        System.out.println("done...");
    }
    
    JTextArea logArea;
    Pattern fileNamePattern = Pattern.compile("TX[A-Z]{4}\\d{16}_([A-Z]{2})_\\d+_\\d+");
    File mkDir;

    public void execute(File exceptionErrorDir, File domainJar, JTextArea logArea) {
        try {
            Log.Setting.FULL.apply();
            this.domainJar = domainJar;
            mkDir = new File(FileUtil.DESKTOP_DIR, "亞中恐怖SQL");
            if(!mkDir.exists()){
                mkDir.mkdirs();
            }
            
            this.logArea = logArea;
            this.logArea.setText("");
            
            HashMultimap<String, File> fileMap = HashMultimap.<String, File>create();
            Matcher matcher = null;
            if(exceptionErrorDir.isDirectory()){
                if(exceptionErrorDir.listFiles()!=null){
                    for (File f : exceptionErrorDir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".xml");
                        }
                    })) {
                        matcher = fileNamePattern.matcher(f.getName());
                        if(matcher.find()){
                            fileMap.put(matcher.group(1), f);
                        }else{
                            logAppend("[警告]未處理 : " + f.getName() + " > 檔名格式不符");
                            this.logArea.append("[警告]未處理 : " + f.getName() + " > 檔名格式不符\n");
                        }
                    }
                    for(String key : fileMap.keySet()){
                        List<File> fileList = new ArrayList<File>(fileMap.get(key));
                        this.logArea.append("================================================\n");
                        this.logArea.append("目前處理 >" + key + "\n");
                        batchReaderInfo(key, fileList);
                    }
                }
            }else{
                batchReaderInfo("", Arrays.asList(exceptionErrorDir));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    static int currentLine = 0;
    static File currentOutputFile;
    private BufferedWriter createNewFile(String sql){
        try {
            currentLine += StringUtils.defaultString(sql).split("\n").length;
            if(currentLine > 2500){
                Pattern pattern = Pattern.compile("([A-Z]{2}\\_\\d+\\_)(\\d+)\\.sql");
                Matcher matcher = pattern.matcher(currentOutputFile.getName());
                matcher.matches();
                String fileName = matcher.group(1) + String.valueOf(Integer.parseInt(matcher.group(2)) + 1) + ".sql";
                File file = new File(currentOutputFile.getParentFile(), fileName);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                        "utf8"));
                currentLine = 0;
                currentOutputFile = file;
                return writer;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void batchReaderInfo(String taskName, List<File> matchMessageFileList) {
        File tempFile = null;
        try {
            taskName = StringUtils.defaultIfEmpty(taskName, "OutputSQL");
            File outputSqlFile = new File(mkDir, taskName + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + "_1.sql");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputSqlFile),
                    "utf8"));
            
            currentOutputFile = outputSqlFile;

            int allSize = matchMessageFileList.size();
            int index = 0;
            for (File f : matchMessageFileList) {
                
                this.logArea.append("正在處理(" + (index + 1) + "/" + allSize + ") : " + f.getName() + "\n");
                
                tempFile = f;
                StringBuffer sb = new StringBuffer();
                LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(f), "utf8"));
                for (String line = null; (line = reader.readLine()) != null;) {
                    sb.append(line + "\n");
                }
                reader.close();

//                logAppend(sb);
                String sql = readInfo(sb.toString());

//                writer.write("--##########################################################################Start");
                writer.newLine();
                writer.write("--第(" + (index + 1) + "/" + allSize + ")筆 : " + f.getName());
                writer.newLine();
                writer.write(sql);
                writer.newLine();
//                writer.write("--##########################################################################End");
                writer.newLine();
                
                BufferedWriter newWriter = this.createNewFile(sql);
                if(newWriter != null){
                    writer.flush();
                    writer.close();
                    writer = newWriter;
                }
                
                this.logArea.append("==>處理完成(" + (index + 1) + "/" + allSize + ") : " + f.getName() + "\n");
                ++index;
            }

            writer.flush();
            writer.close();
        } catch (Exception ex) {
            logAppend("error file => " + tempFile);
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

            List<ObnfDTO> obnfList = new ArrayList<ObnfDTO>();
            List<Element> nodeList = (List<Element>) doc
                    .selectNodes("//list/tw.gov.moi.rs.dto.ObnfDTO");
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
                                logAppend("obnf.wkNoticeDateTime取得錯誤!");
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
//                sb.append("-------------------------------------------\n");
//                sb.append("--" + obnf.toString() + "\n");
                logAppend(obnf.toString());
                processSQL(obnf, sb);
                if(obnf.wkTableName.equalsIgnoreCase("Rcdf019mType") || obnf.wkTableName.equalsIgnoreCase("Rrdf019mType")){
                    processSQL_forRcdf019m(obnf, sb);
                }
//                sb.append("-------------------------------------------\n");
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
                            logAppend(ii + "---->" + key2 + " = " + value);
                            Map<String,String> repairMap = (Map<String,String>)BeanUtilGtu.getPropertyByField(obnfDTO, pe.mapName);
                            repairMap.remove(key);
                            repairMap.put(key2, value);
                        }
                    }
                }
            }
        }
    }
    
    static Map<String, TableInfo2> tableInfoMap = new HashMap<String, TableInfo2>();
    static class TableInfo2 {
        String tableName;
        Set<String> pkColumns;
        Set<String> columns;
    }
    private void filterTableColumn(String wkChgType, String tableName, Map<String, String> wkKeyMapCopy, Map<String, String> wkDataObjectMapCopy, StringBuilder sb){
        if(domainJar != null){
            if(!tableInfoMap.containsKey(tableName)){
                readDomain.execute(domainJar, tableName);
                TableInfo2 tab = new TableInfo2();
                tab.pkColumns = readDomain.getPkColumns();
                tab.columns = readDomain.getColumns();
                tableInfoMap.put(tableName, tab);
            }
            TableInfo2 tab = tableInfoMap.get(tableName);
            logAppend(wkChgType + "==>" + tableName);
            if(wkChgType.matches("[mMdD]")){
                logAppend("==>PK");
                //----------------------------------------寫死↓↓↓↓↓
                if(tableName.equalsIgnoreCase("RCDF020M") || tableName.equalsIgnoreCase("RRDF020M")){
                    if(wkKeyMapCopy.containsKey("site_id")){
                        wkKeyMapCopy.put("area_code", wkKeyMapCopy.get("site_id"));
                    }
                }
                //----------------------------------------寫死↑↑↑↑↑
                List<String> addCol = keepKey(wkKeyMapCopy, tab.pkColumns, "");
//                if(!addCol.isEmpty()){
//                    sb.append("--請注意\"通報的內容缺PK欄位\",特別補上,值為空白請自行填入:" + addCol + "\n");
//                }
                //----------------------------------------寫死↓↓↓↓↓
                wkDataObjectMapCopy.remove("site_id");
                wkDataObjectMapCopy.remove("admin_office_code");
                wkDataObjectMapCopy.remove("area_code");
                //----------------------------------------寫死↑↑↑↑↑
            }else{
                logAppend("==>Column");
                keepKey(wkDataObjectMapCopy, tab.columns, "");
            }
        }
    }

    private void processSQL(ObnfDTO obnf, StringBuilder sb) {
        Map<String, String> wkKeyMapCopy = new LinkedHashMap<String, String>();
        Map<String, String> wkDataObjectMapCopy = new LinkedHashMap<String, String>();
        
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
//        sb.append("--wkKeyMap : " + obnf.wkKeyMap + "\n");
//        sb.append("--wkDataObjectMap : " + obnf.wkDataObjectMap + "\n");

        String tableName = obnf.wkTableName.replaceAll("Type", "");
        this.filterTableColumn(obnf.wkChgType, tableName, wkKeyMapCopy, wkDataObjectMapCopy, sb);
        
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

        String deleteSQL = "delete from " + tableName + " where " + whereCondition;
        if(obnf.wkChgType.equalsIgnoreCase("D")){
            sb.append(deleteSQL + ";\n");
        }

        String updateSetStr = wkDataObjectMapCopy.toString();
        updateSetStr = updateSetStr.replaceAll(",", "' ,");
        updateSetStr = updateSetStr.replaceAll("=", "='");
        updateSetStr = updateSetStr.substring(1);
        updateSetStr = updateSetStr.substring(0, updateSetStr.length() - 1);
        updateSetStr = updateSetStr + "'";

        String updateSQL = "update " + tableName + " set " + updateSetStr + " where " + whereCondition;
        if(obnf.wkChgType.equalsIgnoreCase("M")){
            sb.append(updateSQL + ";\n");
        }

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
        if(obnf.wkChgType.equalsIgnoreCase("A")){
            sb.append(insertSQL + ";\n");
        }
        if(obnf.wkChgType.equalsIgnoreCase("U")){
            sb.append(deleteSQL + ";--U\n");
            sb.append(insertSQL + ";\n");
        }
    }
    
    private void processSQL_forRcdf019m(ObnfDTO obnf, StringBuilder sb) {
        Map<String, String> wkKeyMapCopy = new LinkedHashMap<String, String>();
        Map<String, String> wkDataObjectMapCopy = new LinkedHashMap<String, String>();

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

        //----------------------------------------------------------------------
        String oldAddrTxCode = wkDataObjectMapCopy.get("old_addr_tx_code");
        String newAddrTxCode = wkDataObjectMapCopy.get("new_addr_tx_code");
        
        Map<String, String> newValueMapOld = new HashMap<String, String>();
        Map<String, String> newPkMapOld = new HashMap<String, String>();
        Map<String, String> newValueMapNew = new HashMap<String, String>();
        Map<String, String> newPkMapNew = new HashMap<String, String>();
        parseRcdf019mValueMap(false, wkKeyMapCopy, wkDataObjectMapCopy, newPkMapOld, newValueMapOld);
        parseRcdf019mValueMap(true, wkKeyMapCopy, wkDataObjectMapCopy, newPkMapNew, newValueMapNew);
//        sb.append("--newPkMapOld : " + newPkMapOld + "\n");
//        sb.append("--newValueMapOld : " + newValueMapOld + "\n");
//        sb.append("--newPkMapNew : " + newPkMapNew + "\n");
//        sb.append("--newValueMapNew : " + newValueMapNew + "\n");
        
        String tableName = obnf.wkTableName.toLowerCase();
        if(tableName.startsWith("rr")){
            tableName = "RRDF020M";
        }else if(tableName.startsWith("rc")){
            tableName = "RCDF020M";
        }
        
//        sb.append("--old_addr_tx_code = "+oldAddrTxCode+"\n");
        this.processSQL_forRcdf020m_OK(oldAddrTxCode, tableName, newPkMapOld, newValueMapOld, sb);
        
//        sb.append("--new_addr_tx_code = "+newAddrTxCode+"\n");
        this.processSQL_forRcdf020m_OK(newAddrTxCode, tableName, newPkMapNew, newValueMapNew, sb);
        //----------------------------------------------------------------------
    }
    
    private void processSQL_forRcdf020m_OK(String wkChgType, String tableName, //
            Map<String, String> wkKeyMapCopy, Map<String, String> wkDataObjectMapCopy, StringBuilder sb) {
        this.filterTableColumn(wkChgType, tableName, wkKeyMapCopy, wkDataObjectMapCopy, sb);
        
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

        String deleteSQL = "delete from " + tableName + " where " + whereCondition;
        if(wkChgType.equalsIgnoreCase("D")){
            sb.append(deleteSQL + ";\n");
        }

        String updateSetStr = wkDataObjectMapCopy.toString();
        updateSetStr = updateSetStr.replaceAll(",", "' ,");
        updateSetStr = updateSetStr.replaceAll("=", "='");
        updateSetStr = updateSetStr.substring(1);
        updateSetStr = updateSetStr.substring(0, updateSetStr.length() - 1);
        updateSetStr = updateSetStr + "'";

        String updateSQL = "update " + tableName + " set " + updateSetStr + " where " + whereCondition;
        if(wkChgType.equalsIgnoreCase("M")){
            sb.append(updateSQL + ";\n");
        }

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
        if(wkChgType.equalsIgnoreCase("A")){
            sb.append(insertSQL + ";\n");
        }
        if(wkChgType.equalsIgnoreCase("U")){
            sb.append(deleteSQL + ";--U\n");
            sb.append(insertSQL + ";\n");
        }
    }
    
    private void parseRcdf019mValueMap(boolean isNew, //
            Map<String, String> wkKeyMapCopy, Map<String, String> wkDataObjectMapCopy, //
            Map<String, String> newPkMap, Map<String, String> newValueMap){
        for (ColumnDefineRcdf020m c : ColumnDefineRcdf020m.values()) {
            String value = "";
            String columnName = c.mappingColumn;
            if(columnName == null){
                columnName = c.column;
            }else if(columnName.indexOf(",") != -1){
                String[] tmp = columnName.split(",");
                columnName = isNew?tmp[0]:tmp[1];
            }
            if (c.defalutVal == null) {
                value = wkDataObjectMapCopy.get(columnName);
            }else if(c.defalutVal instanceof gtu._work.ExcelToSql.GetDefineData){
                value = (String)((gtu._work.ExcelToSql.GetDefineData)c.defalutVal).getValue();
            }else{
                value = (String)c.defalutVal;
            }
            newValueMap.put(c.column, value);
            if(c.isPk){
                newPkMap.put(c.column, value);
            }
        }
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
    
    enum ColumnDefineRcdf020m {
        Column1("site_id", null, null, true), //
        Column2("serial_no", null, new ExcelToSql().new SeqNoData22(), false), //
        Column3("seq_no", null, new ExcelToSql().new SeqNoData7(), false), //
        Column4("area_code", null, null, false), //
        Column5("admin_office_code", null, null, false), //
        Column6("village", "new_village,old_village", null, true), //
        Column7("neighbor", "new_neighbor,old_neighbor", null, true), //
        Column8("street_doorplate", "new_street_doorplate,old_street_doorplate", null, true), //
        Column9("street", "new_street,old_street", null, false), //
        Column10("section", "new_section,old_section", null, false), //
        Column11("area", "new_area,old_area", null, false), //
        Column12("lane", "new_lane,old_lane", null, false), //
        Column13("alley", "new_alley,old_alley", null, false), //
        Column14("door_no", "new_door_no,old_door_no", null, false), //
        Column15("floor_no", "new_floor_no,old_floor_no", null, false), //
        Column16("ori_cs", null, "", false), //
        Column17("ori_x", null, "", false), //
        Column18("ori_y", null, "", false), //
        Column19("x97", null, "", false), //
        Column20("y97", null, "", false), //
        Column21("register_date", "modify_date", null, false), //
        Column22("register_kind", null, "7", false), //
        Column23("reserved_field", null, "", false), //
        Column24("notice_date", null, new SysDateData(), false), //
        Column25("notice_time", null, new SysTimeData(), false), //
        Column26("original_code", "site_id", null, false), //
        Column27("notallowset", null, "", false), //
        Column28("mark", null, "", false), //
        ;
        final String column;//目的欄位名
        final String mappingColumn;//要轉換的欄位名稱(null則用column)
        final Object defalutVal;//預設值,可使用GetDefineData做特殊預設值處理
        final boolean isPk;//是否PK
        int index = -1;//對應excel的pos,用程式抓Row取得對應index
        ColumnDefineRcdf020m(String column, String mappingColumn, Object defalutVal, boolean isPk) {
            this.column = StringUtils.trim(column);
            this.mappingColumn = StringUtils.trim(mappingColumn);
            this.defalutVal = defalutVal;
            this.isPk = isPk;
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
        logAppend("\t新增=>" + addCol);
        logAppend("\t移除=>" + remCol);
        return addCol;
    }
    
    private void logAppend(String message){
        gtu.log.Log.debug(message);//TODO
    }
}
