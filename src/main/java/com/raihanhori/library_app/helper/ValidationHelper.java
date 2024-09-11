package com.raihanhori.library_app.helper;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Component
public class ValidationHelper {
	
	@Autowired
	private Validator validator;
	
	public void validate(Object object) {
		Set<ConstraintViolation<Object>> violation = validator.validate(object);
		if (!violation.isEmpty()) {
			throw new ConstraintViolationException(violation);
		}
	}
	
}
