package com.contaazul.mde.business.action;

import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.NfeCabecMsg;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.NfeCabecMsgDocument;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.NfeDadosMsgDocument;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.NfeDadosMsgDocument.NfeDadosMsg;

public class EventBuilderFactory {

	public BodyBuilder body() {
		return content -> {
			NfeDadosMsgDocument factory = NfeDadosMsgDocument.Factory.newInstance();
			NfeDadosMsg body = factory.addNewNfeDadosMsg();
			body.set( content );
			return factory;
		};
	}

	public HeaderBuilder header() {
		return code -> {
			NfeCabecMsgDocument factory = NfeCabecMsgDocument.Factory.newInstance();
			NfeCabecMsg header = factory.addNewNfeCabecMsg();
			header.setCUF( code );
			header.setVersaoDados( LayoutVersion.V1_00.getCode() );
			return factory;
		};
	}
}
