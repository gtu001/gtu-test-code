package gtu.fx.tradevan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.ObservableListBase;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

public class DBToolUI2 extends Application implements Initializable {
    
//    1.訊息種類  2.報關日期  3.傳送日期  4.提單號碼主號    5.提單號碼分號    6.統編    7.箱號
    @FXML
    ComboBox<String> srcDb;
    @FXML
    ComboBox<String> destDb;
    @FXML
    ComboBox<String> sysType;
    @FXML
    ComboBox<String> value1;
    @FXML
    DatePicker value2A;
    @FXML
    DatePicker value2B;
    @FXML
    DatePicker value3A;
    @FXML
    DatePicker value3B;
    @FXML
    TextField value35;
    @FXML
    TextField value4;
    @FXML
    TextField value5;
    @FXML
    TextField value6;
    @FXML
    TextField value7;
    @FXML
    TextField value8;
    @FXML
    TextField value9;
    @FXML
    ComboBox<String> value10;
    @FXML
    Button executeBtn;
    @FXML
    Label errorMessage;
    
    private static PrintStream out = null;
    private static PrintStream err = null;
    static {
        try {
            out = new PrintStream(new FileOutputStream(new File("c:/success_DBToolUI.log")), true);
            err = new PrintStream(new FileOutputStream(new File("c:/fail_DBToolUI.log")), true);
        } catch (FileNotFoundException e) {
            out = System.out;
            err = System.err;
        }
    }

    public static void main(String[] args) {
        launch((String[])null);
    }
    
