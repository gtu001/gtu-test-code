/**
 * 
 */
package com.staffchannel.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Walalala
 *
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    
    private static Logger logger = LoggerFactory.getLogger(PhoneValidator.class);

	@Override
	public void initialize(Phone phoneNumber) {
	}

	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext ctx) {
	    logger.info("#isValid " + phoneNumber);
	    
//	    if(phoneNumber == null) {
//	        return true;
//	    }
	    
		return Pattern.matches("((?=(09))[0-9]{10})$", phoneNumber);
	}

}

