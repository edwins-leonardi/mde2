package com.contaazul.mde.business.action;

public class ActionException extends RuntimeException {
	private static final long serialVersionUID = 2142293844655462051L;

	public ActionException(String message, Throwable t) {
		super( message, t );
	}

	public ActionException(Throwable t) {
		super( t );
	}

	public ActionException(String message) {
		super( message );
	}

	public ActionException() {
		super();
	}

}
