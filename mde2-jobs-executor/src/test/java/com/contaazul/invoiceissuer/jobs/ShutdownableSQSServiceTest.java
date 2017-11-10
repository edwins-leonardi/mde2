package com.contaazul.invoiceissuer.jobs;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.contaazul.aws.config.AWSConfig;
import com.contaazul.invoiceissuer.jobs.sqs.ShutdownableSQSService;


public class ShutdownableSQSServiceTest {
	private static final String QUEUE = "random-queue";
	private static final String URL = "blah://any";

	@Mock
	private ShutdownableSQSService sqs;
	@Mock
	private AmazonSQS amazonSqs;
	@Mock
	private AWSConfig awsConfig;
	@Mock
	private AWSCredentialsProvider awsCredentialsProvider;

	@Before
	public void init() {
		initMocks( this );
		doCallRealMethod().when( sqs ).shutdown();
		doCallRealMethod().when( sqs ).isShutingdown();
		doCallRealMethod().when( sqs ).readMessages( anyInt(), any( AmazonSQS.class ), anyString() );
	}

	@Test
	public void testNormalBehavior() throws Exception {
		sqs.list( 1, QUEUE, String.class );
		sqs.readMessages( 1, amazonSqs, URL );
		verify( sqs ).doReadMessages( anyInt(), any( AmazonSQS.class ), anyString() );
	}

	@Test
	public void testShuttingDown() throws Exception {
		sqs.shutdown();
		sqs.readMessages( 1, amazonSqs, URL );
		verify( sqs, times( 0 ) ).doReadMessages( anyInt(), any( AmazonSQS.class ), anyString() );
	}
}
