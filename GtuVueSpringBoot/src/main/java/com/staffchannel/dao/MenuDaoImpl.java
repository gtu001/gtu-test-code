package com.staffchannel.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.staffchannel.model.Menu;
import com.staffchannel.model.User;
import com.staffchannel.model.UserProfile;

@Repository("menuDao")
public class MenuDaoImpl extends AbstractDao<Integer, Menu> implements MenuDao {

	static final Logger logger = LoggerFactory.getLogger(MenuDaoImpl.class);

	public Menu findById(int id) {
		Menu menu = getByKey(id);
		if(menu!=null){
			Hibernate.initialize(menu.getUserProfile());
		}
		return getByKey(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Menu> findBySSO(String sso) {
		return getSession().createCriteria(User.class)
                .createAlias("userProfile", "userProfile")
                .createAlias("userProfile.user", "user")
                .add(Restrictions.eq("user.ssoId", sso))
                .list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Menu> findAllMenus() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<Menu> menuList = (List<Menu>) criteria.list();
		
		// No need to fetch userProfiles since we are not showing them on list page. Let them lazy load. 
		// Uncomment below lines for eagerly fetching of userProfiles if you want.
		/*
		for(User user : users){
			Hibernate.initialize(user.getUserProfiles());
		}*/
		return menuList;
	}

	public void save(Menu meun) {
		persist (meun);
	}
	
	public void delete(int id){
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("id", id));
		Menu meun = (Menu) criteria.uniqueResult();
		delete(	meun );	
	}
	
}
