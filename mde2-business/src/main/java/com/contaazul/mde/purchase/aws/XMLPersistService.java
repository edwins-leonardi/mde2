package com.contaazul.mde.purchase.aws;

import javax.inject.Inject;

import com.contaazul.mde.api.request.Invoice;
import com.contaazul.mde.api.request.InvoiceBatch;
import com.contaazul.mde.api.request.InvoiceQueryResult;

public class XMLPersistService {
	@Inject
	private PurchaseImportXMLBridge brigde;

	public void save(InvoiceQueryResult result) {
		InvoiceBatch batch = result.getInvoices();
		for (Invoice invoice : batch.getProcessedInvoices()) {
			brigde.save( result.getCompanyId(), invoice.getInvoiceKey(), invoice.getXml() );
			discardXmlAfterSaving( invoice );
		}
	}

	private void discardXmlAfterSaving(Invoice invoice) {
		invoice.setXml( null );
	}
}
