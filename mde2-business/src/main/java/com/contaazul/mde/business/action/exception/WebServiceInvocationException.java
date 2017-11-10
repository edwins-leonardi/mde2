package com.contaazul.mde.business.action.exception;

import java.lang.reflect.Method;

import com.contaazul.mde.business.action.ActionException;

public class WebServiceInvocationException extends ActionException {
	private static final long serialVersionUID = -3343490438834478627L;
	private static final String MESSAGE = "Error invoking method %s on %s";

	public WebServiceInvocationException(Throwable t, Class<?> stub, Method method) {
		super( String.format( MESSAGE, method, stub ), t );
	}
}
