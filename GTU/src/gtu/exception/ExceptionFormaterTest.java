package gtu.exception;

import gtu.console.SystemInUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ExceptionFormaterTest {

    public static void main(String[] args) throws IOException{
        String exMessage = SystemInUtil.readContent();
        Pattern ptn = Pattern.compile("(at\\s|Caused\\sby\\:)");
        Scanner scan = new Scanner(exMessage);
        System.out.println(exMessage.substring(0, exMessage.indexOf("at ")));
        scan.useDelimiter(ptn);
        while(scan.hasNext()){
            System.out.print(scan.findInLine(ptn));
            System.out.println(scan.next());
        }
        scan.close();
    }

    /*
PA-004-繳費通知單-Multistream 1 gainIds failed! org.springframework.jdbc.UncategorizedSQLException: StatementCallback; uncategorized SQLException for SQL [select policy_id from (SELECT tbms.policy_id from t_batch_multi_streaming tbms where tbms.trans_code = 'PREM_NOTICE' and tbms.stream_value = ? )]; SQL state [72000]; error code [1008]; ORA-01008: not all variables bound ; nested exception is java.sql.SQLException: ORA-01008: not all variables bound at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:83) at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:80) at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:80) at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:407) at org.springframework.jdbc.core.JdbcTemplate.query(JdbcTemplate.java:456) at org.springframework.jdbc.core.JdbcTemplate.query(JdbcTemplate.java:464) at org.springframework.jdbc.core.JdbcTemplate.queryForList(JdbcTemplate.java:491) at com.ebao.ls.pub.batch.GenericBatchLetterService.prepareIds(GenericBatchLetterService.java:90) at com.ebao.ls.batch.TGLBasePieceableJob.gainIds(TGLBasePieceableJob.java:45) at com.ebao.pub.batch.exe.JobExecutor.processGainIds(JobExecutor.java:397) at com.ebao.pub.batch.exe.JobExecutor.access$000(JobExecutor.java:56) at com.ebao.pub.batch.exe.JobExecutor$2.run(JobExecutor.java:188) at com.ebao.pub.batch.util.TransactionTemplate.runInTransaction(TransactionTemplate.java:28) at com.ebao.pub.batch.exe.JobExecutor.executeSub(JobExecutor.java:179) at com.ebao.pub.batch.exe.JobExecutor.run(JobExecutor.java:74) at java.lang.Thread.run(Thread.java:784) Caused by: java.sql.SQLException: ORA-01008: not all variables bound at oracle.jdbc.driver.T4CTTIoer.processError(T4CTTIoer.java:440) at oracle.jdbc.driver.T
*/  
}
