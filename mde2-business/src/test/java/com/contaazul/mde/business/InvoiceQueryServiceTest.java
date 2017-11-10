package com.contaazul.mde.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.mde.api.config.MDePublicQueuesNames;
import com.contaazul.mde.api.request.InvoiceQueryRequest;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.purchase.aws.XMLPersistService;
import com.contaazul.nfe.key.KeyProvider;

public class InvoiceQueryServiceTest {
	private static final Long COMPANY_ID = 7L;
	private static final String QUEUE = "mde-retrieve-response";
	@Mock
	private MDePublicQueuesNames queues;
	@Mock
	private Interactor interactor;
	@Mock
	private XMLPersistService xmlPersistService;
	@InjectMocks
	private InvoiceQueryService invoiceQueryService;
	@Mock
	private KeyProvider keyProvider;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void process() {
		InvoiceQueryResult result = new InvoiceQueryResult();
		InvoiceQueryRequest request = new InvoiceQueryRequest();
		request.setCompanyId( COMPANY_ID );
		when( queues.queueRetrieveResponseName() ).thenReturn( QUEUE );
		when( interactor.query( request, keyProvider ) ).thenReturn( result );
		invoiceQueryService.process( request, keyProvider );
		verify( xmlPersistService ).save( result );
	}

}
