/**
 * 
 */
package com.staffchannel.UserDetails;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.staffchannel.model.Menu;

/**
 * @author Walalala
 *
 */
public class CurrentUser extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8538523509764343512L;

	public CurrentUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			List<Menu> menuList, String ipAddress) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.menuList = menuList;
		this.ipAddress = ipAddress;
	}

	private List<Menu> menuList;
	private String ipAddress;

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



}
