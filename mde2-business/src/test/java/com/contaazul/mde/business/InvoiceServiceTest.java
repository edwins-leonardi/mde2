package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.mde.api.request.Invoice;
import com.contaazul.mde.converter.DocZipContentDecoder;
import com.contaazul.mde.converter.InvoiceConverter;
import com.contaazul.mde.proc_nfe_v3_10.TNfeProc;
import com.contaazul.mde.result.InvoiceParser;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;

public class InvoiceServiceTest {
	private static final DocZip DOC_ZIP = new DocZip();
	private static final String XML = "<nfeProc />";
	private static final TNfeProc PROCESSED_INVOICE_SEFAZ = new TNfeProc();
	private static final Invoice PROCESSED_INVOICE = new Invoice();

	@Mock
	private DocZipContentDecoder decoder;
	@Mock
	private InvoiceConverter converter;
	@Mock
	private InvoiceParser parser;
	@InjectMocks
	private InvoiceService service;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		when( decoder.decode( DOC_ZIP ) ).thenReturn( XML );
		when( parser.parse( XML ) ).thenReturn( PROCESSED_INVOICE_SEFAZ );
		when( converter.convert( PROCESSED_INVOICE_SEFAZ ) ).thenReturn( PROCESSED_INVOICE );
		Invoice invoice = service.convert( DOC_ZIP );
		assertThat( invoice, equalTo( PROCESSED_INVOICE ) );
		assertThat( invoice.getXml(), equalTo( XML ) );
	}
}
