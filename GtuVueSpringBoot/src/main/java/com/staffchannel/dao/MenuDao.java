package com.staffchannel.dao;

import java.util.List;

import com.staffchannel.model.Menu;
import com.staffchannel.model.User;
import com.staffchannel.model.UserProfile;


public interface MenuDao {
	
	Menu findById(int id);
	
	List<Menu> findBySSO(String sso);
	
	List<Menu> findAllMenus();
	
	void save(Menu menu);
	
	void delete(int id);
	
}

