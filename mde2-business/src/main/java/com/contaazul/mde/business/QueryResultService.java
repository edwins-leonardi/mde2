package com.contaazul.mde.business;

import javax.inject.Inject;

import org.w3c.dom.Document;

import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.business.xml.signer.XmlMarshallerService;
import com.contaazul.mde.converter.InvoiceQueryResultConverter;
import com.contaazul.mde.result.QueryResultParser;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt;

public class QueryResultService {
	@Inject
	private InvoiceQueryResultConverter converter;
	@Inject
	private QueryResultParser parser;
	@Inject
	private XmlMarshallerService xmlMarshallerService;

	public InvoiceQueryResult convert(Document document) {
		RetDistDFeInt sefazResult = parser.parse( document );
		InvoiceQueryResult result = converter.convert( sefazResult );
		result.setResultXml( xmlMarshallerService.asString( sefazResult ) );
		return result;
	}

}
