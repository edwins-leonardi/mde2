package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.mde.api.request.InvoiceSummary;
import com.contaazul.mde.converter.DocZipContentDecoder;
import com.contaazul.mde.converter.InvoiceSummaryConverter;
import com.contaazul.mde.res_nfe_v1_00.ResNFe;
import com.contaazul.mde.result.InvoiceSummaryParser;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;

public class InvoiceSummaryServiceTest {

	private static final DocZip DOC_ZIP = new DocZip();
	private static final String XML = "<resNFe />";
	private static final ResNFe INVOICE_SUMMARY_SEFAZ = new ResNFe();
	private static final InvoiceSummary INVOICE_SUMMARY = new InvoiceSummary();

	@Mock
	private DocZipContentDecoder decoder;
	@Mock
	private InvoiceSummaryConverter converter;
	@Mock
	private InvoiceSummaryParser parser;
	@InjectMocks
	private InvoiceSummaryService service;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		when( decoder.decode( DOC_ZIP ) ).thenReturn( XML );
		when( parser.parse( XML ) ).thenReturn( INVOICE_SUMMARY_SEFAZ );
		when( converter.convert( INVOICE_SUMMARY_SEFAZ ) ).thenReturn( INVOICE_SUMMARY );
		InvoiceSummary invoice = service.convert( DOC_ZIP );
		assertThat( invoice, equalTo( INVOICE_SUMMARY ) );
	}

}
