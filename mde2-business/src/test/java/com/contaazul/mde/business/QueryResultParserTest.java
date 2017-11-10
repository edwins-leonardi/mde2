package com.contaazul.mde.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;

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

import com.contaazul.mde.business.action.BasicResultParser;
import com.contaazul.mde.business.xml.context.QueryResponseContext;
import com.contaazul.mde.result.QueryResultParser;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt;

public class QueryResultParserTest {
	private static final String RESULT_EXAMPLE_FILENAME = "src/test/resources/com/contaazul/mde/result/nfeDistDFeInteresseResult_example.xml";
	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private BasicResultParser xmlParser;
	@Mock
	private QueryResponseContext context;
	@InjectMocks
	private QueryResultParser parser;
	private Document result;

	@Before
	public void setup() throws Exception {
		initMocks( this );
		result = parseResultFromExampleFile();
		when( context.getContext() ).thenReturn( JAXBContext.newInstance( RetDistDFeInt.class ) );
	}

	private Document parseResultFromExampleFile() throws Exception {
		return createDocumentBuilder().parse( new File( RESULT_EXAMPLE_FILENAME ) );
	}

	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware( true );
		return documentBuilderFactory.newDocumentBuilder();
	}

	@Test
	public void parse() throws Exception {
		RetDistDFeInt parsedResult = parser.parse( result );
		assertThat( parsedResult.getUltNSU(), equalTo( "000000000046886" ) );
		assertThat( parsedResult.getMaxNSU(), equalTo( "000000000051692" ) );
	}

}
