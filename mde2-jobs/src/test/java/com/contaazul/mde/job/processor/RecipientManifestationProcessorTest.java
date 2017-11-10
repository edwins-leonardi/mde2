package com.contaazul.mde.job.processor;

import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_FAIL;
import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_START;
import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_SUCCESS;
import static com.contaazul.invoiceissuer.entities.ActionLogType.MDE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.Arrays;
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
import com.contaazul.invoiceissuer.entities.ActionLog;
import com.contaazul.invoiceissuer.entities.ActionLogMessage;
import com.contaazul.invoiceissuer.entities.App;
import com.contaazul.invoiceissuer.service.LogService;
import com.contaazul.mde.api.request.RecipientManifestation;
import com.contaazul.mde.api.request.RecipientManifestationBatch;
import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.api.request.RecipientManifestationRequest;
import com.contaazul.mde.aws.MDeS3Service;
import com.contaazul.mde.business.RecipientManifestationService;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.mde.business.keyprovider.ProcessorService;
import com.contaazul.nfe.key.KeyProvider;

public class RecipientManifestationProcessorTest {
	private static final Long COMPANY_ID = 1L;
	private static final Long INTEGRATION_ID = 3L;
	private static final String FILENAME = "biirrl";
	private static final char[] PASSWORD = { 'a' };
	private static final String INVOICE_KEY = "35150410807909000124550010000034831413600373";
	private static final String QUEUE = "mde-event-acknowledge-request";
	private static final char[] KEY_TOKEN = "A8A0S8GA0S8".toCharArray();
	@Mock
	private RecipientManifestationService service;
	@Mock
	private KeyProvider keyProvider;
	@Mock
	private MDeS3Service mdeS3Service;
	@Mock
	private S3Context context;
	@Mock
	private ProcessorService processorService;
	@Mock
	private LogService logService;
	@Mock
	private SQSService sqsService;
	@InjectMocks
	private RecipientManifestationProcessor processor;

	@Captor
	private ArgumentCaptor<ActionLog> logCaptor;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void process() {
		RecipientManifestationRequest request = buildRequest();
		when( processorService.buildKeyProvider( FILENAME, PASSWORD, KEY_TOKEN, Environment.HOMOLOGATION, context ) ).thenReturn( keyProvider );
		when( mdeS3Service.createContext( COMPANY_ID ) ).thenReturn( context );
		ParentResult<RecipientManifestationBatchResult> result = buildResult();
		when( service.process( request, keyProvider, QUEUE ) ).thenReturn( result );
		processor.executeEvent( request, QUEUE );
		verifyLogs( MDE_START, MDE_SUCCESS );
	}

	private ParentResult<RecipientManifestationBatchResult> buildResult() {
		return ParentResult.<RecipientManifestationBatchResult> builder()
				.result( buildBatchResult() )
				.build();
	}

	private RecipientManifestationBatchResult buildBatchResult() {
		return new RecipientManifestationBatchResult( 138, "Evento registrado", COMPANY_ID, INTEGRATION_ID );
	}

	@Test
	public void processFailure() {
		RecipientManifestationRequest request = buildRequest();
		doThrow( IOException.class ).when( mdeS3Service ).createContext( COMPANY_ID );
		processor.executeEvent( request, QUEUE );
		verifyLogs( MDE_START, MDE_FAIL );
	}

	private RecipientManifestationRequest buildRequest() {
		return RecipientManifestationRequest.builder()
				.companyId( COMPANY_ID )
				.integrationId( INTEGRATION_ID )
				.keyFilename( FILENAME )
				.keyPassword( PASSWORD )
				.keyToken(KEY_TOKEN)
				.environment(Environment.HOMOLOGATION)
				.batch( RecipientManifestationBatch.builder()
						.events( Arrays.asList( RecipientManifestation.builder()
								.invoiceKey( INVOICE_KEY )
								.build() ) )
						.build() )
				.build();
	}

	private void verifyLogs(ActionLogMessage startLog, ActionLogMessage finishLog) {
		verify( logService, times( 2 ) ).log( logCaptor.capture() );
		List<ActionLog> logs = logCaptor.getAllValues();
		assertLog( logs.get( 0 ), startLog );
		assertLog( logs.get( 1 ), finishLog );
	}

	private void assertLog(ActionLog log, ActionLogMessage expectedMessage) {
		assertThat( log.getApp(), equalTo( App.MDE ) );
		assertThat( log.getInvoiceId(), equalTo( INTEGRATION_ID ) );
		assertThat( log.getTenantId(), equalTo( COMPANY_ID ) );
		assertThat( log.getType(), equalTo( MDE ) );
		assertThat( log.getMessage(), equalTo( expectedMessage ) );
	}

}
