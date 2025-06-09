package com.springboot.assetsphere;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.assetsphere.exception.ResourceNotFoundException;

@ControllerAdvice
public class GobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

	@ExceptionHandler(exception = RuntimeException.class)
	public ResponseEntity<?> handleRuntimE(RuntimeException e){
		Map<String,String> map = new HashMap<>();
		logger.info(e.getMessage());
		map.put("msg", e.getMessage());
		logger.error(e.getMessage(), e.getClass());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		
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
}
