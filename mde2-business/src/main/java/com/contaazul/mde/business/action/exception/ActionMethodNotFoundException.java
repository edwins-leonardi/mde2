package com.contaazul.mde.business.action.exception;

import com.contaazul.mde.business.action.ActionException;

public class ActionMethodNotFoundException extends ActionException {
	private static final long serialVersionUID = 5006707356878309433L;
	private static final String MESSAGE = "Action method %s not found in class %s";

	public ActionMethodNotFoundException(Class<?> stub, String method) {
		super( String.format( MESSAGE, method, stub ) );
	}

}
