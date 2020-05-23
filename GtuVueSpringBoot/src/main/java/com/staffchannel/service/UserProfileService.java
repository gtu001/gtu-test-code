package com.staffchannel.service;

import java.util.List;

import com.staffchannel.model.UserProfile;


public interface UserProfileService {

	UserProfile findById(int id);

	UserProfile findByType(String type);
	
	List<UserProfile> findAll();
	
	void save(UserProfile userProfile);
	
	void update(UserProfile userProfile);

    void delete(UserProfile userProfile);
	
}
