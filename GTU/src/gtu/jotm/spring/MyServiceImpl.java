package gtu.jotm.spring;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MyServiceImpl {
    private UserJdbcDao userDao;

    public void setUserDao(UserJdbcDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = false)
    public void addGrade() {
        //假如希望两个添加数据的事务，其中有一个添加失败时，均回滚，   
        //由于两个操作是在两个不同的数据库上进行的，故要JTA事务来进行管理   
        //否则，将会出现添加一个，回滚一个的情形   
        userDao.addUser();
    }
}