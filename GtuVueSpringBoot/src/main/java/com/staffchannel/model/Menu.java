package com.staffchannel.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="MENU")
public class Menu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5653294136659464935L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="MENUNAME", unique=true, nullable=false)
	private String menuName;
	
	@Column(name="MENUURL", unique=true, nullable=false)
	private String menuUrl;

	@Column(name="MENUDESC", nullable=false)
	private String menuDesc;
	
	@Column(name="FATHERID", nullable=true)
	private Integer fatherId;

	@ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.MERGE, mappedBy="menuList")
	private List<UserProfile> userProfile;
	
	@Transient
	private List<Menu> subMenu;
	
	

	public List<UserProfile> getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(List<UserProfile> userProfile) {
		this.userProfile = userProfile;
	}

	public List<Menu> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<Menu> subMenu) {
		this.subMenu = subMenu;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public Integer getFatherId() {
		return fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", menuName=" + menuName + ", menuUrl=" + menuUrl + ", menuDesc=" + menuDesc
				+ ", fatherId=" + fatherId + ", subMenu=" + subMenu + "]";
	}


}
