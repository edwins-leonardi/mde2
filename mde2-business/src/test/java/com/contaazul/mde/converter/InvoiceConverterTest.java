package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.invoiceissuer.api.Issuer;
import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.invoiceissuer.api.document.DocumentType;
import com.contaazul.invoiceissuer.api.nfe.OperationType;
import com.contaazul.invoiceissuer.converter.ConvertionUtils;
import com.contaazul.mde.api.request.Invoice;
import com.contaazul.mde.proc_nfe_v3_10.TNFe;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Dest;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Emit;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Ide;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Total;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Total.ICMSTot;
import com.contaazul.mde.proc_nfe_v3_10.TNfeProc;
import com.contaazul.mde.proc_nfe_v3_10.TProtNFe;
import com.contaazul.mde.proc_nfe_v3_10.TProtNFe.InfProt;

public class InvoiceConverterTest {
	private static final String INVOICE_KEY = "NFe35150410807909000124550010000034831413600373";
	private static final String FILENAME = "NFe35150410807909000124550010000034831413600373.xml";
	private static final String ISSUER_LEGAL_PERSON_DOCUMENT = "10808909000124";
	private static final String ISSUER_PHYSICAL_PERSON_DOCUMENT = "98107965442";
	private static final String RECIPIENT_LEGAL_PERSON_DOCUMENT = "21828703000182";
	private static final String RECIPIENT_PHYSICAL_PERSON_DOCUMENT = "38060362682";
	private static final String ISSUER_NAME = "Casa do Smurf Ltda";
	private static final String EMISSION_DATE = "2015-05-15T08:46:00-03:00";
	private static final String OPERATION_TYPE = "1";
	private static final String INVOICE_VALUE = "112.50";
	private static final String SERIE = "3";
	private static final String NUMBER = "30408";
	private static final String PROTOCOL_NUMBER = "9239340239393";

	@Mock(answer = CALLS_REAL_METHODS)
	private OperationTypeConverter operationTypeConverter;

	@InjectMocks
	private InvoiceConverter converter;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		Invoice processedInvoice = converter.convert( buildProcessedInvoice() );
		assertThat( processedInvoice.getProtocolNumber(), equalTo( PROTOCOL_NUMBER ) );
		assertThat( processedInvoice.getInvoiceKey(), equalTo( INVOICE_KEY ) );

		Issuer issuer = processedInvoice.getIssuer();
		assertThat( issuer.getName(), equalTo( ISSUER_NAME ) );

		assertThat( processedInvoice.getEmission(), equalTo( ConvertionUtils.parseUTC( EMISSION_DATE ) ) );
		assertThat( processedInvoice.getOperationType(), equalTo( OperationType.OUT ) );
		assertThat( processedInvoice.getValue(), equalTo( new BigDecimal( INVOICE_VALUE ) ) );
		assertThat( processedInvoice.getFilename(), equalTo( FILENAME ) );
		assertThat( processedInvoice.getSerie(), equalTo( SERIE ) );
		assertThat( processedInvoice.getNumber(), equalTo( Long.valueOf( NUMBER ) ) );
	}

	@Test
	public void convertWithLegalPersonDocument() {
		Invoice processedInvoice = converter.convert( buildProcessedInvoicewithLegalPersonDocument() );

		Document issuerDocument = processedInvoice.getIssuer().getDocument();
		assertThat( issuerDocument.getType(), is( DocumentType.CNPJ ) );
		assertThat( issuerDocument.getValue(), equalTo( ISSUER_LEGAL_PERSON_DOCUMENT ) );

		Document recipientDocument = processedInvoice.getRecipient().getDocument();
		assertThat( recipientDocument.getType(), is( DocumentType.CNPJ ) );
		assertThat( recipientDocument.getValue(), equalTo( RECIPIENT_LEGAL_PERSON_DOCUMENT ) );
	}

	@Test
	public void convertWithPhysicalPersonDocument() {
		Invoice processedInvoice = converter.convert( buildProcessedInvoicewithPhysicalPersonDocument() );

		Document issuerDocument = processedInvoice.getIssuer().getDocument();
		assertThat( issuerDocument.getType(), is( DocumentType.CPF ) );
		assertThat( issuerDocument.getValue(), equalTo( ISSUER_PHYSICAL_PERSON_DOCUMENT ) );

		Document recipientDocument = processedInvoice.getRecipient().getDocument();
		assertThat( recipientDocument.getType(), is( DocumentType.CPF ) );
		assertThat( recipientDocument.getValue(), equalTo( RECIPIENT_PHYSICAL_PERSON_DOCUMENT ) );
	}

	private TNfeProc buildProcessedInvoicewithPhysicalPersonDocument() {
		TNfeProc processedInvoice = buildProcessedInvoice();
		Emit issuer = processedInvoice.getNFe().getInfNFe().getEmit();
		issuer.setCPF( ISSUER_PHYSICAL_PERSON_DOCUMENT );
		Dest recipient = processedInvoice.getNFe().getInfNFe().getDest();
		recipient.setCPF( RECIPIENT_PHYSICAL_PERSON_DOCUMENT );
		return processedInvoice;
	}

	private TNfeProc buildProcessedInvoicewithLegalPersonDocument() {
		TNfeProc processedInvoice = buildProcessedInvoice();
		Emit issuer = processedInvoice.getNFe().getInfNFe().getEmit();
		issuer.setCNPJ( ISSUER_LEGAL_PERSON_DOCUMENT );
		Dest recipient = processedInvoice.getNFe().getInfNFe().getDest();
		recipient.setCNPJ( RECIPIENT_LEGAL_PERSON_DOCUMENT );
		return processedInvoice;
	}

	private TNfeProc buildProcessedInvoice() {
		TNfeProc tNfeProc = new TNfeProc();
		tNfeProc.setProtNFe( buildProtocol() );
		tNfeProc.setNFe( buildInvoice() );
		return tNfeProc;
	}

	private TProtNFe buildProtocol() {
		TProtNFe tProtNFe = new TProtNFe();
		tProtNFe.setInfProt( buildProtocolInfo() );
		return tProtNFe;
	}

	private InfProt buildProtocolInfo() {
		InfProt infProt = new InfProt();
		infProt.setChNFe( INVOICE_KEY );
		infProt.setNProt( PROTOCOL_NUMBER );
		return infProt;
	}

	private TNFe buildInvoice() {
		TNFe tNFe = new TNFe();
		tNFe.setInfNFe( buildInvoiceInfo() );
		return tNFe;
	}

	private InfNFe buildInvoiceInfo() {
		InfNFe infNFe = new InfNFe();
		infNFe.setIde( buildIdentification() );
		infNFe.setEmit( buildIssuer() );
		infNFe.setDest( new Dest() );
		infNFe.setTotal( buildTotal() );
		return infNFe;
	}

	private Ide buildIdentification() {
		Ide ide = new Ide();
		ide.setDhEmi( EMISSION_DATE );
		ide.setTpNF( OPERATION_TYPE );
		ide.setSerie( SERIE );
		ide.setNNF( NUMBER );
		return ide;
	}

	private Emit buildIssuer() {
		Emit emit = new Emit();
		emit.setXNome( ISSUER_NAME );
		return emit;
	}

	private Total buildTotal() {
		Total total = new Total();
		total.setICMSTot( buildICMSTotal() );
		return total;
	}

	private ICMSTot buildICMSTotal() {
		ICMSTot icmsTotal = new ICMSTot();
		icmsTotal.setVNF( INVOICE_VALUE );
		return icmsTotal;
	}

}
