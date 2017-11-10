package com.contaazul.mde.business;

import javax.inject.Inject;

import com.contaazul.mde.api.request.InvoiceSummary;
import com.contaazul.mde.converter.DocZipContentDecoder;
import com.contaazul.mde.converter.InvoiceSummaryConverter;
import com.contaazul.mde.result.InvoiceSummaryParser;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;

public class InvoiceSummaryService {
	@Inject
	private DocZipContentDecoder decoder;
	@Inject
	private InvoiceSummaryConverter converter;
	@Inject
	private InvoiceSummaryParser parser;

	public InvoiceSummary convert(DocZip docZip) {
		return converter.convert( parser.parse( decoder.decode( docZip ) ) );
	}
}
