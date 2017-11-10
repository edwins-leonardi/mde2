package com.contaazul.mde.business.action;

import static com.contaazul.invoiceissuer.api.Environment.HOMOLOGATION;
import static com.contaazul.invoiceissuer.api.Environment.PRODUCTION;

import org.apache.axis2.client.Stub;
import org.apache.xmlbeans.XmlObject;

import com.contaazul.invoiceissuer.api.action.url.ActionURL;
import com.contaazul.invoiceissuer.api.request.Result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Action {
	private ActionURL actionUrl;
	private final Class<? extends Stub> stub;
	private final HeaderBuilder header;
	private final BodyBuilder body;
	private final ResultBuilder<? extends Result> result;
	private final Method method;

	public String getURL(boolean production) {
		return actionUrl.get( production ? PRODUCTION : HOMOLOGATION );
	}

	public XmlObject header(String code) {
		return header.build( code );
	}

	public XmlObject body(XmlObject content) {
		return body.build( content );
	}

	@SuppressWarnings("unchecked")
	public <T extends Result> T parse(XmlObject xml) {
		return (T) result.build( xml );
	}

}
