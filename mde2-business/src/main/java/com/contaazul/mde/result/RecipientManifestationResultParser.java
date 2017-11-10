package com.contaazul.mde.result;

import javax.inject.Inject;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.contaazul.mde.business.action.BasicResultParser;
import com.contaazul.mde.business.action.XmlParseException;
import com.contaazul.mde.business.xml.context.EventResponseContext;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TRetEnvEvento;

public class RecipientManifestationResultParser {
	@Inject
	private BasicResultParser parser;
	@Inject
	private EventResponseContext context;

	public TRetEnvEvento parse(Document document) throws XmlParseException {
		return parser.parse( extractReturnNode( document ), TRetEnvEvento.class, context.getContext() );
	}

	public Node extractReturnNode(Document document) {
		return document.getElementsByTagName( "retEnvEvento" ).item( 0 );
	}

}
