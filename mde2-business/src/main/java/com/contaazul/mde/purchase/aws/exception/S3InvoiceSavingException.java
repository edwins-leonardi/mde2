package com.contaazul.mde.purchase.aws.exception;

import com.contaazul.mde.business.action.ActionException;

public class S3InvoiceSavingException extends ActionException {
	private static final long serialVersionUID = -7674852219291575626L;
	private static final String MESSAGE = "Erro ao tentar salvar arquivo: %s";

	public S3InvoiceSavingException(Throwable t, String filename) {
		super( String.format( MESSAGE, filename ), t );
	}

}
