package com.contaazul.mde.purchase.aws;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.amazonaws.services.s3.AmazonS3;
import com.contaazul.aws.s3.S3Context;
import com.contaazul.aws.s3.S3Service;

public class PurchaseImportS3ServiceTest {
	@Mock
	private S3Service s3;
	@InjectMocks
	private PurchaseImportS3Service service;

	@Before
	public void init() {
		initMocks( this );
		when( s3.createContext( eq( 1L ) ) ).thenReturn( new S3Context( mock( AmazonS3.class ), "1", "" ) );
	}

	@Test
	public void correctInstanceType() throws Exception {
		assertThat( service.createContext( 1L ), instanceOf( PurchaseImportS3Context.class ) );
	}

}
