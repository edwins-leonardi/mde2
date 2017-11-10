package com.contaazul.mde.business.action;

import org.apache.xmlbeans.XmlObject;

import com.contaazul.invoiceissuer.api.request.Result;

public interface ResultBuilder<T extends Result> {
	T build(XmlObject xml);
}
