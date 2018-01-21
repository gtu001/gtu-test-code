package gtu.db.oracle;

import java.text.MessageFormat;

/**
 * 貼到 tnsnames.ora
 */
public class Tnsnames_ora_Creater {

    public static void main(String[] args) {
//        String tnsName = "R5_ED";
//        String serviceName = "ebouat2";
//        String ip = "10.67.67.70";
//        String port = "1521";
//        String username = "ls_uat_read";
//        String password = "ls_uat_read_only";
        
//        String tnsName = "R5_DEV";
//        String serviceName = "ebosit1";
//        String ip = "10.67.67.86";
//        String port = "1521";
//        String username = "ls_sit_dev";
//        String password = "ls_sit_devpwd";
        
//        String tnsName = "R5_ILP";
//        String serviceName = "tgldcbak";
//        String ip = "ploodse-scan.transglobe.com.tw";
//        String port = "1521";
//        String username = "TGL_TAR_PRE_read";
//        String password = "TGL_TAR_PRE_read_only";
        
        String tnsName = "DCS_122";
        String serviceName = "IBTDCS1";
        String ip = "122.116.167.154";
        String port = "1521";
        String username = "sysadm";
        String password = "123456";
        
        boolean isService = false;
        
        //--------------------------------------------------------------------------------------
        
        String serviceOrSid = "SERVICE_NAME";
        if(!isService){
            serviceOrSid = "SID";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" {0}  =                                                  \n");
        sb.append("  (DESCRIPTION =                                          \n");
        sb.append("    (ADDRESS_LIST =                                       \n");
        sb.append("      (ADDRESS = (PROTOCOL = TCP)(Host = {1})(Port ={2}))     \n");
        sb.append("    )                                                     \n");
        sb.append("  (CONNECT_DATA =                                         \n");
        sb.append("    ("+serviceOrSid+" ={3})                                     \n");
        sb.append("  )                                                       \n");
        sb.append(" )                                                        \n");

        String result = MessageFormat.format(sb.toString(), new Object[] { tnsName, ip, port, serviceName });
        System.out.println(result);

        System.out.println("連線指令 : sqlplus " + username + "/" + password + "@" + tnsName);
        
        
        String format2 = "連線指令 : sqlplus {0}/{1}@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(Host={2})(Port={3}))(CONNECT_DATA=({4}={5})))";
        String result2 = MessageFormat.format(format2, new Object[] { username, password, ip, port, serviceOrSid, serviceName});
        System.out.println(result2);
        
        
        String format3 = "連線指令 : sqlplus {0}/{1}@{2}:{3}/{4}";
        String result3 = MessageFormat.format(format3, new Object[] { username, password, ip, port, serviceName});
        System.out.println(result3);
    }

}
