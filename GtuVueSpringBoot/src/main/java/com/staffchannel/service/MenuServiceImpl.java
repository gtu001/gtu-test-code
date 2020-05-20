package com.staffchannel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staffchannel.dao.MenuDao;
import com.staffchannel.dao.UserDao;
import com.staffchannel.model.Menu;
import com.staffchannel.model.User;


@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuDao dao;

	public Menu findById(int id) {
		return dao.findById(id);
	}

	public List<Menu> findBySSO(String sso) {
		List<Menu> menu = dao.findBySSO(sso);
		return menu;
	}

	public List<Menu> findAllMenus() {
		List<Menu> MenuExsiting = dao.findAllMenus();
		HashMap<Integer, Menu> menuMapFather = new LinkedHashMap<Integer, Menu>();
		HashMap<Integer, List<Menu>> menuMapSon = new HashMap<Integer, List<Menu>>();
		for(Menu menu : MenuExsiting ) {
			if(menu.getFatherId()!=null) {
				int fatherId = menu.getFatherId();
				if (menuMapSon.get(fatherId) != null) {
					menuMapSon.get(fatherId).add(menu);
				} 
				else {
					List<Menu> menuListSon = new ArrayList<Menu>();
					menuListSon.add(menu);
					menuMapSon.put(fatherId, menuListSon);
				}
			}else {
				menuMapFather.put(menu.getId(), menu);
			}
		}
		for (int key : menuMapSon.keySet()) {
			if (menuMapFather.get(key) == null)
				break;
			menuMapFather.get(key).setSubMenu(menuMapSon.get(key));
		}
		List<Menu> menuList = new ArrayList<Menu>();
		for (int key : menuMapFather.keySet()) {
			menuList.add(menuMapFather.get(key));
		}
		
		
		return menuList;
	}
	
	public void save(Menu menu){
		dao.save(menu);
	}
		
	public void delete(int id) {
		dao.delete(id);
	}
	
	public void update(Menu menu){
		Menu entity = dao.findById(menu.getId());
		if (entity != null){
			entity.setFatherId(menu.getFatherId());
			entity.setMenuDesc(menu.getMenuDesc());
			entity.setMenuName(menu.getMenuName());
			entity.setMenuUrl(menu.getMenuUrl());
		}
	}	
	
}
