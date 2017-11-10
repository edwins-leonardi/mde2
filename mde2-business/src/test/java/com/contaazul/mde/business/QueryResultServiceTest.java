package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.w3c.dom.Document;

import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.business.xml.signer.XmlMarshallerService;
import com.contaazul.mde.converter.InvoiceQueryResultConverter;
import com.contaazul.mde.result.QueryResultParser;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt;

public class QueryResultServiceTest {
	private static final String QUERY_XML = "<query />";
	@Mock
	private InvoiceQueryResultConverter converter;
	@Mock
	private QueryResultParser parser;
	@Mock
	private XmlMarshallerService xmlMarshallerService;
	@InjectMocks
	private QueryResultService service;
	@Mock
	private Document document;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		RetDistDFeInt result = new RetDistDFeInt();
		InvoiceQueryResult convertedResult = new InvoiceQueryResult();
		when( parser.parse( document ) ).thenReturn( result );
		when( converter.convert( result ) ).thenReturn( convertedResult );
		when( xmlMarshallerService.asString( result ) ).thenReturn( QUERY_XML );
		assertThat( service.convert( document ), equalTo( convertedResult ) );
		assertThat( convertedResult.getResultXml(), equalTo( QUERY_XML ) );
	}

}
