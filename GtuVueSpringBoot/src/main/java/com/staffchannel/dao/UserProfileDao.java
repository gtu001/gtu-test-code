package com.staffchannel.dao;

import java.util.List;

import com.staffchannel.model.UserProfile;


public interface UserProfileDao {

	List<UserProfile> findAll();
	
	UserProfile findByType(String type);
	
	UserProfile findById(int id);
	
	void save(UserProfile userProfile);
}
