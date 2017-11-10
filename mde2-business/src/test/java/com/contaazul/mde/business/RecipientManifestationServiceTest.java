package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.aws.sqs.SQSService;
import com.contaazul.mde.api.config.MDePublicQueuesNames;
import com.contaazul.mde.api.request.RecipientManifestationBatch;
import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.api.request.RecipientManifestationRequest;
import com.contaazul.mde.aws.MDeS3Service;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.nfe.key.KeyProvider;

public class RecipientManifestationServiceTest {
	private static final long COMPANY_ID = 3L;
	private static final long INTEGRATION_ID = 10L;
	private static final String QUEUE = "mde-event-acknowledge-request";
	@Mock
	private MDePublicQueuesNames queues;
	@Mock
	private SQSService sqsService;
	@Mock
	private Interactor interactor;
	@Mock
	private KeyProvider keyProvider;
	@Mock
	private MDeS3Service s3;
	@Mock
	private EventXMLBridge eventXMLBridge;
	@InjectMocks
	private RecipientManifestationService service;

	@Captor
	private ArgumentCaptor<RecipientManifestationBatchResult> captor;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void process() {
		RecipientManifestationRequest request = buildRequest();
		when( queues.queueRequestEventAcknowledge() ).thenReturn( QUEUE );
		ParentResult<RecipientManifestationBatchResult> result = buildResult();
		when( interactor.event( request.getBatch(), keyProvider ) ).thenReturn( result );
		service.process( request, keyProvider, QUEUE );
		verify( sqsService ).newMessage( same( QUEUE ), captor.capture() );
		assertThat( captor.getValue().getCompanyId(), equalTo( COMPANY_ID ) );
		assertThat( captor.getValue().getIntegrationId(), equalTo( INTEGRATION_ID ) );
		verify( eventXMLBridge ).save( result );
	}

	private ParentResult<RecipientManifestationBatchResult> buildResult() {
		return ParentResult.<RecipientManifestationBatchResult> builder()
				.result( new RecipientManifestationBatchResult() )
				.build();
	}

	private RecipientManifestationRequest buildRequest() {
		return RecipientManifestationRequest.builder()
				.companyId( COMPANY_ID )
				.integrationId( INTEGRATION_ID )
				.batch( new RecipientManifestationBatch() )
				.build();
	}
}
