package com.contaazul.mde.business.action;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;

public class BasicResultParser {
	public <T> T parse(Node node, Class<T> resultClass, JAXBContext context) {
		try {
			JAXBElement<T> jaxbElement = context.createUnmarshaller().unmarshal( node, resultClass );
			return jaxbElement.getValue();
		} catch (JAXBException e) {
			throw new XmlParseException( e );
		}
	}

	public <T> T parse(String xml, Class<T> resultClass, JAXBContext context) {
		try {
			JAXBElement<T> jaxbElement = context.createUnmarshaller().unmarshal( createXMLSource( xml ), resultClass );
			return jaxbElement.getValue();
		} catch (JAXBException e) {
			throw new XmlParseException( e );
		}
	}

	private StreamSource createXMLSource(String xml) {
		return new StreamSource( new StringReader( xml ) );
	}

}