    private Connection getConnectionFrom() throws FileNotFoundException, IOException{
        String dbType = srcDb.getValue();
        
        Pattern ptn = Pattern.compile("\\w+");
        Matcher mth = ptn.matcher(dbType);
        mth.find();
        String key = mth.group();
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File("c:/config_DBToolUI.properties")));
        String url = prop.getProperty(key+".url");
        String userName = prop.getProperty(key+".userid");
        String password = prop.getProperty(key+".password");
        String driver = prop.getProperty(key+".driver");
        Connection conn = getConnection(url, userName, password, driver);
        return conn;
    }
    
    private Connection getConnectionTo() throws FileNotFoundException, IOException{
        String dbType = destDb.getValue();
        
        Pattern ptn = Pattern.compile("\\w+");
        Matcher mth = ptn.matcher(dbType);
        mth.find();
        String key = mth.group();
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File("c:/config_DBToolUI.properties")));
        String url = prop.getProperty(key+".url");
        String userName = prop.getProperty(key+".userid");
        String password = prop.getProperty(key+".password");
        String driver = prop.getProperty(key+".driver");
        Connection conn = getConnection(url, userName, password, driver);
        return conn;
    }
    
    public void btnTestConnection(){
        errorMessage.setText("");
        StringBuilder sb = new StringBuilder();
        try{
            closeConnection(getConnectionFrom());
            sb.append("來源成功!");
        }catch(Exception ex){
            ex.printStackTrace();
            sb.append("來源失敗!");
        }
        try{
            closeConnection(getConnectionTo());
            sb.append("目的成功!");
        }catch(Exception ex){
            ex.printStackTrace();
            sb.append("目的失敗!");
        }
        errorMessage.setText(sb.toString());
    }
    
    public void changeMessage(){
        if(StringUtils.equals(value1.getValue(), "5116") || StringUtils.equals(value1.getValue(), "5204")){
            value2A.setDisable(true);
            value2B.setDisable(true);
            value2A.setValue(null);
            value2B.setValue(null);
        }else{
            value2A.setDisable(false);
            value2B.setDisable(false);
        }
        
        Define config = null;
        for (Define e : Define.values()) {
            if (e.id.equals(value1.getValue()) && e.sys.equals(sysType.getValue())) {
                config = e;
                break;
            }
        }
        if(config!=null){
            if(StringUtils.isBlank(config.ORG_ID)){
                value8.setDisable(true);
                value8.setText("");
            }else{
                value8.setDisable(false);
            }
        }
    }

    public void btnExecute() throws Exception {
        errorMessage.setText("");
        
        if (StringUtils.isBlank(value1.getValue())) {
            errorMessage.setText("請輸入訊息種類");
            return;
        }
        if (StringUtils.isBlank(srcDb.getValue())) {
            errorMessage.setText("請輸入來源資料庫");
            return;
        }
        if (StringUtils.isBlank(destDb.getValue())) {
            errorMessage.setText("請輸入目的資料庫");
            return;
        }
        if (StringUtils.isBlank(sysType.getValue())) {
            errorMessage.setText("請輸入系統別");
            return;
        }
        
        Connection conn = null;
        Connection connTo = null;
        try {
            errorMessage.setText("");
            System.out.println("#. btnExecute .s");
            conn = getConnectionFrom();
            connTo = getConnectionTo();

            for (Field f : DBToolUI2.class.getDeclaredFields()) {
                f.setAccessible(true);
                System.out.println(f.getName() + " ... " + f.get(this));
            }

            Define config = null;
            for (Define e : Define.values()) {
                if (e.id.equals(value1.getValue()) && e.sys.equals(sysType.getValue())) {
                    config = e;
                    break;
                }
            }
            if (config == null) {
                errorMessage.setText("未找到符合訊息種類");
                return;
            }

            String master_sql = "select * from " + config.master + " where 1=1 ";
            StringBuilder sb = new StringBuilder();
            boolean sendDateFormat = config.SEND_DATE_Format.equals("YYYYMMDD");
            if (value2A.getValue() != null && StringUtils.isNotBlank(String.valueOf(value2A.getValue()))) {
                sb.append(" AND " + config.DECL_DATE + " >= '" + getDate(value2A.getValue(), true) + "' ");
            }
            if (value2B.getValue() != null && StringUtils.isNotBlank(String.valueOf(value2B.getValue()))) {
                sb.append(" AND " + config.DECL_DATE + " <= '" + getDate(value2B.getValue(), true) + "' ");
            }
            if (value3A.getValue() != null && StringUtils.isNotBlank(String.valueOf(value3A.getValue()))) {
                sb.append(" AND " + config.SEND_DATE + " >= '" + getDate(value3A.getValue(), sendDateFormat) + "' ");
            }
            if (value3B.getValue() != null && StringUtils.isNotBlank(String.valueOf(value3B.getValue()))) {
                sb.append(" AND " + config.SEND_DATE + " <= '" + getDate(value3B.getValue(), sendDateFormat) + "' ");
            }
            if (StringUtils.isNotBlank(value35.getText())) {
                sb.append(" AND " + config.DECL_NO + " = '" + value35.getText() + "' ");
            }
            if (StringUtils.isNotBlank(value4.getText())) {
                sb.append(" AND " + config.MAWB + " = '" + value4.getText() + "' ");
            }
            if (StringUtils.isNotBlank(value5.getText())) {
                sb.append(" AND " + config.HAWB + " = '" + value5.getText() + "' ");
            }
            if (StringUtils.isNotBlank(value6.getText())) {
                sb.append(" AND " + config.DUTY_PAYER_BAN + " = '" + value6.getText() + "' ");
            }
            if (StringUtils.isNotBlank(value7.getText())) {
                sb.append(" AND " + config.BOX_NO + " = '" + value7.getText() + "' ");
            }
            if (StringUtils.isNotBlank(config.ORG_ID) && StringUtils.isNotBlank(value8.getText())) {
                sb.append(" AND " + config.ORG_ID + " LIKE '" + value8.getText() + "' ");
            }
            if (StringUtils.isNotBlank(value9.getText())) {
                sb.append(" AND Transaction_id Like '" + value9.getText() + "' ");
            }
            
            if (sb.length() == 0) {
                errorMessage.setText("至少輸入一項查詢條件");
                return;
            }
            

            String querySql = master_sql + sb;
            out.println("查詢SQL : " + querySql);

            masterProcess(config, querySql, conn, connTo);

        } catch (Throwable e) {
            errorMessage.setText("錯誤:" + e.getMessage());
            e.printStackTrace();
            e.printStackTrace(err);
        } finally {
            closeConnection(conn);
            closeConnection(connTo);
        }
        System.out.println("#. btnExecute .e");
    }
    
    private void masterProcess(Define config, String sql, Connection conn, Connection connTo) throws SQLException{
        int success = 0;
        int fail = 0;
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        boolean checkExists = StringUtils.equals(value10.getValue(), "是");
        
        Map<String,String> columnMap = this.getColumnList(rs);
        
        while(rs.next()){
            List<String> logStack = new ArrayList<String>();
            
            Map<String,Object> dataMap = this.getDataMap(rs, columnMap);
            String masterInsert = createInsertSql(config.master, dataMap, columnMap);
            String masterInsert4p = createInsertSql_4prepare(config.master, dataMap, columnMap);
            logStack.add(masterInsert);
            
            if(checkExists){
                String transactionId = (String)dataMap.get("TRANSACTION_ID");
                Statement stmtCheck = connTo.createStatement();
                ResultSet rsCheck = stmtCheck.executeQuery("select * from " + config.master + " where transaction_id = '"+transactionId+"' ");
                boolean hasVal = rsCheck.next();
                closeResultSet(rsCheck);
                closeStatement(stmtCheck);
                if(hasVal){
                    out.println("此筆已存在 : " + "select * from " + config.master + " where transaction_id = '"+transactionId+"' ");
                    continue;
                }
            }
            
            try{
                connTo.setAutoCommit(false);
                PreparedStatement stmtToMaster = connTo.prepareStatement(masterInsert4p);
                setStatement(dataMap, columnMap, stmtToMaster);
                stmtToMaster.executeUpdate();
                closeStatement(stmtToMaster);
                
                String transactionId = (String)dataMap.get("TRANSACTION_ID");
                out.println("master " + config.master + " : " + transactionId);
                
                for(String detailTable : config.detailList){
                    String sql2 = " select * from " + detailTable + " where transaction_id = '" + transactionId + "' ";
                    out.println("detail SQL : " + sql2);
                    Statement stmt2 = conn.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(sql2);
                    
                    Map<String,String> columnMap2 = this.getColumnList(rs2);
                    
                    int detialCount = 0;
                    while(rs2.next()){
                        Map<String,Object> dataMap2 = this.getDataMap(rs2, columnMap2);
                        String detailInsert = createInsertSql(detailTable, dataMap2, columnMap2);
                        String detailInsert2p = createInsertSql_4prepare(detailTable, dataMap2, columnMap2);
                        logStack.add(detailInsert);
                        PreparedStatement stmtToDetail = connTo.prepareStatement(detailInsert2p);
                        setStatement(dataMap2, columnMap2, stmtToDetail);
                        stmtToDetail.executeUpdate();
                        closeStatement(stmtToDetail);
                        detialCount ++;
                    }
                    out.println("detail 筆數 : " + detialCount);
                    
                    closeResultSet(rs2);
                    closeStatement(stmt2);
                }
                
                connTo.commit();
                success ++;
                
                for(String logStr : logStack){
                    out.println(logStr);
                }
            }catch(Exception ex){
                connTo.rollback();
                fail ++;
                
                for(String logStr : logStack){
                    err.println(logStr);
                }
                ex.printStackTrace();
//                err.println("-->" + ex.getMessage());
                ex.printStackTrace(err);
            }
        }
        closeResultSet(rs);
        closeStatement(stmt);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String message = "成功:"+success +", 失敗:" + fail + " - " + sdf.format(new Date());
        errorMessage.setText(message);
        out.println(message);
    }

    
    //====================================================================================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessage.setText("");
        List<String> valueList = new ArrayList<String>();
        for(Define e : Define.values()){
            if(!valueList.contains(e.id)){
                valueList.add(e.id);
            }
        }
        
        Set<String> srcSet = new LinkedHashSet<String>();
        Set<String> destSet = new LinkedHashSet<String>();
        
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(new File("c:/config_DBToolUI.properties")));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        for(Object k : prop.keySet()){
            String key = (String)k;
            key = key.substring(0, key.indexOf("."));
            srcSet.add(key);
            destSet.add(key);
            if(key.endsWith("_P")){
                destSet.remove(key);
            }
        }
        
        value1.setItems(new ObservableListBase2(valueList)); 
        srcDb.setItems(new ObservableListBase2(new ArrayList(srcSet)));  
        destDb.setItems(new ObservableListBase2(new ArrayList(destSet)));  
        sysType.setItems(new ObservableListBase2(Arrays.asList("SPIS","DCCU")));  
        value10.setItems(new ObservableListBase2(Arrays.asList("是","否"))); 
        
        errorMessage.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event arg0) {
                errorMessage.setText("");
            }
        });
    }

    @Override
    public void start(Stage paramStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = DBToolUI2.class.getResourceAsStream("test.fxml");
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(DBToolUI2.class.getResource("test.fxml"));
        TabPane page;
        try {
            page = (TabPane) loader.load(in);
        } finally {
            in.close();
        } 
        Scene scene = new Scene(page, 800, 600);
        paramStage.setScene(scene);
        paramStage.sizeToScene();
        loader.getController();
        paramStage.show();
    }
    
    private static String createInsertSql(String tableName, Map<String, Object> valmap, Map<String,String> columnMap) {
        StringBuilder sb = new StringBuilder();
        Object value = null;
        sb.append(String.format("INSERT INTO %s  (", tableName));
        for (String key : valmap.keySet()) {
            sb.append(key + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES ( ");
        for (String key : valmap.keySet()) {
            value = valmap.get(key);
            String colType = columnMap.get(key);
            
            if(value == null){
                if(colType.equalsIgnoreCase("varchar2")){
                    sb.append("null,");
                }else if(colType.equalsIgnoreCase("number")){
                    sb.append("0,");
                }else{
                    throw new RuntimeException(key + "..." + colType);
                }
            }else{
                if(colType.equalsIgnoreCase("varchar2")){
                    char[] array = value.toString().toCharArray();
                    StringBuilder sbb = new StringBuilder();
                    for(int ii = 0 ; ii < array.length; ii ++){
                        if(array[ii] == '\''){
                            sbb.append("\\'");
                        }else{
                            sbb.append(array[ii]);
                        }
                    }
                    sb.append("'" + sbb + "',");
                }else if(colType.equalsIgnoreCase("number")){
                    sb.append(value + ",");
                }else{
                    throw new RuntimeException(key + "..." + colType);
                }
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")    ");
        return sb.toString();
    }
    
    private void setStatement(Map<String, Object> dataMap, Map<String, String> columnMap, PreparedStatement stmt) throws SQLException {
        int ii = 1;
        for(String col : dataMap.keySet()){
            String colType = columnMap.get(col);
            if(colType.equalsIgnoreCase("varchar2")){
                stmt.setString(ii, (String)dataMap.get(col));
            }else if(colType.equalsIgnoreCase("number")){
                stmt.setDouble(ii, Double.parseDouble(String.valueOf(dataMap.get(col))));
            }else{
                throw new RuntimeException(col + "..." + dataMap.get(col) + "..." + colType);
            }
            ii++;
        }
    }
    
    private static String createInsertSql_4prepare(String tableName, Map<String, Object> valmap, Map<String,String> columnMap) {
        StringBuilder sb = new StringBuilder();
        Object value = null;
        sb.append(String.format("INSERT INTO %s  (", tableName));
        for (String key : valmap.keySet()) {
            sb.append(key + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES ( ");
        for (String key : valmap.keySet()) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")    ");
        return sb.toString();
    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    private String getDate(LocalDate localDate, boolean full) throws ParseException{
//        return String.format(" to_date('%s', 'yyyy.mm.dd') ", String.valueOf(localDate));
        Date d = sdf.parse(String.valueOf(localDate));
        String value = sdf2.format(d);
        if(full){
            return value;
        }
        return value.substring(2);
    }
    
    private enum Define {
//        Num("","系統別","訊息種類","報關日期","傳送日期","報單號碼","提單號碼主號","提單號碼分號","統編","箱號","公司別"),//
        Num01("5105","SPIS","EDI5105T1","DECL_DATE","SEND_DATE","YYMMDD","DECL_NO","MAWB","HAWB","DUTY_PAYER_BAN","BOX_NO","ORG_ID"),//
        Num02("5116","SPIS","EDI5116T1","DECL_DATE","SEND_DATE","YYMMDD","DECL_NO","MAWB","HAWB","DUTY_PAYER_BAN","BOX_NO","ORG_ID"),//
        Num03("5203","SPIS","EDI5203T1","DECL_DATE","SEND_DATE","YYMMDD","DECL_NO","MAWB","HAWB","EXPORTER_BAN","BOX_NO","ORG_ID"),//
        Num04("5204","SPIS","EDI5204T1","DECL_DATE","SEND_DATE","YYMMDD","DECL_NO","MAWB","HAWB","EXPORTER_BAN","BOX_NO","ORG_ID"),//
        Num05("5135","SPIS","EDI5135T1","DECL_DATE","SEND_DATE","YYMMDD","DECL_NO","MAWB","HAWB","DUTY_PAYER_BAN","BOX_NO","ORG_ID"),//
        Num06("5205","SPIS","EDI5205T1","DECL_DATE","SEND_DATE","YYMMDD","DECL_NO","MAWB","HAWB","EXPORTER_BAN","BOX_NO","ORG_ID"),//(EX:LIKE'UPS%')
        Num07("5105","DCCU","IM_5105M_N","DECL_DATE","SEND_DATE","YYYYMMDD","DECL_NO","MAWB_NO","HAWB_NO","DUTY_PAYER_ID","BROKER_BOX_NO",""),//
        Num08("5116","DCCU","IM_5116M_N","DECL_DATE","SEND_DATE","YYYYMMDD","DECL_NO","MAWB_NO","HAWB_NO","DUTY_PAYER_ID","BROKER_BOX_NO",""),//
        Num09("5203","DCCU","EX_5203M_N","DECL_DATE","SEND_DATE","YYYYMMDD","DECL_NO","MAWB_NO","HAWB_NO","EXPORTER_ID","BROKER_BOX_NO",""),//
        Num10("5204","DCCU","EX_5204M_N","DECL_DATE","SEND_DATE","YYYYMMDD","DECL_NO","MAWB_NO","HAWB_NO","EXPORTER_ID","BROKER_BOX_NO",""),//
        Num11("5135","DCCU","IM_5135M_N","DECL_DATE","SEND_DATE","YYYYMMDD","DECL_NO","MAWB_NO","HAWB_NO","DUTY_PAYER_ID","BROKER_BOX_NO",""),//
        Num12("5205","DCCU","EX_5205M_N","DECL_DATE","SEND_DATE","YYYYMMDD","DECL_NO","MAWB_NO","HAWB_NO","EXPORTER_ID","BROKER_BOX_NO",""),//

        ;
        final String id;
        final String sys;
        final String master;
        final String DECL_DATE;
        final String SEND_DATE;
        final String SEND_DATE_Format;
        final String DECL_NO;
        final String MAWB;
        final String HAWB;
        final String DUTY_PAYER_BAN;
        final String BOX_NO;
        final String ORG_ID;
        final List<String> detailList;

        Define(String... values) {
            this.id = values[0];
            this.sys = values[1];
            this.master = values[2];
            this.DECL_DATE = values[3];
            this.SEND_DATE = values[4];
            this.SEND_DATE_Format = values[5];
            this.DECL_NO = values[6];
            this.MAWB = values[7];
            this.HAWB = values[8];
            this.DUTY_PAYER_BAN = values[9];
            this.BOX_NO = values[10];
            this.ORG_ID = values[11];
            List<String> detailList = null;
            for(MasterDetail e : MasterDetail.values()){
                if(e.name().equals(this.master)){
                    detailList = e.detailList;
                    break;
                }
            }
            this.detailList = detailList;
            if(this.detailList == null){
                throw new RuntimeException(this.master);
            }
        }
    }
    
    private enum MasterDetail {
        EDI5105T1("EDI5105T2~14"),//
        EDI5116T1("EDI5116T2"),//
        EDI5203T1("EDI5203T2~7"),//
        EDI5204T1("EDI5204T2"),//
        EDI5135T1("EDI5135T2~3"),//
        EDI5205T1("EDI5205T2~3"),//
        IM_5105M_N("IM_5105D_CAR_N,IM_5105D_EXAM_N,IM_5105D_FOOD_N,IM_5105D_N,IM_5105D_SHIPPING_N,IM_5105M_ADDITIONAL_DOC_N,IM_5105M_CONTAINER_N,IM_5105M_DELIVERY_N,IM_5105M_EXAM_LABEL_N,IM_5105M_EXAM_N,IM_5105M_EXAM_REG_N,IM_5105M_INVOICE_N"),//
        IM_5116M_N("IM_5116D_N"),//
        EX_5203M_N("EX_5203D_CAR_N,EX_5203D_N,EX_5203M_ATTACH_N,EX_5203M_CONTAINER_N,EX_5203M_INVOICE_N,EX_5203M_NOTENO_N,EX_5203M_ROUTE_N"),//
        EX_5204M_N("EX_5204D_N"),//
        IM_5135M_N("IM_5135D_ATTACH_N,IM_5135D_HAWB_N,IM_5135D_N"),//
        EX_5205M_N("EX_5205D_ATTACH_N,EX_5205D_HAWB_N,EX_5205D_N"),//
        ;
        
        final List<String> detailList;
        MasterDetail(String values){
            List<String> detailList = new ArrayList<String>();
            Pattern ptn = Pattern.compile("(\\w+T)");
            if(values.indexOf("~") != -1){
                String[] valx = values.split("~");
                Matcher mth = ptn.matcher(valx[0]);
                mth.find();
                String vx = mth.group(1);
                int toVal = Integer.parseInt(valx[1]);
                for(int ii = 2; ii <= toVal; ii ++){
                    detailList.add(vx + ii);
                }
            }else if(values.indexOf(",") != -1){
                for(String s : values.split(",", -1)){
                    detailList.add(s);
                }
            }
            this.detailList = detailList;
        }
    }
    
    private Connection getConnection(String url, String username, String password, String driver) {
        BasicDataSource dbs = new BasicDataSource();
        dbs.setDriverClassName(driver);
        dbs.setUrl(url);
        dbs.setUsername(username);
        dbs.setPassword(password);
        dbs.setMaxActive(100);
        dbs.setMinIdle(30);
        Connection con = null;
        try {
            con = dbs.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }
    private static void closeResultSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private static void closeConnection(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private static void closeStatement(Statement stmt){
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class ObservableListBase2<T> extends ObservableListBase<T> {
        List<T> valueList;
        ObservableListBase2(List<T> valueList){
            this.valueList = valueList;
        }
        
        @Override
        public T get(int paramInt) {
            return valueList.get(paramInt);
        }

        @Override
        public int size() {
            return valueList.size();
        }
    }
    
    private Map<String,Object> getDataMap(ResultSet rs, Map<String,String> columnMap) throws SQLException{
        Map<String,Object> dataMap = new LinkedHashMap<String,Object>();
        for(String col : columnMap.keySet()){
            String colType = columnMap.get(col);
            if(colType.equalsIgnoreCase("varchar2")){
                try{
                    dataMap.put(col, rs.getString(col));
                }catch(Exception ex){
                    throw new RuntimeException(col + "..." + colType);
                }
            }else if(colType.equalsIgnoreCase("number")){
                dataMap.put(col, rs.getDouble(col));
            }else{
                throw new RuntimeException(col + "..." + colType);
            }
        }
        return dataMap;
    }
    private Map<String, String> getColumnList(ResultSet rs) throws SQLException{
        Map<String, String> columnMap = new LinkedHashMap<String, String> ();
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String col = rsmd.getColumnName(i);
            String colType = rsmd.getColumnTypeName(i);
            columnMap.put(col, colType);
        }
        return columnMap;
    }
}
