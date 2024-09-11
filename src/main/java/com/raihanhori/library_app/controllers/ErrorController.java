package com.raihanhori.library_app.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.raihanhori.library_app.helper.ErrorResponseHelper;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorController {
	
	@ExceptionHandler(ConstraintViolationException.class)
	ErrorResponseHelper handleConstraintViolationException(ConstraintViolationException e) {
		return ErrorResponseHelper.builder().status(400).message(e.getMessage()).build();
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	ErrorResponseHelper handleResponseStatusException(ResponseStatusException e) {
		return ErrorResponseHelper.builder().status(e.getStatusCode().value()).message(e.getReason()).build();
	}
	
}
