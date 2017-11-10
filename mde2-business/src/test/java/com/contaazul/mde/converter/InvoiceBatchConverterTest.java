package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.mde.api.request.Invoice;
import com.contaazul.mde.api.request.InvoiceBatch;
import com.contaazul.mde.api.request.InvoiceSummary;
import com.contaazul.mde.business.InvoiceSummaryService;
import com.contaazul.mde.business.InvoiceService;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;

public class InvoiceBatchConverterTest {
	private static final String PROCESSED_INVOICE = "procNFe_v3.10.xsd";
	private static final String INVOICE_SUMMARY = "resNFe_v1.00.xsd";

	@Mock
	private InvoiceService processedInvoiceService;
	@Mock
	private InvoiceSummaryService invoiceSummaryService;
	@InjectMocks
	private InvoiceBatchConverter converter;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		Invoice processedInvoice = new Invoice();
		InvoiceSummary invoiceSummary = new InvoiceSummary();
		DocZip processedInvoiceDocument = buildDocZip( PROCESSED_INVOICE );
		DocZip invoiceSummaryDocument = buildDocZip( INVOICE_SUMMARY );
		when( processedInvoiceService.convert( processedInvoiceDocument ) ).thenReturn( processedInvoice );
		when( invoiceSummaryService.convert( invoiceSummaryDocument ) ).thenReturn( invoiceSummary );

		InvoiceBatch invoiceBatch = converter.convert( buildBatch( processedInvoiceDocument, invoiceSummaryDocument ) );

		List<Invoice> processedInvoices = invoiceBatch.getProcessedInvoices();
		assertThat( processedInvoices, hasSize( 1 ) );
		assertThat( processedInvoices.get( 0 ), equalTo( processedInvoice ) );

		List<InvoiceSummary> invoiceSummaries = invoiceBatch.getInvoiceSummaries();
		assertThat( invoiceSummaries, hasSize( 1 ) );
		assertThat( invoiceSummaries.get( 0 ), equalTo( invoiceSummary ) );
	}

	private LoteDistDFeInt buildBatch(DocZip... documents) {
		LoteDistDFeInt batch = new LoteDistDFeInt();
		batch.getDocZip().addAll( Arrays.asList( documents ) );
		return batch;
	}

	private static DocZip buildDocZip(String schema) {
		DocZip docZip = new DocZip();
		docZip.setSchema( schema );
		return docZip;
	}

}
