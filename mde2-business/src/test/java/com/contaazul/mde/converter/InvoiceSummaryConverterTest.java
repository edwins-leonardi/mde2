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

import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.invoiceissuer.api.document.DocumentType;
import com.contaazul.invoiceissuer.api.nfe.OperationType;
import com.contaazul.invoiceissuer.converter.ConvertionUtils;
import com.contaazul.mde.api.request.InvoiceSituation;
import com.contaazul.mde.api.request.InvoiceSummary;
import com.contaazul.mde.res_nfe_v1_00.ResNFe;

public class InvoiceSummaryConverterTest {
	public static final String INVOICE_KEY = "NFe35150410807909000124550010000034831413600373";
	public static final String ISSUER_LEGAL_PERSON_DOCUMENT = "10808909000124";
	public static final String ISSUER_PHYSICAL_PERSON_DOCUMENT = "98107965442";
	public static final String ISSUER_NAME = "Casa do Smurf Ltda";
	public static final String EMISSION_DATE = "2015-05-15T08:46:00-03:00";
	public static final String OPERATION_TYPE = "1";
	public static final String INVOICE_VALUE = "112.50";
	public static final String PROTOCOL_NUMBER = "9239340239393";
	public static final String SITUATION = "1";

	@Mock(answer = CALLS_REAL_METHODS)
	private OperationTypeConverter operationTypeConverter;

	@InjectMocks
	private InvoiceSummaryConverter converter;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		InvoiceSummary invoiceSummary = converter.convert( buildInvoiceSummary() );
		assertThat( invoiceSummary.getInvoiceKey(), equalTo( INVOICE_KEY ) );
		assertThat( invoiceSummary.getIssuer().getName(), equalTo( ISSUER_NAME ) );
		assertThat( invoiceSummary.getEmission(), equalTo( ConvertionUtils.parseUTC( EMISSION_DATE ) ) );
		assertThat( invoiceSummary.getOperationType(), equalTo( OperationType.OUT ) );
		assertThat( invoiceSummary.getValue(), equalTo( new BigDecimal( INVOICE_VALUE ) ) );
		assertThat( invoiceSummary.getProtocolNumber(), equalTo( PROTOCOL_NUMBER ) );
		assertThat( invoiceSummary.getSituation(), equalTo( InvoiceSituation.AUTHORIZED ) );
	}

	@Test
	public void convertWithLegalPersonDocument() {
		InvoiceSummary invoiceSummary = converter.convert( buildInvoiceSummaryWithLegalPersonDocument() );
		Document document = invoiceSummary.getIssuer().getDocument();
		assertThat( document.getType(), is( DocumentType.CNPJ ) );
		assertThat( document.getValue(), equalTo( ISSUER_LEGAL_PERSON_DOCUMENT ) );
	}

	@Test
	public void convertWithPhysicalPersonDocument() {
		InvoiceSummary invoiceSummary = converter.convert( buildInvoiceSummaryWithPhysicalPersonDocument() );
		Document document = invoiceSummary.getIssuer().getDocument();
		assertThat( document.getType(), is( DocumentType.CPF ) );
		assertThat( document.getValue(), equalTo( ISSUER_PHYSICAL_PERSON_DOCUMENT ) );
	}

	private ResNFe buildInvoiceSummaryWithLegalPersonDocument() {
		ResNFe invoiceSummary = buildInvoiceSummary();
		invoiceSummary.setCNPJ( ISSUER_LEGAL_PERSON_DOCUMENT );
		return invoiceSummary;
	}

	private ResNFe buildInvoiceSummaryWithPhysicalPersonDocument() {
		ResNFe invoiceSummary = buildInvoiceSummary();
		invoiceSummary.setCPF( ISSUER_PHYSICAL_PERSON_DOCUMENT );
		return invoiceSummary;
	}

	private ResNFe buildInvoiceSummary() {
		ResNFe resNfe = new ResNFe();
		resNfe.setChNFe( INVOICE_KEY );
		resNfe.setXNome( ISSUER_NAME );
		resNfe.setDhEmi( EMISSION_DATE );
		resNfe.setTpNF( OPERATION_TYPE );
		resNfe.setVNF( INVOICE_VALUE );
		resNfe.setNProt( PROTOCOL_NUMBER );
		resNfe.setCSitNFe( SITUATION );
		return resNfe;
	}

}
