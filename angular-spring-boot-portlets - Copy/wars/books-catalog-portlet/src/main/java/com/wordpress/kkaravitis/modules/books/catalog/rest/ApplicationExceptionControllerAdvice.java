package com.wordpress.kkaravitis.modules.books.catalog.rest;

import com.wordpress.kkaravitis.modules.books.catalog.Constants;
import com.wordpress.kkaravitis.modules.books.catalog.exception.ApplicationException;
import com.wordpress.kkaravitis.modules.books.catalog.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Konstantinos Karavitis
 **/
@ControllerAdvice
public class ApplicationExceptionControllerAdvice {
	@Autowired
	MessageSource messageSource;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorResponse> applicationExceptionHandler(ApplicationException exception) {
		if (exception.getCause() != null) {
			logger.error(exception.getCause().getMessage());
		}
		logger.error(exception.getMessage(), exception);
		return createErrorResponseEntity(exception.getCode());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
		logger.error(exception.getMessage());
		logger.error(exception.getMessage(), exception);
		return createErrorResponseEntity(null);
	}
	
	private ResponseEntity<ErrorResponse> createErrorResponseEntity(Integer code) {
		ErrorResponse error = new ErrorResponse();
		String errorMessageKey = code != null ? Constants.APPLICATION_ERROR + code  : Constants.SYSTEM_ERROR;
		error.setMessage(messageSource.getMessage(errorMessageKey, null, LocaleContextHolder.getLocale()));
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
