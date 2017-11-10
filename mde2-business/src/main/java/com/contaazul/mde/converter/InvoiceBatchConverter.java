package com.contaazul.mde.converter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.contaazul.mde.api.request.InvoiceBatch;
import com.contaazul.mde.business.InvoiceService;
import com.contaazul.mde.business.InvoiceSummaryService;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;

public class InvoiceBatchConverter {
	@Inject
	private InvoiceService processedInvoiceService;
	@Inject
	private InvoiceSummaryService invoiceSummaryService;

	private static final String PROCESSED_SCHEMA = "procNFe_v3.10.xsd";
	private static final String SUMMARY_SCHEMA = "resNFe_v1.00.xsd";

	public InvoiceBatch convert(LoteDistDFeInt batch) {
		List<DocZip> documents = batch.getDocZip();
		return InvoiceBatch
				.builder()
				.processedInvoices( extract( documents, PROCESSED_SCHEMA, e -> processedInvoiceService.convert( e ) ) )
				.invoiceSummaries( extract( documents, SUMMARY_SCHEMA, e -> invoiceSummaryService.convert( e ) ) )
				.build();
	}

	public <T> List<T> extract(List<DocZip> documents, String schema, Function<DocZip, T> converter) {
		return documents.stream()
				.filter( e -> schema.equals( e.getSchema() ) )
				.map( converter )
				.collect( Collectors.toList() );
	}

}
