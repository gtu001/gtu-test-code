package com.staffchannel.service;

import java.util.List;

import com.staffchannel.model.Menu;


public interface MenuService {
	
	Menu findById(int id);
	
	List<Menu> findBySSO(String sso);

	List<Menu> findAllMenus(); 

	void save(Menu menu);
	
	void delete(int id);	
	
	void update(Menu menu);	

}