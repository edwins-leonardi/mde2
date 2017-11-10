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

import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.business.xml.signer.XmlMarshallerService;
import com.contaazul.mde.converter.RecipientManifestationBatchResultConverter;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TRetEnvEvento;
import com.contaazul.mde.result.RecipientManifestationResultParser;

public class RecipientManifestationResultServiceTest {
	private static final String RESPONSE_XML = "<response />";
	@Mock
	private RecipientManifestationBatchResultConverter converter;
	@Mock
	private RecipientManifestationResultParser parser;
	@Mock
	private XmlMarshallerService xmlMarshallerService;
	@InjectMocks
	private RecipientManifestationResultService service;
	@Mock
	private Document document;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		TRetEnvEvento result = new TRetEnvEvento();
		RecipientManifestationBatchResult convertedResult = new RecipientManifestationBatchResult();
		when( parser.parse( document ) ).thenReturn( result );
		when( converter.convert( result ) ).thenReturn( convertedResult );
		when( xmlMarshallerService.asString( result ) ).thenReturn( RESPONSE_XML );
		RecipientManifestationBatchResult generatedResult = service.convert( document );
		assertThat( generatedResult, equalTo( convertedResult ) );
		assertThat( generatedResult.getResultXml(), equalTo( RESPONSE_XML ) );
	}

}
