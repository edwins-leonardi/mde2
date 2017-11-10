package com.contaazul.mde.business;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlObject;

import com.contaazul.mde.api.request.InvoiceQueryRequest;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.api.request.RecipientManifestationBatch;
import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.business.action.ActionException;
import com.contaazul.mde.business.action.ActionExecutorService;
import com.contaazul.mde.business.action.ActionFactory;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.mde.business.xml.signer.XmlMarshallerService;
import com.contaazul.mde.converter.InvoiceQueryConverter;
import com.contaazul.mde.converter.RecipientManifestationBatchConverter;
import com.contaazul.nfe.key.KeyProvider;

public class Interactor {
	@Inject
	private ActionFactory actionFactory;
	@Inject
	private ActionExecutorService executor;
	@Inject
	private InvoiceQueryConverter invoiceQueryConverter;
	@Inject
	private RecipientManifestationBatchConverter recipientManifestationBatchConverter;
	@Inject
	private XmlMarshallerService marshallerService;

	public InvoiceQueryResult query(InvoiceQueryRequest invoiceQuery, KeyProvider keyProvider) {
		XmlObject xml = marshalQuery( invoiceQuery );
		return executor.execute( keyProvider, actionFactory.query(), invoiceQuery.getInvoiceQuery().getUf(), xml );
	}

	private XmlObject marshalQuery(InvoiceQueryRequest invoiceQuery) {
		return marshallerService.marshalQuery( invoiceQueryConverter.convert( invoiceQuery.getInvoiceQuery() ) );
	}

	public ParentResult<RecipientManifestationBatchResult> event(RecipientManifestationBatch batch,
			KeyProvider keyProvider) {
		InputStream signedXml = signEvent( batch, keyProvider );
		XmlObject xml = marshallerService.parse( signedXml );
		RecipientManifestationBatchResult result = executor.execute( keyProvider, actionFactory.event(), batch.getUf(),
				xml );
		resetStream( signedXml );
		return new ParentResult<RecipientManifestationBatchResult>( signedXml, result );
	}

	private void resetStream(InputStream signedXml) {
		try {
			signedXml.reset();
		} catch (IOException e) {
			throw new ActionException( e );
		}
	}

	private InputStream signEvent(RecipientManifestationBatch batch, KeyProvider keyProvider) {
		return marshallerService.signEvent( keyProvider, recipientManifestationBatchConverter.convert( batch ) );
	}

}
