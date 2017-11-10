package com.contaazul.mde.converter.exception;

import com.contaazul.mde.business.action.ActionException;

public class DocZipDecodingException extends ActionException {
	private static final long serialVersionUID = -3997789000153741913L;
	private static final String MESSAGE = "Erro ao decodificar conteúdo do docZip: %s";

	public DocZipDecodingException(Throwable t, String content) {
		super( String.format( MESSAGE, content ), t );
	}

}
