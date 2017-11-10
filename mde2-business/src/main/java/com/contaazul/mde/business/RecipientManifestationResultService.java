package com.contaazul.mde.business;

import javax.inject.Inject;

import org.w3c.dom.Document;

import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.business.xml.signer.XmlMarshallerService;
import com.contaazul.mde.converter.RecipientManifestationBatchResultConverter;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TRetEnvEvento;
import com.contaazul.mde.result.RecipientManifestationResultParser;

public class RecipientManifestationResultService {
	@Inject
	private RecipientManifestationResultParser parser;
	@Inject
	private RecipientManifestationBatchResultConverter converter;
	@Inject
	private XmlMarshallerService marshallerService;

	public RecipientManifestationBatchResult convert(Document document) {
		TRetEnvEvento sefazResult = parser.parse( document );
		RecipientManifestationBatchResult result = converter.convert( sefazResult );
		result.setResultXml( marshallerService.asString( sefazResult ) );
		return result;
	}

}
