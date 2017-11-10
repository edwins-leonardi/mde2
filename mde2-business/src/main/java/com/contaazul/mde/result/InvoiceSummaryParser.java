package com.contaazul.mde.result;

import javax.inject.Inject;

import com.contaazul.mde.business.action.BasicResultParser;
import com.contaazul.mde.business.xml.context.InvoiceSummaryContext;
import com.contaazul.mde.res_nfe_v1_00.ResNFe;

public class InvoiceSummaryParser {
	@Inject
	private BasicResultParser parser;
	@Inject
	private InvoiceSummaryContext context;

	public ResNFe parse(String xml) {
		return parser.parse( xml, ResNFe.class, context.getContext() );
	}

}
