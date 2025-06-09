package com.springboot.codingchallenge;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.codingchallenge.exception.ResourceNotFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

	@ExceptionHandler(exception = RuntimeException.class)
	public ResponseEntity<?> handleRuntimE(RuntimeException e){
		logger.info(e.getMessage());
		Map<String,String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		logger.error(e.getMessage(), e.getClass());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		
	}
	
	@ExceptionHandler(exception = ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
		Map<String,String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		logger.error(e.getMessage(), e.getClass());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(map);
	}
	
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		Map<String, String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		logger.error(e.getMessage(), e.getClass());
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(map);
	}
	

	
}
