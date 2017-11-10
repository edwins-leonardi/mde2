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
import com.contaazul.mde.business.xml.context.InvoiceContext;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Emit;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Ide;
import com.contaazul.mde.proc_nfe_v3_10.TNfeProc;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class InvoiceParserTest {
	public static final String NFE_PROC_EXAMPLE_FILE = "src/test/resources/com/contaazul/mde/result/nfeProc_example.xml";
	public static final String INVOICE_KEY = "35140208674015000147550010000030341470359450";
	public static final String DOCUMENT = "10808909000124";
	public static final String ISSUER_NAME = "Casa Do Smurf Ltda";
	public static final String SERIE = "1";
	public static final String INVOICE_NUMBER = "3034";
	public static final String EMISSION_DATE = "2014-02-14T09:06:12-02:00";
	public static final String OPERATION_TYPE = "1";
	public static final String INVOICE_VALUE = "1.00";

	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private BasicResultParser basicResultParser;
	@Mock
	private InvoiceContext context;

	@InjectMocks
	private InvoiceParser parser;

	@Before
	public void setup() throws Exception {
		initMocks( this );
		when( context.getContext() ).thenReturn( JAXBContext.newInstance( TNfeProc.class ) );
	}

	@Test
	public void parse() throws Exception {
		TNfeProc tNfeProc = parser.parse( Files.toString( new File( NFE_PROC_EXAMPLE_FILE ), Charsets.UTF_8 ) );

		assertThat( tNfeProc.getProtNFe().getInfProt().getChNFe(), equalTo( INVOICE_KEY ) );
		InfNFe infNFe = tNfeProc.getNFe().getInfNFe();
		Ide ide = infNFe.getIde();
		assertThat( ide.getTpNF(), equalTo( OPERATION_TYPE ) );
		assertThat( ide.getDhEmi(), equalTo( EMISSION_DATE ) );
		assertThat( ide.getSerie(), equalTo( SERIE ) );
		assertThat( ide.getNNF(), equalTo( INVOICE_NUMBER ) );
		Emit emit = infNFe.getEmit();
		assertThat( emit.getCNPJ(), equalTo( DOCUMENT ) );
		assertThat( emit.getXNome(), equalTo( ISSUER_NAME ) );
		assertThat( infNFe.getTotal().getICMSTot().getVNF(), equalTo( INVOICE_VALUE ) );
	}
}
