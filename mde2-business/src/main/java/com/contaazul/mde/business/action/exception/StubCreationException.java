package com.contaazul.mde.business.action.exception;

import com.contaazul.mde.business.action.ActionException;

public class StubCreationException extends ActionException {
	private static final long serialVersionUID = -2383923662770192255L;
	private static final String MESSAGE = "Error creating stub for class %s with url %s";

	public StubCreationException(Throwable t, Class<?> stub, String url) {
		super( String.format( MESSAGE, stub, url ), t );
	}

}
