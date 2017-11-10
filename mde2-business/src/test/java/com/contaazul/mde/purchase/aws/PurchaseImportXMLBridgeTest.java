package com.contaazul.mde.purchase.aws;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.danfe.DanfeEmitter;
import com.contaazul.danfe.DanfeEmitterException;
import com.contaazul.mde.converter.DanfeGenerationException;
import com.contaazul.mde.purchase.aws.exception.S3InvoiceSavingException;

public class PurchaseImportXMLBridgeTest {
	private static final Long TENANT_ID = 3L;
	private static final String FILENAME = "NFe35150410807909000124550010000034831413600373";
	private static final String XML_FILENAME = "NFe35150410807909000124550010000034831413600373.xml";
	private static final String PDF_FILENAME = "NFe35150410807909000124550010000034831413600373.pdf";
	private static final String XML = "<nfeProc />";
	@Mock
	private PurchaseImportS3Service s3Service;
	@Mock
	private DanfeEmitter danfeEmitter;
	@InjectMocks
	private PurchaseImportXMLBridge service;
	@Mock
	private S3Context context;
	@Mock
	private InputStream danfe;
	@Captor
	private ArgumentCaptor<String> filenameCaptor;
	@Captor
	private ArgumentCaptor<InputStream> streamCaptor;
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void save() throws Exception {
		when( s3Service.createContext( TENANT_ID ) ).thenReturn( context );
		when( danfeEmitter.emitDanfe( any( InputStream.class ) ) ).thenReturn( danfe );
		service.save( TENANT_ID, FILENAME, XML );
		verify( context, times( 2 ) ).save( filenameCaptor.capture(), streamCaptor.capture() );
		assertThat( filenameCaptor.getAllValues().get( 0 ), equalTo( XML_FILENAME ) );
		assertThat( filenameCaptor.getAllValues().get( 1 ), equalTo( PDF_FILENAME ) );
		assertThat( streamCaptor.getAllValues().get( 1 ), equalTo( danfe ) );
	}

	@Test
	public void errorWhenSavingXMLToS3() throws Exception {
		when( s3Service.createContext( TENANT_ID ) ).thenReturn( context );
		when( danfeEmitter.emitDanfe( any( InputStream.class ) ) ).thenReturn( danfe );
		doThrow( IOException.class ).when( context ).save( eq( XML_FILENAME ), any( InputStream.class ) );
		expectedException.expect( S3InvoiceSavingException.class );
		expectedException.expectMessage( contains( XML_FILENAME ) );
		service.save( TENANT_ID, FILENAME, XML );
	}

	@Test
	public void errorWhenSavingPDFToS3() throws Exception {
		when( s3Service.createContext( TENANT_ID ) ).thenReturn( context );
		when( danfeEmitter.emitDanfe( any( InputStream.class ) ) ).thenReturn( danfe );
		doThrow( IOException.class ).when( context ).save( eq( PDF_FILENAME ), any( InputStream.class ) );
		expectedException.expect( S3InvoiceSavingException.class );
		expectedException.expectMessage( contains( PDF_FILENAME ) );
		service.save( TENANT_ID, FILENAME, XML );
	}

	@Test
	public void errorWhenGeneratingDanfe() throws Exception {
		when( s3Service.createContext( TENANT_ID ) ).thenReturn( context );
		when( danfeEmitter.emitDanfe( any( InputStream.class ) ) ).thenReturn( danfe );
		doThrow( DanfeEmitterException.class ).when( danfeEmitter ).emitDanfe( any( InputStream.class ) );
		expectedException.expect( DanfeGenerationException.class );
		expectedException.expectMessage( contains( PDF_FILENAME ) );
		service.save( TENANT_ID, FILENAME, XML );
	}

}
