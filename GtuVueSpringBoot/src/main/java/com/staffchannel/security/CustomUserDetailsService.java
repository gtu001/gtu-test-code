package com.staffchannel.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staffchannel.UserDetails.CurrentUser;
import com.staffchannel.model.Menu;
import com.staffchannel.model.User;
import com.staffchannel.model.UserProfile;
import com.staffchannel.service.UserService;
import com.staffchannel.util.IPUtil;


@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String ssoId) throws UsernameNotFoundException {
		User user = userService.findBySSO(ssoId);
		logger.info("User : {}", user);

		if(user==null){
			logger.info("User not found");
			throw new UsernameNotFoundException("Username not found");
		}
		
		HashMap<Integer, Menu> menuMapFather = new LinkedHashMap<Integer, Menu>();
		HashMap<Integer, List<Menu>> menuMapSon = new HashMap<Integer, List<Menu>>();

		for (UserProfile userProfile : user.getUserProfiles()) {
			for (Menu menu : userProfile.getMenuList()) {
				if (menu.getFatherId() != null) {
					int fatherId = menu.getFatherId();
					if (menuMapSon.get(fatherId) != null) {
						menuMapSon.get(fatherId).add(menu);
					} else {
						List<Menu> menuListSon = new ArrayList<Menu>();
						menuListSon.add(menu);
						menuMapSon.put(fatherId, menuListSon);
					}
				} else {
					menuMapFather.put(menu.getId(), menu);
				}
			}
		}
		for (int key : menuMapSon.keySet()) {
		    List<Menu> subMenu = menuMapSon.get(key);
			menuMapFather.get(key).setSubMenu(subMenu);
		}

		List<Menu> menuList = new ArrayList<Menu>();
		for (int key : menuMapFather.keySet()) {
			menuList.add(menuMapFather.get(key));
		}

		String ipAddress = IPUtil.getIpAddress(request);
		
		return new CurrentUser(user.getSsoId(), user.getPassword(), 
			 true, true, true, true, getGrantedAuthorities(user), menuList, ipAddress);
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(UserProfile userProfile : user.getUserProfiles()){
			logger.info("UserProfile : {}", userProfile);
			authorities.add(new SimpleGrantedAuthority("ROLE_"+userProfile.getType()));
		}
		logger.info("authorities : {}", authorities);
		return authorities;
	}
	
}
