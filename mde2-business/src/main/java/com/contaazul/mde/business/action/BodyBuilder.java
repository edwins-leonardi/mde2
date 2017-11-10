package com.contaazul.mde.business.action;

import org.apache.xmlbeans.XmlObject;

public interface BodyBuilder {
	XmlObject build(XmlObject content);
}
