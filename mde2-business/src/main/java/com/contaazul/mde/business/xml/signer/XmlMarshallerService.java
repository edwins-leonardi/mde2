package com.contaazul.mde.business.xml.signer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.xml.bind.JAXB;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import com.contaazul.invoiceissuer.parser.XmlObjectParser;
import com.contaazul.mde.business.action.XmlMarshalException;
import com.contaazul.mde.business.action.XmlParseException;
import com.contaazul.mde.business.xml.EventMarshaller;
import com.contaazul.mde.business.xml.QueryMarshaller;
import com.contaazul.mde.business.xml.signer.event.EventSigner;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEnvEvento;
import com.contaazul.nfe.key.KeyProvider;

public class XmlMarshallerService {
	@Inject
	private XmlObjectParser xmlObjectParser;
	@Inject
	private QueryMarshaller queryMarshaller;
	@Inject
	private EventMarshaller eventMarshaller;

	public XmlObject marshalQuery(DistDFeInt distDFeInt) {
		try {
			return xmlObjectParser.parse( queryMarshaller.asStream( distDFeInt ) );
		} catch (IOException | XmlException e) {
			throw new XmlMarshalException( e, distDFeInt );
		}
	}

	public InputStream signEvent(KeyProvider keyProvider, TEnvEvento tEnvEvento) {
		return new EventSigner()
				.xml( eventMarshaller.asStream( tEnvEvento ) )
				.keyProvider( keyProvider )
				.sign();
	}

	public XmlObject parse(InputStream inputStream) {
		try {
			return xmlObjectParser.parse( inputStream );
		} catch (IOException | XmlException e) {
			throw new XmlParseException( e );
		}
	}

	public String asString(Object object) {
		StringWriter writer = new StringWriter();
		JAXB.marshal( object, writer );
		return writer.toString();
	}
}
