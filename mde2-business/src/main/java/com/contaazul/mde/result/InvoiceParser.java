package com.contaazul.mde.result;

import javax.inject.Inject;

import com.contaazul.mde.business.action.BasicResultParser;
import com.contaazul.mde.business.xml.context.InvoiceContext;
import com.contaazul.mde.proc_nfe_v3_10.TNfeProc;

public class InvoiceParser {
	@Inject
	private BasicResultParser parser;
	@Inject
	private InvoiceContext context;

	public TNfeProc parse(String xml) {
		return parser.parse( xml, TNfeProc.class, context.getContext() );
	}

}
