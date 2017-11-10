package com.contaazul.mde.business.action;

import javax.inject.Inject;

import com.contaazul.invoiceissuer.api.request.Result;

public class BuilderFactory {
	@Inject
	private QueryBuilderFactory queryBuilderFactory;
	@Inject
	private EventBuilderFactory eventBuilderFactory;
	@Inject
	private ResultBuilderFactory resultBuilderFactory;

	public HeaderBuilder queryHeader() {
		return queryBuilderFactory.header();
	}

	public BodyBuilder queryBody() {
		return queryBuilderFactory.body();
	}

	public ResultBuilder<? extends Result> queryResult() {
		return resultBuilderFactory.queryResult();
	}

	public HeaderBuilder eventHeader() {
		return eventBuilderFactory.header();
	}

	public BodyBuilder eventBody() {
		return eventBuilderFactory.body();
	}

	public ResultBuilder<? extends Result> eventResult() {
		return resultBuilderFactory.eventResult();
	}

}
