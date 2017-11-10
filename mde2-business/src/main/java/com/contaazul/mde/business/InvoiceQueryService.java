package com.contaazul.mde.business;

import javax.inject.Inject;

import com.contaazul.mde.api.request.InvoiceQueryRequest;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.purchase.aws.XMLPersistService;
import com.contaazul.nfe.key.KeyProvider;

public class InvoiceQueryService {
	@Inject
	private Interactor interactor;
	@Inject
	private XMLPersistService xmlPersistService;

	public InvoiceQueryResult process(InvoiceQueryRequest invoiceQueryRequest, KeyProvider keyProvider) {
		InvoiceQueryResult invoiceQueryResult = interactor.query( invoiceQueryRequest, keyProvider );
		invoiceQueryResult.setCompanyId( invoiceQueryRequest.getCompanyId() );
		xmlPersistService.save( invoiceQueryResult );
		return invoiceQueryResult;
	}
}
