package com.contaazul.invoiceissuer.jobs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import lombok.Setter;

import com.contaazul.aws.sqs.SQSMessage;
import com.contaazul.distributedjob.JobException;
import com.contaazul.invoiceissuer.config.InvoiceIssuerPublicQueueNames;
import com.contaazul.invoiceissuer.jobs.sqs.ShutdownableSQSService;

public class AbstractSqsJobTest {
	private static final String QUEUE_NAME = "queue";
	private static String MESSAGE_ID = "60b291da-a26e-4962-af56-d99c4190a12b";

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Spy
	@InjectMocks
	private MyJob job;
	@Mock
	private InvoiceIssuerPublicQueueNames config;
	@Mock
	private ShutdownableSQSService sqs;

	@Before
	public void init() {
		initMocks( this );
	}

	@Test
	public void testEnabled() throws Exception {
		when( sqs.isShutingdown() ).thenReturn( false );
		assertThat( job.shouldExecute(), is( true ) );
	}

	@Test
	public void testDisabled() throws Exception {
		when( sqs.isShutingdown() ).thenReturn( true );
		assertThat( job.shouldExecute(), is( false ) );
	}

	@Test
	public void removeFromMsgFromSqsOnLockFailure() throws Exception {
		SQSMessage<String> msg = new SQSMessage<String>( MESSAGE_ID, "receipt", "a" );
		job.handleLockFailure( msg );
		verify( sqs ).delete( QUEUE_NAME, msg );
	}

	@Test
	public void testStringify() throws Exception {
		String stringified = job.stringfy( new SQSMessage<String>( MESSAGE_ID, "receipt", "data" ) );
		assertThat( stringified, equalTo( MESSAGE_ID ) );
	}

	@Test
	public void disabledHostShouldNotExecute() throws Exception {
		when( sqs.isShutingdown() ).thenReturn( true );
		SQSMessage<String> msg = new SQSMessage<String>( MESSAGE_ID, "receipt", "data" );
		exception.expect( JobException.class );
		job.processElement( msg );
		verify( sqs, never() ).delete( anyString(), eq( msg ) );
		verify( job, never() ).doProcessSqsElement( msg.getData() );
	}

	@Test
	public void enabledHostShouldExecute() throws Exception {
		when( sqs.isShutingdown() ).thenReturn( false );
		SQSMessage<String> msg = new SQSMessage<String>( "id", "receipt", "data" );
		job.processElement( msg );
		verify( sqs ).delete( anyString(), eq( msg ) );
		verify( job ).doProcessSqsElement( msg.getData() );
	}

	@Test
	public void requestsElementsToProcessFromSQS() throws Exception {
		job.findElementsToProcess();
		verify( sqs ).iterable( job.queueName(), job.sqsClass() );
	}

	@Setter
	private static class MyJob extends AbstractSqsJob<String> {

		@Override
		protected String queueName() {
			return QUEUE_NAME;
		}

		@Override
		protected Class<String> sqsClass() {
			return String.class;
		}

		@Override
		protected void doProcessSqsElement(String data) throws Exception {
			// does nothing
		}
	}
}
