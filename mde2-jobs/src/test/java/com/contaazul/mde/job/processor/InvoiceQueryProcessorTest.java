package com.contaazul.mde.job.processor;

import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_FAIL;
import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_START;
import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_SUCCESS;
import static com.contaazul.invoiceissuer.entities.ActionLogType.MDE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.aws.sqs.SQSService;
import com.contaazul.invoiceissuer.api.Environment;
import com.contaazul.invoiceissuer.api.UF;
import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.invoiceissuer.entities.ActionLog;
import com.contaazul.invoiceissuer.entities.App;
import com.contaazul.invoiceissuer.service.LogService;
import com.contaazul.mde.api.config.MDePublicQueuesNames;
import com.contaazul.mde.api.request.InvoiceQuery;
import com.contaazul.mde.api.request.InvoiceQueryRequest;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.aws.MDeS3Service;
import com.contaazul.mde.business.InvoiceQueryService;
import com.contaazul.mde.business.action.ActionException;
import com.contaazul.mde.business.keyprovider.ProcessorService;
import com.contaazul.nfe.key.KeyProvider;

public class InvoiceQueryProcessorTest {
	private static final String EXCEPTION_MESSAGE = "Teste";
	private static final String RESULT_XML = "<result />";
	private static final Long COMPANY_ID = 1234L;
	private static final String STATE_CODE = "SC";
	private static final String NSU = "00000000001324";
	private static final String DOCUMENT = "65978334000180";
	private static final String REASON = "Documento localizado";
	private static final String QUEUE = "mde.queue.retrieve.response.name";
	private static final String KEY_FILENAME = "1234.pfx";
	private static final char[] KEY_PASSWORD = "passw0rd".toCharArray();
	private static final char[] KEY_TOKEN = "D3SG3ASG8G48GS".toCharArray();
	@Mock
	private LogService logService;
	@Mock
	private InvoiceQueryService service;
	@Mock
	private MDePublicQueuesNames queues;
	@Mock
	private SQSService sqsService;
	@Mock
	private ProcessorService processorService;
	@Mock
	private MDeS3Service mdeS3Service;
	@InjectMocks
	private InvoiceQueryProcessor processor;
	@Captor
	private ArgumentCaptor<ActionLog> logCaptor;
	@Captor
	private ArgumentCaptor<InvoiceQueryResult> messageCaptor;
	@Mock
	private KeyProvider keyProvider;
	@Mock
	private S3Context s3Context;

	@Before
	public void setup() {
		initMocks( this );
		mockCertificate();
	}

	private void mockCertificate() {
		when( mdeS3Service.createContext( COMPANY_ID ) ).thenReturn( s3Context );
		when( processorService.buildKeyProvider( KEY_FILENAME, KEY_PASSWORD, KEY_TOKEN, Environment.HOMOLOGATION,s3Context ) )
				.thenReturn( keyProvider );
	}

	@Test
	public void process() {
		when( queues.queueRetrieveResponseName() ).thenReturn( QUEUE );
		InvoiceQueryRequest request = buildRequest();
		InvoiceQueryResult result = buildResult();
		when( service.process( request, keyProvider ) ).thenReturn( result );
		processor.executeQuery( request );
		verify( logService, times( 2 ) ).log( logCaptor.capture() );
		List<ActionLog> logs = logCaptor.getAllValues();
		verifyStartLog( logs.get( 0 ) );
		verifySuccessLog( logs.get( 1 ) );
		verify( sqsService ).newMessage( QUEUE, result );
	}

	@Test
	public void processFailure() {
		InvoiceQueryRequest request = buildRequest();
		doThrow( new ActionException( EXCEPTION_MESSAGE ) ).when( service ).process( request, keyProvider );
		when( queues.queueRetrieveResponseName() ).thenReturn( QUEUE );
		processor.executeQuery( request );
		verify( logService, times( 2 ) ).log( logCaptor.capture() );
		List<ActionLog> logs = logCaptor.getAllValues();
		verifyStartLog( logs.get( 0 ) );
		verifyFailLog( logs.get( 1 ) );
		verify( sqsService ).newMessage( same( QUEUE ), messageCaptor.capture() );
		InvoiceQueryResult errorMessage = messageCaptor.getValue();
		assertThat( errorMessage.isSuccess(), is( false ) );
		assertThat( errorMessage.getReason(), equalTo( EXCEPTION_MESSAGE ) );
		assertThat( errorMessage.getCompanyId(), equalTo( COMPANY_ID ) );
	}

	private InvoiceQueryResult buildResult() {
		InvoiceQueryResult result = InvoiceQueryResult.builder()
				.resultXml( RESULT_XML )
				.build();
		result.setReason( REASON );
		return result;
	}

	private void verifyStartLog(ActionLog log) {
		assertThat( log.getApp(), equalTo( App.MDE ) );
		assertThat( log.getKey(), equalTo( NSU ) );
		assertThat( log.getMessage(), equalTo( MDE_START ) );
		assertThat( log.getType(), equalTo( MDE ) );
		assertThat( log.getData(), containsString( NSU ) );
		assertThat( log.getData(), containsString( STATE_CODE ) );
		assertThat( log.getData(), containsString( DOCUMENT ) );
		assertThat( log.getTenantId(), equalTo( COMPANY_ID ) );
	}

	private void verifySuccessLog(ActionLog log) {
		assertThat( log.getApp(), equalTo( App.MDE ) );
		assertThat( log.getKey(), equalTo( NSU ) );
		assertThat( log.getMessage(), equalTo( MDE_SUCCESS ) );
		assertThat( log.getType(), equalTo( MDE ) );
		assertThat( log.getTenantId(), equalTo( COMPANY_ID ) );
		assertThat( log.getText(), equalTo( REASON ) );
		assertThat( log.getData(), equalTo( RESULT_XML ) );
	}

	private void verifyFailLog(ActionLog log) {
		assertThat( log.getApp(), equalTo( App.MDE ) );
		assertThat( log.getKey(), equalTo( NSU ) );
		assertThat( log.getMessage(), equalTo( MDE_FAIL ) );
		assertThat( log.getType(), equalTo( MDE ) );
		assertThat( log.getTenantId(), equalTo( COMPANY_ID ) );
		assertThat( log.getText(), equalTo( EXCEPTION_MESSAGE ) );
	}

	private InvoiceQueryRequest buildRequest() {
		return InvoiceQueryRequest.builder()
				.companyId( COMPANY_ID )
				.invoiceQuery( buildQuery() )
				.keyFilename( KEY_FILENAME )
				.keyPassword( KEY_PASSWORD )
				.keyToken(KEY_TOKEN)
				.environment(Environment.HOMOLOGATION)
				.build();
	}

	private InvoiceQuery buildQuery() {
		return InvoiceQuery.builder()
				.lastUniqueSequentialNumber( NSU )
				.uf( UF.SC )
				.document( Document.cnpj( DOCUMENT ) )
				.build();
	}

}
