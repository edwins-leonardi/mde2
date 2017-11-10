package com.contaazul.mde.business.xml.signer.event;

import lombok.NoArgsConstructor;

import com.contaazul.nfe.signer.Signer;

@NoArgsConstructor
public class EventSigner extends Signer {

	@Override
	protected EventXMLSigner getSigner() {
		return new EventXMLSigner( provider );
	}

}
