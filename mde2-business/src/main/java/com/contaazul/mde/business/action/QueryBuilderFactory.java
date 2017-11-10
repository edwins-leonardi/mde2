package com.contaazul.mde.business.action;

import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NfeDistDFeInteresseDocument;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NfeDistDFeInteresseDocument.NfeDistDFeInteresse;
import br.inf.portalfiscal.www.nfe.wsdl.nfedistribuicaodfe.NfeDistDFeInteresseDocument.NfeDistDFeInteresse.NfeDadosMsg;

public class QueryBuilderFactory {

	public HeaderBuilder header() {
		return code -> null;
	}

	public BodyBuilder body() {
		return content -> {
			NfeDistDFeInteresseDocument document = NfeDistDFeInteresseDocument.Factory.newInstance();
			NfeDistDFeInteresse body = document.addNewNfeDistDFeInteresse();
			NfeDadosMsg nfeDadosMsg = body.addNewNfeDadosMsg();
			nfeDadosMsg.set( content );
			return document;
		};
	}
}
