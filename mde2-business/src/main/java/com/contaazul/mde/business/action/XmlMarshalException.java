package com.contaazul.mde.business.action;

public class XmlMarshalException extends RuntimeException {
	private static final long serialVersionUID = 1574090487653732508L;
	private static final String MESSAGE = "Erro ao gerar XMl do objeto %s";

	public XmlMarshalException(Throwable t, Object objectToMarshal) {
		super( String.format( MESSAGE, objectToMarshal ), t );
	}

}
