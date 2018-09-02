package gtu._work;

import gtu.apache.BeanUtilGtu;
import gtu.file.FileUtil;
import gtu.string.StringUtilForDb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ObnfRepairDBBatch_forJet1 {
    
    private PrintStream out = System.out;
    
    public static void main(String[] args) {
        ObnfRepairDBBatch_forJet1 test = new ObnfRepairDBBatch_forJet1();
        System.out.println("done...");
    }
    
    private Set<String> excludePersonSet = new HashSet<String>();
    public void execute(String excludePersonId, File xmlFile){
        StringTokenizer tok = new StringTokenizer(excludePersonId);
        while(tok.hasMoreElements()){
            excludePersonSet.add((String)tok.nextElement());
        }
        this.readInfo(xmlFile);
    }

    private void readInfo(File xmlFile) {
        try {
            Document doc = new SAXReader().read(xmlFile);

            List<ObnfDTO> obnfList = new ArrayList<ObnfDTO>();
            List<Element> nodeList = (List<Element>) doc
                    .selectNodes("//tw.gov.moi.rs.dto.SendMessageDTO/wkDataList/tw.gov.moi.rs.dto.ObnfDTO");
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

            File outputFile = new File(xmlFile.getParent(), "outputDelete_"+FileUtil.getNameNoSubName(xmlFile)+".log");
            File outputFile2 = new File(xmlFile.getParent(), "outputSelect_"+FileUtil.getNameNoSubName(xmlFile)+".log");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
            for (ObnfDTO obnf : obnfList) {
                StringBuilder sb = new StringBuilder();
                sb.append("--start["+obnf.wkSystemCode+"]-------------------------------------\n");
                processSQL(obnf, sb);
                sb.append("--end-------------------------------------------\n");
                writer.write(sb.toString());
            }
            writer.flush();
            writer.close();
            
            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile2), "utf8"));
            String sql = "select person_id from rrdf004m where person_id = '%s' and site_id = '%s' and personal_mark = '0' union all \n";
            for(DeletePerson person : personIdSet){
                writer2.write(String.format(sql, person.personId, person.siteId));
            }
            writer2.flush();
            writer2.close();
        } catch (Exception ex) {
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
    
    //------------------------------------------------------------------------------↓↓↓↓↓ 正是邏輯
    private Set<DeletePerson> personIdSet = new LinkedHashSet<DeletePerson>();
    private void printPersonDelete(DeletePerson person, StringBuilder sb){
        String personId = person.personId;
        String siteId = person.siteId;
        if(person.systemCode.equalsIgnoreCase("RC")){
            sb.append(String.format( "delete from RRDF004M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDF005M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDF008M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDF040M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDF047M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM01M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM02M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM04M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM05M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM08M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM09M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM10M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM11M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
        } else if(person.systemCode.equalsIgnoreCase("RR")){
            sb.append(String.format("delete from RRDF004M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            sb.append(String.format( "delete from RRDF005M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDF006M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDF007M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format("delete from RRDF008M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            //sb.append(String.format( "delete from RRDF030T where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format("delete from RRDF040M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            sb.append(String.format("delete from RRDF047M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            sb.append(String.format("delete from RRDFM01M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            sb.append(String.format("delete from RRDFM02M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            sb.append(String.format("delete from RRDFM03M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            sb.append(String.format("delete from RRDFM04M where person_id = '%s' and site_id = '%s';\n"   , personId, siteId));
            sb.append(String.format( "delete from RRDFM05M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM06M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM08M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM09M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM10M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
            sb.append(String.format( "delete from RRDFM11M where person_id = '%s' and site_id = '%s';\n"  , personId, siteId));
        }
    }
    
    private void processSQL(ObnfDTO obnf, StringBuilder sb){
        if(obnf.wkSystemCode.matches("(RC|RR)")){
            if(obnf.wkChgType.equalsIgnoreCase("E")){
                String personId = obnf.wkKeyMap.get("personId");
                String siteId = obnf.wkKeyMap.get("siteId");
                if(!excludePersonSet.contains(personId)){
                    DeletePerson person = new DeletePerson();
                    person.personId = personId;
                    person.siteId = siteId;
                    person.systemCode = obnf.wkSystemCode;
                    personIdSet.add(person);
                    this.printPersonDelete(person, sb);
                }else{
                    sb.append("--存在於排除清單 :" + personId);
                }
            }else{
                boolean insert = obnf.wkChgType.matches("[aA]");
                boolean update = obnf.wkChgType.matches("[mM]");
                boolean delete = obnf.wkChgType.matches("[dD]");
                boolean saveOrUpdate = obnf.wkChgType.matches("[uU]");
                if(!saveOrUpdate){
                    this.processSQL(obnf.wkTableName, obnf.wkKeyMap, obnf.wkDataObjectMap, sb, insert, update, delete);
                }else{
                    this.processSQL(obnf.wkTableName, obnf.wkKeyMap, obnf.wkDataObjectMap, sb, false, false, true);
                    this.processSQL(obnf.wkTableName, obnf.wkKeyMap, obnf.wkDataObjectMap, sb, true, false, false);
                }
            }
        } else{
            throw new RuntimeException("錯誤的SystemCode:" + obnf.wkSystemCode);
        }
    }

    private void processSQL(String tableName, Map<String, String> wkKeyMap, Map<String, String> wkDataObjectMap, StringBuilder sb, boolean insert, boolean update, boolean delete) {
        Map<String, String> wkKeyMapCopy = new LinkedHashMap<String, String>();
        Map<String, String> wkDataObjectMapCopy = new LinkedHashMap<String, String>();
        
        for (String key : wkKeyMap.keySet()) {
            if (key.equals("serialVersionUID")) {
                continue;
            }
            String newKey = StringUtilForDb.javaToDbField(key);
            wkKeyMapCopy.put(newKey, wkKeyMap.get(key));
        }
        for (String key : wkDataObjectMap.keySet()) {
            if (key.equals("serialVersionUID")) {
                continue;
            }
            String newKey = StringUtilForDb.javaToDbField(key);
            wkDataObjectMapCopy.put(newKey, wkDataObjectMap.get(key));
        }

        tableName = tableName.replaceAll("Type", "");
        
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
        if(delete){
            sb.append(deleteSQL + ";\n");
        }

        String updateSetStr = wkDataObjectMapCopy.toString();
        updateSetStr = updateSetStr.replaceAll(",", "' ,");
        updateSetStr = updateSetStr.replaceAll("=", "='");
        updateSetStr = updateSetStr.substring(1);
        updateSetStr = updateSetStr.substring(0, updateSetStr.length() - 1);
        updateSetStr = updateSetStr + "'";

        String updateSQL = "update " + tableName + " set " + updateSetStr + " where " + whereCondition;
        if(update){
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
        if(insert){
            sb.append(insertSQL + ";\n");
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

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }
    
    static class DeletePerson {
        String personId;
        String siteId;
        String systemCode;
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((personId == null) ? 0 : personId.hashCode());
            result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
            result = prime * result + ((systemCode == null) ? 0 : systemCode.hashCode());
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
            DeletePerson other = (DeletePerson) obj;
            if (personId == null) {
                if (other.personId != null)
                    return false;
            } else if (!personId.equals(other.personId))
                return false;
            if (siteId == null) {
                if (other.siteId != null)
                    return false;
            } else if (!siteId.equals(other.siteId))
                return false;
            if (systemCode == null) {
                if (other.systemCode != null)
                    return false;
            } else if (!systemCode.equals(other.systemCode))
                return false;
            return true;
        }
    }
}
