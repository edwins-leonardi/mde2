package com.contaazul.mde.result;

import javax.inject.Inject;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.contaazul.mde.business.action.BasicResultParser;
import com.contaazul.mde.business.action.XmlParseException;
import com.contaazul.mde.business.xml.context.QueryResponseContext;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt;

public class QueryResultParser {
	@Inject
	private BasicResultParser parser;
	@Inject
	private QueryResponseContext context;

	public RetDistDFeInt parse(Document document) throws XmlParseException {
		return parser.parse( extractReturnNode( document ), RetDistDFeInt.class, context.getContext() );
	}

	public Node extractReturnNode(Document document) {
		return document.getElementsByTagName( "retDistDFeInt" ).item( 0 );
	}

}
