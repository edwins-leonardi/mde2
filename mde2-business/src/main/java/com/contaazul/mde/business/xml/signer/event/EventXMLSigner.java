package com.contaazul.mde.business.xml.signer.event;

import javax.xml.crypto.dsig.XMLSignatureFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.contaazul.nfe.key.KeyProvider;
import com.contaazul.nfe.signer.DocumentReader;
import com.contaazul.nfe.signer.SignerKeyProvider;
import com.contaazul.nfe.signer.XMLSignatureAbstractFactory;
import com.contaazul.nfe.signer.XMLSigner;

public class EventXMLSigner extends XMLSigner {
	private static final XMLSignatureFactory FACTORY = new XMLSignatureAbstractFactory().build();

	public EventXMLSigner(KeyProvider provider) {
		super( SignerKeyProvider.decorate( provider, FACTORY ), FACTORY, new DocumentReader() );
	}

	@Override
	protected String id(Document document) {
		Element infNfe = (Element) document.getElementsByTagName( "infEvento" ).item( 0 );
		infNfe.setIdAttribute( "Id", true );
		return infNfe.getAttribute( "Id" );
	}

	@Override
	protected Node getParent(Document document) {
		return document.getElementsByTagName( "evento" ).item( 0 );
	}

}
