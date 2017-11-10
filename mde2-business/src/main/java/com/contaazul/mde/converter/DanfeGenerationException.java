package com.contaazul.mde.converter;

import com.contaazul.mde.business.action.ActionException;

public class DanfeGenerationException extends ActionException {
	private static final long serialVersionUID = 4443693911997370563L;
	private static final String MESSAGE = "Erro ao gerar DANFE: %s";

	public DanfeGenerationException(Throwable t, String invoiceKey) {
		super( String.format( MESSAGE, invoiceKey ), t );
	}

}
