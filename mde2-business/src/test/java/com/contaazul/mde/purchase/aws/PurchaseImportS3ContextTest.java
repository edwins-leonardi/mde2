package com.contaazul.mde.purchase.aws;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.contaazul.aws.s3.S3Context;

public class PurchaseImportS3ContextTest {
	private static final String BUCKETNAME = "bucket";
	private static final Long TENANT_ID = 1L;

	@Mock
	private AmazonS3 client;

	@Before
	public void init() {
		initMocks( this );
	}

	@Test(expected = NullPointerException.class)
	public void withNullTenantId() throws Exception {
		new PurchaseImportS3Context( null, getS3Context() );
	}

	@Test(expected = NullPointerException.class)
	public void withNullContext() throws Exception {
		new PurchaseImportS3Context( TENANT_ID, null );
	}

	@Test
	public void prependFolder() throws Exception {
		S3Context context = new PurchaseImportS3Context( TENANT_ID, getS3Context() );
		when( client.getObject( eq( BUCKETNAME ), anyString() ) ).thenReturn( new S3Object() );
		context.retrieve( "file" );
		verify( client ).getObject( BUCKETNAME, "1/notafiscal/purchase/import/file" );
	}

	private S3Context getS3Context() {
		return new S3Context( client, "" + TENANT_ID, BUCKETNAME );
	}

}
