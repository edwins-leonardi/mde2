package com.contaazul.mde.business.action;

import javax.inject.Inject;

import org.w3c.dom.Document;

import com.contaazul.invoiceissuer.api.request.Result;
import com.contaazul.mde.business.QueryResultService;
import com.contaazul.mde.business.RecipientManifestationResultService;

public class ResultBuilderFactory {
	@Inject
	private QueryResultService queryResultService;
	@Inject
	private RecipientManifestationResultService recipientManifestationResultService;

	public ResultBuilder<? extends Result> queryResult() {
		return xml -> queryResultService.convert( (Document) xml.getDomNode() );
	}

	public ResultBuilder<? extends Result> eventResult() {
		return xml -> recipientManifestationResultService.convert( (Document) xml.getDomNode() );
	}

}
