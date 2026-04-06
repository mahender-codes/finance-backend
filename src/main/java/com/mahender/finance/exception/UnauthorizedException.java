package com.mahender.finance.exception;

public class UnauthorizedException extends RuntimeException{
	public UnauthorizedException(String msg) {
		super(msg);
	}
}
