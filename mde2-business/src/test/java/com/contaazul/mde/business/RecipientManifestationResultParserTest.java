package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.contaazul.mde.business.action.BasicResultParser;
import com.contaazul.mde.business.action.XmlParseException;
import com.contaazul.mde.business.xml.context.EventResponseContext;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TRetEnvEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TretEvento.InfEvento;
import com.contaazul.mde.result.RecipientManifestationResultParser;

public class RecipientManifestationResultParserTest {
	private static final String INVOICE_KEY = "35150410807909000124550010000034831413600373";
	private static final String EVENT_PROCESSED_REASON = "Evento recebido";
	private static final String EVENT_SUCCESS_CODE = "135";
	private static final String RESULT_EXAMPLE_FILENAME = "/com/contaazul/mde/result/nfeRecepcaoEventoResult_example.xml";

	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private BasicResultParser basicResultParser = new BasicResultParser();
	@Mock
	private EventResponseContext context;
	@InjectMocks
	private RecipientManifestationResultParser parser;
	private Document result;

	@Before
	public void setup() throws Exception {
		initMocks( this );
		result = parseResultFromExampleFile( RESULT_EXAMPLE_FILENAME );
		when( context.getContext() ).thenReturn( JAXBContext.newInstance( TRetEnvEvento.class ) );
	}

	private Document parseResultFromExampleFile(String pathname) throws Exception {
		return createDocumentBuilder().parse( getClass().getResourceAsStream( pathname ) );
	}

	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware( true );
		return documentBuilderFactory.newDocumentBuilder();
	}

	@Test
	public void parse() throws Exception {
		TRetEnvEvento eventResult = parser.parse( result );
		InfEvento infEvento = eventResult.getRetEvento().get( 0 ).getInfEvento();
		assertThat( infEvento.getCStat(), equalTo( EVENT_SUCCESS_CODE ) );
		assertThat( infEvento.getXMotivo(), equalTo( EVENT_PROCESSED_REASON ) );
		assertThat( infEvento.getChNFe(), equalTo( INVOICE_KEY ) );
		String[] a = new String[] {};
	}

	@Test(expected = XmlParseException.class)
	public void errorParsing() throws Exception {
		doThrow( XmlParseException.class ).when( basicResultParser ).parse(
				any( Node.class ),
				same( TRetEnvEvento.class ),
				any( JAXBContext.class
				) );
		parser.parse( result );
	}
}
