package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.invoiceissuer.api.UF;
import com.contaazul.mde.api.request.InvoiceQuery;
import com.contaazul.mde.api.request.InvoiceQueryRequest;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.api.request.RecipientManifestationBatch;
import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.business.action.Action;
import com.contaazul.mde.business.action.ActionException;
import com.contaazul.mde.business.action.ActionExecutorService;
import com.contaazul.mde.business.action.ActionFactory;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.mde.business.xml.signer.XmlMarshallerService;
import com.contaazul.mde.converter.InvoiceQueryConverter;
import com.contaazul.mde.converter.RecipientManifestationBatchConverter;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEnvEvento;
import com.contaazul.nfe.key.KeyProvider;

public class InteractorTest {
	@Mock
	private ActionFactory actionFactory;
	@Mock
	private ActionExecutorService executor;
	@Mock
	private InvoiceQueryConverter invoiceQueryConverter;
	@Mock
	private RecipientManifestationBatchConverter recipientManifestationBatchConverter;
	@Mock
	private XmlMarshallerService marshallerService;
	@Mock
	private XmlObject xml;
	@Mock
	private InputStream signedXml;
	@Mock
	private InvoiceQueryResult invoiceQueryResultFromExecution;
	@Mock
	private RecipientManifestationBatchResult resultFromExecution;
	@Mock
	private Action action;
	@Mock
	private KeyProvider keyProvider;
	@InjectMocks
	private Interactor interactor;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void query() {
		DistDFeInt distDFeInt = new DistDFeInt();
		InvoiceQuery query = new InvoiceQuery();
		query.setUf( UF.SC );
		InvoiceQueryRequest request = buildRequest( query );
		when( invoiceQueryConverter.convert( query ) ).thenReturn( distDFeInt );
		when( marshallerService.marshalQuery( distDFeInt ) ).thenReturn( xml );
		when( actionFactory.query() ).thenReturn( action );
		when( executor.execute( keyProvider, action, UF.SC, xml ) ).thenReturn( invoiceQueryResultFromExecution );
		InvoiceQueryResult result = interactor.query( request, keyProvider );
		assertThat( result, is( equalTo( invoiceQueryResultFromExecution ) ) );
	}

	@Test
	public void event() throws Exception {
		TEnvEvento tEnvEvento = new TEnvEvento();
		RecipientManifestationBatch batch = new RecipientManifestationBatch();
		batch.setUf( UF.SC );
		when( recipientManifestationBatchConverter.convert( batch ) ).thenReturn( tEnvEvento );
		when( marshallerService.signEvent( keyProvider, tEnvEvento ) ).thenReturn( signedXml );
		when( marshallerService.parse( signedXml ) ).thenReturn( xml );
		when( actionFactory.event() ).thenReturn( action );
		when( executor.execute( same( keyProvider ), any( Action.class ), same( UF.SC ), same( xml ) ) )
				.thenReturn( resultFromExecution );
		ParentResult<RecipientManifestationBatchResult> result = interactor.event( batch, keyProvider );
		assertThat( result.getResult(), equalTo( resultFromExecution ) );
		assertThat( result.getXml(), equalTo( signedXml ) );
		verify( signedXml ).reset();
	}

	private InvoiceQueryRequest buildRequest(InvoiceQuery query) {
		return InvoiceQueryRequest.builder()
				.invoiceQuery( query )
				.build();
	}

	@Test(expected = ActionException.class)
	public void errorWhenResettingStream() throws Exception {
		TEnvEvento tEnvEvento = new TEnvEvento();
		RecipientManifestationBatch batch = new RecipientManifestationBatch();
		batch.setUf( UF.SC );
		when( recipientManifestationBatchConverter.convert( batch ) ).thenReturn( tEnvEvento );
		when( marshallerService.signEvent( keyProvider, tEnvEvento ) ).thenReturn( signedXml );
		when( marshallerService.parse( signedXml ) ).thenReturn( xml );
		when( actionFactory.event() ).thenReturn( action );
		when( executor.execute( same( keyProvider ), any( Action.class ), same( UF.SC ), same( xml ) ) )
				.thenReturn( resultFromExecution );
		doThrow( IOException.class ).when( signedXml ).reset();
		interactor.event( batch, keyProvider );
	}
}
