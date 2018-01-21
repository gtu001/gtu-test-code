package gtu.spring.transcation;

import java.sql.SQLException;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRES_NEW, //事務傳播行為
isolation = Isolation.READ_COMMITTED,//事務隔離級別
readOnly = true,//事務讀寫性
timeout = 60,//超過時間
rollbackFor = { SQLException.class },//一組異常類，遇到時進行回滾
rollbackForClassName = { "Exception" },//一組異常類名，遇到時進行回滾
noRollbackFor = { RuntimeException.class }, //一組異常類，遇到時不回滾
noRollbackForClassName = {})
//一組異常類名，遇到時不回滾
public class TransactionalTest {

    @Transactional(readOnly = true)
    //提供額外的註解信息，將覆蓋類級註解
    public void getForum() {
        //TODO
    }
}
