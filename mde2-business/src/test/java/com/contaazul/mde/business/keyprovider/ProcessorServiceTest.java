package com.contaazul.mde.business.keyprovider;

import static com.contaazul.invoiceissuer.api.Environment.HOMOLOGATION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.nfe.key.PrivateKeyData;
import com.contaazul.nfe.key.SimpleKeyProvider;
import com.contaazul.security.cryptography.InvoiceCryptographyService;

public class ProcessorServiceTest {

	private static final String UNCRYPTED_PASSWORD = "567";
	private static final String FILE_NAME = "asd";
	private static final char[] PASSWORD = "C01034".toCharArray();
	private static final char[] TOKEN = "DJF32323RSAFR3F".toCharArray();

	@Mock
	private S3Context context;

	@Mock
	private InvoiceCryptographyService invoiceCryptographyService;

	@InjectMocks
	@Spy
	private ProcessorService service;

	private ArgumentCaptor<PrivateKeyData> argument = ArgumentCaptor.forClass(PrivateKeyData.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		doReturn(new SimpleKeyProvider(null, null, null)).when(service)
				.buildKeyProvider(Mockito.any(PrivateKeyData.class));
	}

	@Test
	public void buildKeyProviderWithPassword() {
		service.buildKeyProvider(FILE_NAME, PASSWORD, null, HOMOLOGATION, context);
		verify(service).buildKeyProvider(argument.capture());
		verify(invoiceCryptographyService).decrypt(null, HOMOLOGATION);
		assertEquals(PASSWORD, argument.getValue().getPassword());
	}
	
	@Test
	public void buildKeyProviderWithToken() {
		when(invoiceCryptographyService.decrypt(TOKEN, HOMOLOGATION)).thenReturn(UNCRYPTED_PASSWORD.toCharArray());
		service.buildKeyProvider(FILE_NAME, PASSWORD, TOKEN, HOMOLOGATION, context);
		verify(service).buildKeyProvider(argument.capture());
		verify(invoiceCryptographyService).decrypt(TOKEN, HOMOLOGATION);
		assertEquals(UNCRYPTED_PASSWORD, new String(argument.getValue().getPassword()));
	}

}
