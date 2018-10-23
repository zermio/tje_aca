package com.tj.exception;

public class MemberNotFoundException extends Exception {

	private String message;
	
	public MemberNotFoundException() {
		this.message = "아이디를 찾을 수 없습니다.";
	}
	
	public MemberNotFoundException(String message) {
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
