/**
 * 
 */
package com.staffchannel.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Walalala
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String password = "1234";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode(password));
	}

}
