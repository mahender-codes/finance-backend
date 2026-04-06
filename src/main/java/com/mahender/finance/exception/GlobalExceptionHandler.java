package com.mahender.finance.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mahender.finance.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseStructure<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<String, String>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

		return ResponseEntity.ok(new ResponseStructure<Map<String, String>>(HttpStatus.BAD_REQUEST.value(),
				"Invalid data request. Please check the input fields.", errorMap));
	}

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ResponseStructure<String>> userAlreadyExistException(UserAlreadyExistException ex) {

		return ResponseEntity.ok(new ResponseStructure<String>(HttpStatus.CONFLICT.value(), ex.getMessage(), ""));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> userNotFoundException(UserNotFoundException ex) {

		return ResponseEntity.ok(new ResponseStructure<String>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), ""));
	}

	@ExceptionHandler(UserInactiveException.class)
	public ResponseEntity<ResponseStructure<String>> userInactiveException(UserInactiveException ex) {

		return ResponseEntity.ok(new ResponseStructure<String>(HttpStatus.FORBIDDEN.value(), ex.getMessage(), ""));
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> recordNotFoundException(RecordNotFoundException ex) {

		return ResponseEntity.ok(new ResponseStructure<String>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), ""));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResponseStructure<String>> unauthorizedException(UnauthorizedException ex) {

		return ResponseEntity.ok(new ResponseStructure<String>(HttpStatus.FORBIDDEN.value(), ex.getMessage(), ""));
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<ResponseStructure<String>> invalidOperationException(InvalidOperationException ex) {

		return ResponseEntity.ok(new ResponseStructure<String>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null));
	}
}
