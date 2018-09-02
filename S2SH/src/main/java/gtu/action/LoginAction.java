package gtu.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import gtu.common.util.WebUtil;
import gtu.constant.Constants;
import gtu.model.dao.UserDaoImpl;
import gtu.model.entity.User;

@Controller
public class LoginAction extends ActionSupport{
    
    private static Logger logger = Logger.getLogger(LoginAction.class);
    
    @Resource(name="userDao")
    private UserDaoImpl userDao;
    
    private User user;
    
    public String expired(){
        return "expired";
    }

    public String execute() {
        return Action.SUCCESS;
    }
    
    public String goCreate() {
        return "goCreate";
    }
    
    public String doCreate(){
        logger.debug("---" + ReflectionToStringBuilder.toString(user));
        userDao.insert(user);
        user = new User();
        return "input";
    }
    
    public String input(){
        logger.debug("---" + ReflectionToStringBuilder.toString(user));
        int loginSize = userDao.querySize(user.getUserId(), user.getPassword());
        if(loginSize == 0){
            this.addActionError("login fail!");
            logger.debug("login fail!");
        }else{
            this.addActionMessage("login success");
            logger.debug("login success!");
            user = userDao.findByUserId(user.getUserId());
            WebUtil.getSession().setAttribute(Constants.USER_KEY, user);
        }
        return "index";
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
