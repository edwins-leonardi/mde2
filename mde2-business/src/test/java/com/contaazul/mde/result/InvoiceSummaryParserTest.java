package com.contaazul.mde.result;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;

import javax.xml.bind.JAXBContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.mde.business.action.BasicResultParser;
import com.contaazul.mde.business.xml.context.InvoiceSummaryContext;
import com.contaazul.mde.res_nfe_v1_00.ResNFe;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class InvoiceSummaryParserTest {
	public static final String RESNFE_EXAMPLE_FILE = "src/test/resources/com/contaazul/mde/result/resNfe_example.xml";
	public static final String INVOICE_KEY = "NFe35150410807909000124550010000034831413600373";
	public static final String DOCUMENT = "10808909000124";
	public static final String ISSUER_NAME = "Casa do Smurf Ltda";
	public static final String SERIE = "1";
	public static final String INVOICE_NUMBER = "3034";
	public static final String EMISSION_DATE = "2015-05-15T08:46:00-03:00";
	public static final String OPERATION_TYPE = "1";
	public static final String INVOICE_VALUE = "112.50";
	public static final String PROTOCOL_NUMBER = "9239340239393";
	public static final String SITUATION = "1";

	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private BasicResultParser basicResultParser;
	@Mock
	private InvoiceSummaryContext context;

	@InjectMocks
	private InvoiceSummaryParser parser;

	@Before
	public void setup() throws Exception {
		initMocks( this );
		when( context.getContext() ).thenReturn( JAXBContext.newInstance( ResNFe.class ) );
	}

	@Test
	public void parse() throws Exception {
		ResNFe resNFe = parser.parse( Files.toString( new File( RESNFE_EXAMPLE_FILE ), Charsets.UTF_8 ) );
		assertThat( resNFe.getChNFe(), equalTo( INVOICE_KEY ) );
		assertThat( resNFe.getCNPJ(), equalTo( DOCUMENT ) );
		assertThat( resNFe.getXNome(), equalTo( ISSUER_NAME ) );
		assertThat( resNFe.getDhEmi(), equalTo( EMISSION_DATE ) );
		assertThat( resNFe.getTpNF(), equalTo( OPERATION_TYPE ) );
		assertThat( resNFe.getVNF(), equalTo( INVOICE_VALUE ) );
		assertThat( resNFe.getNProt(), equalTo( PROTOCOL_NUMBER ) );
		assertThat( resNFe.getCSitNFe(), equalTo( SITUATION ) );
	}

}
