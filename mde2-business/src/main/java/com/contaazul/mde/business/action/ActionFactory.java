package com.contaazul.mde.business.action;

import javax.inject.Inject;

import com.contaazul.invoiceissuer.api.action.url.ServiceURL;
import com.contaazul.sefaz.mde.NFeDistribuicaoDFeStub;
import com.contaazul.sefaz.mde.RecepcaoEventoStub;

public class ActionFactory {
	private static final ServiceURL SEFAZ_URL = new ServiceURL(
			"https://www1.nfe.fazenda.gov.br/",
			"https://hom.nfe.fazenda.gov.br/" );

	@Inject
	private BuilderFactory builderFactory;

	public Action query() {
		return Action.builder()
				.actionUrl( SEFAZ_URL.with( "NFeDistribuicaoDFe/NFeDistribuicaoDFe.asmx" ) )
				.stub( NFeDistribuicaoDFeStub.class )
				.header( builderFactory.queryHeader() )
				.body( builderFactory.queryBody() )
				.result( builderFactory.queryResult() )
				.method( Method.QUERY )
				.build();
	}

	public Action event() {
		return Action.builder()
				.actionUrl( SEFAZ_URL.with( "RecepcaoEvento/RecepcaoEvento.asmx" ) )
				.stub( RecepcaoEventoStub.class )
				.header( builderFactory.eventHeader() )
				.body( builderFactory.eventBody() )
				.result( builderFactory.eventResult() )
				.method( Method.EVENT )
				.build();
	}

}
