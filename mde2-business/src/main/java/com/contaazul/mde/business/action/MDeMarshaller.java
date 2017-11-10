package com.contaazul.mde.business.action;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.xml.bind.Marshaller.JAXB_ENCODING;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static javax.xml.bind.Marshaller.JAXB_FRAGMENT;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import com.contaazul.nfe.xml.XmlPatcher;

public abstract class MDeMarshaller<T> {
	private static final String NAMESPACE = "http://www.portalfiscal.inf.br/nfe";

	protected abstract Class<T> getClazz();

	protected abstract String getRootTag();

	public InputStream asStream(T xml) {
		try (Writer writer = new StringWriter()) {
			Marshaller marshaller = createMarshaller();
			QName qName = new QName( NAMESPACE, getRootTag() );
			marshaller.marshal( new JAXBElement<T>( qName, getClazz(), xml ), writer );
			String content = writer.toString();
			return new ByteArrayInputStream( monkeyPatchXML( content ).getBytes( UTF_8 ) );
		} catch (JAXBException | IOException e) {
			throw new XmlMarshalException( e, xml );
		}
	}

	private Marshaller createMarshaller() throws JAXBException {
		Marshaller marshaller = JAXBContext.newInstance( getClazz() ).createMarshaller();
		marshaller.setProperty( JAXB_FORMATTED_OUTPUT, false );
		marshaller.setProperty( JAXB_FRAGMENT, true );
		marshaller.setProperty( JAXB_ENCODING, "UTF-8" );
		return marshaller;
	}

	protected String monkeyPatchXML(String xml) {
		return new XmlPatcher( xml ).patch();
	}

}
