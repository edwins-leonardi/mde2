package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.aws.MDeS3Service;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.mde.purchase.aws.exception.S3InvoiceSavingException;
import com.google.common.io.CharStreams;

public class EventXMLBridgeTest {
	private static final String GENERATED_RESPONSE_FILENAME = "4_eventResponse.xml";
	private static final String GENERATED_REQUEST_FILENAME = "4_eventRequest.xml";
	private static final Long COMPANY_ID = 3L;
	private static final Long INTEGRATION_ID = 4L;
	private static final String RESPONSE_XML = "<evento />";
	@Mock
	private MDeS3Service s3Service;
	@InjectMocks
	private EventXMLBridge bridge;
	@Mock
	private S3Context s3Context;
	@Mock
	private InputStream requestInputStream;
	@Captor
	private ArgumentCaptor<String> filenameCaptor;
	@Captor
	private ArgumentCaptor<InputStream> streamCaptor;
	@Rule
	public ExpectedException expectedExcetion = ExpectedException.none();

	@Before
	public void setup() throws Exception {
		initMocks( this );
	}

	@Test
	public void save() throws Exception {
		when( s3Service.createContext( COMPANY_ID ) ).thenReturn( s3Context );
		bridge.save( buildResult() );
		verify( s3Context, times( 2 ) ).save( filenameCaptor.capture(), streamCaptor.capture() );
		assertThat( filenameCaptor.getAllValues().get( 0 ), equalTo( GENERATED_REQUEST_FILENAME ) );
		assertThat( filenameCaptor.getAllValues().get( 1 ), equalTo( GENERATED_RESPONSE_FILENAME ) );
		assertThat( streamCaptor.getAllValues().get( 0 ), equalTo( requestInputStream ) );
		InputStream responseInputStream = streamCaptor.getAllValues().get( 1 );
		assertThat( CharStreams.toString( new InputStreamReader( responseInputStream ) ), equalTo( RESPONSE_XML ) );
	}

	@Test
	public void errorWhenSaving() throws Exception {
		when( s3Service.createContext( COMPANY_ID ) ).thenReturn( s3Context );
		doThrow( IOException.class ).when( s3Context ).save( GENERATED_REQUEST_FILENAME, requestInputStream );
		expectedExcetion.expect( S3InvoiceSavingException.class );
		expectedExcetion.expectMessage( GENERATED_REQUEST_FILENAME );
		bridge.save( buildResult() );
	}

	private ParentResult<RecipientManifestationBatchResult> buildResult() {
		return ParentResult.<RecipientManifestationBatchResult> builder()
				.xml( requestInputStream )
				.result( buildBatchResult() )
				.build();
	}

	private RecipientManifestationBatchResult buildBatchResult() {
		RecipientManifestationBatchResult result = new RecipientManifestationBatchResult();
		result.setCompanyId( COMPANY_ID );
		result.setIntegrationId( INTEGRATION_ID );
		result.setResultXml( RESPONSE_XML );
		return result;
	}

}
