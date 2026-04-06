package com.mahender.finance.exception;

public class UserAlreadyExistException extends RuntimeException{
	public UserAlreadyExistException(String msg) {
		super(msg);
	}
}
