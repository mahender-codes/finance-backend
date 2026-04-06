package com.mahender.finance.exception;

public class UserInactiveException extends RuntimeException{
	public UserInactiveException(String msg) {
		super(msg);
	}
}
