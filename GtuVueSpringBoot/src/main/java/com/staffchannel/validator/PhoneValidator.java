/**
 * 
 */
package com.staffchannel.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Walalala
 *
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

	@Override
	public void initialize(Phone phoneNumber) {
	}

	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext ctx) {
		return Pattern.matches("((?=(09))[0-9]{10})$", phoneNumber);
	}

}

