package com.contaazul.mde.purchase.aws;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.mde.api.request.Invoice;
import com.contaazul.mde.api.request.InvoiceBatch;
import com.contaazul.mde.api.request.InvoiceQueryResult;

public class XMLPersistServiceTest {
	private static final String INVOICE_KEY_1 = "NFe35150410807909000124550010000034831413600373";
	private static final String INVOICE_KEY_2 = "NFe35150410807909000124550010000034831413600374";
	private static final String XML_INVOICE_1 = "invoice 1";
	private static final String XML_INVOICE_2 = "invoice 2";
	private static final Long COMPANY_ID = 7L;
	@Mock
	private PurchaseImportXMLBridge bridge;
	@InjectMocks
	private XMLPersistService service;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void save() {
		service.save( buildResult() );
		verify( bridge ).save( COMPANY_ID, INVOICE_KEY_1, XML_INVOICE_1 );
		verify( bridge ).save( COMPANY_ID, INVOICE_KEY_2, XML_INVOICE_2 );
	}

	private InvoiceQueryResult buildResult() {
		InvoiceQueryResult result = InvoiceQueryResult.builder()
				.invoices( InvoiceBatch.builder()
						.processedInvoices( asList(
								buildInvoice( INVOICE_KEY_1, XML_INVOICE_1 ),
								buildInvoice( INVOICE_KEY_2, XML_INVOICE_2 )
								) )
						.build() )
				.build();
		result.setCompanyId( COMPANY_ID );
		return result;
	}

	private Invoice buildInvoice(String invoiceKey, String xml) {
		return Invoice.builder()
				.invoiceKey( invoiceKey )
				.xml( xml )
				.build();
	}

}
