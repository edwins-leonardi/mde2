package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.mde.api.request.InvoiceBatch;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt;

public class InvoiceQueryResultConverterTest {
	private static final String ULT_NSU = "000000000046886";
	private static final String MAX_NSU = "000000000051692";
	private static final String SUCCESS_CODE = "138";
	private static final String REASON = "Lote processado com sucesso";
	private static final LoteDistDFeInt SEFAZ_BATCH = new LoteDistDFeInt();
	private static final InvoiceBatch BATCH = new InvoiceBatch();

	@Mock
	private InvoiceBatchConverter invoiceBatchConverter;

	@InjectMocks
	private InvoiceQueryResultConverter converter;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convertResultWithoutBatch() {
		InvoiceQueryResult result = converter.convert( buildResultWithoutBatch() );
		assertThat( result.getLastUniqueSequentialNumber(), equalTo( ULT_NSU ) );
		assertThat( result.getMaxAvailableSequentialNumber(), equalTo( MAX_NSU ) );
		assertThat( result.getInvoices().getProcessedInvoices(), is( empty() ) );
		assertThat( result.getInvoices().getInvoiceSummaries(), is( empty() ) );
		assertThat( result.getReason(), equalTo( REASON ) );
		verifyZeroInteractions( invoiceBatchConverter );
	}

	private RetDistDFeInt buildResultWithoutBatch() {
		RetDistDFeInt retDistDFeInt = new RetDistDFeInt();
		retDistDFeInt.setCStat( SUCCESS_CODE );
		retDistDFeInt.setXMotivo( REASON );
		retDistDFeInt.setUltNSU( ULT_NSU );
		retDistDFeInt.setMaxNSU( MAX_NSU );
		return retDistDFeInt;
	}

	@Test
	public void convert() {
		when( invoiceBatchConverter.convert( SEFAZ_BATCH ) ).thenReturn( BATCH );
		InvoiceQueryResult result = converter.convert( buildResult() );
		assertThat( result.getLastUniqueSequentialNumber(), equalTo( ULT_NSU ) );
		assertThat( result.getMaxAvailableSequentialNumber(), equalTo( MAX_NSU ) );
		assertThat( result.getInvoices(), equalTo( BATCH ) );
		assertThat( result.getReason(), equalTo( REASON ) );
	}

	private RetDistDFeInt buildResult() {
		RetDistDFeInt retDistDFeInt = buildResultWithoutBatch();
		retDistDFeInt.setLoteDistDFeInt( SEFAZ_BATCH );
		return retDistDFeInt;
	}

}
