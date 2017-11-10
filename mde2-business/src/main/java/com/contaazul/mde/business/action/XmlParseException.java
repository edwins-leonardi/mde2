package com.contaazul.mde.business.action;

public class XmlParseException extends RuntimeException {
	private static final long serialVersionUID = -1703043889834439857L;

	public XmlParseException(Exception e) {
		super( e );
	}
}
