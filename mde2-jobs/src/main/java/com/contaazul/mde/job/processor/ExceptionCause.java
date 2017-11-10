package com.contaazul.mde.job.processor;

import static com.google.common.base.Strings.isNullOrEmpty;
import lombok.AllArgsConstructor;

import com.google.common.base.Throwables;

@AllArgsConstructor
public class ExceptionCause {
	private static final String UNEXPECTED_ERROR = "Erro desconhecido: %s";
	private Exception e;

	public String getText() {
		Throwable rootCause = Throwables.getRootCause( e );
		String rootCauseMessage = rootCause.getMessage();
		if (isNullOrEmpty( rootCauseMessage ))
			return String.format( UNEXPECTED_ERROR, rootCause.getClass().getSimpleName() );
		return rootCauseMessage;
	}
}
