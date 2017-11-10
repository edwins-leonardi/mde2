package com.contaazul.mde.business;

import javax.inject.Inject;

import com.contaazul.mde.api.request.Invoice;
import com.contaazul.mde.converter.DocZipContentDecoder;
import com.contaazul.mde.converter.InvoiceConverter;
import com.contaazul.mde.result.InvoiceParser;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;

public class InvoiceService {
	@Inject
	private DocZipContentDecoder decoder;
	@Inject
	private InvoiceConverter converter;
	@Inject
	private InvoiceParser parser;

	public Invoice convert(DocZip docZip) {
		String xml = decoder.decode( docZip );
		Invoice invoice = converter.convert( parser.parse( xml ) );
		invoice.setXml( xml );
		return invoice;
	}

}
