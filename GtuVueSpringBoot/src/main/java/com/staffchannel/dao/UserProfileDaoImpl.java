package com.staffchannel.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.staffchannel.model.User;
import com.staffchannel.model.UserProfile;



@Repository("userProfileDao")
public class UserProfileDaoImpl extends AbstractDao<Integer, UserProfile>implements UserProfileDao{

	public UserProfile findById(int id) {
		UserProfile userProfile = getByKey(id);
		if(userProfile!=null){
			Hibernate.initialize(userProfile.getMenuList());
		}
		return userProfile;
	}

	public UserProfile findByType(String type) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("type", type));
		UserProfile userProfile = (UserProfile) crit.uniqueResult();
		if(userProfile!=null){
			Hibernate.initialize(userProfile.getMenuList());
		}
		return userProfile;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserProfile> findAll(){
		Criteria crit = createEntityCriteria();
		crit.addOrder(Order.asc("type"));
		return (List<UserProfile>)crit.list();
	}
	
	public void save(UserProfile userProfile){
		merge(userProfile);
	}
	
}
