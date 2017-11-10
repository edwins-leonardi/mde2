package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.contaazul.invoiceissuer.api.Environment;
import com.contaazul.invoiceissuer.api.UF;
import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.mde.api.request.InvoiceQuery;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt;

public class InvoiceQueryConverterTest {
	private static final String DOCUMENT = "19671095000101";
	private static final String PHYSICAL_PERSON_DOCUMENT = "06623046943";
	private static final String LAST_UNIQUE_SEQUENTIAL_NUMBER = "000000000046886";
	private static final String UNIQUE_SEQUENTIAL_NUMBER = "000000000046889";

	@InjectMocks
	private InvoiceQueryConverter converter;
	private InvoiceQuery invoiceQuery;

	@Before
	public void setup() {
		invoiceQuery = buildInvoiceQuery();
		initMocks( this );
	}

	@Test
	public void convert() {
		DistDFeInt distDFeInt = converter.convert( invoiceQuery );
		assertThat( distDFeInt.getCNPJ(), equalTo( DOCUMENT ) );
		assertThat( distDFeInt.getCUFAutor(), equalTo( UF.SC.getCode() ) );
		assertThat( distDFeInt.getVersao(), equalTo( "1.00" ) );
	}

	@Test
	public void convertWithPhysicalPersonDocument() {
		invoiceQuery.setDocument( Document.cpf( PHYSICAL_PERSON_DOCUMENT ) );
		DistDFeInt distDFeInt = converter.convert( invoiceQuery );
		assertThat( distDFeInt.getCPF(), equalTo( PHYSICAL_PERSON_DOCUMENT ) );
	}

	@Test
	public void convertWithLastUniqueSequentialNumber() {
		invoiceQuery.setLastUniqueSequentialNumber( LAST_UNIQUE_SEQUENTIAL_NUMBER );
		DistDFeInt distDFeInt = converter.convert( invoiceQuery );
		assertThat( distDFeInt.getDistNSU().getUltNSU(), equalTo( LAST_UNIQUE_SEQUENTIAL_NUMBER ) );
	}

	@Test
	public void convertWithSpecificUniqueSequentialNumber() {
		invoiceQuery.setUniqueSequentialNumber( UNIQUE_SEQUENTIAL_NUMBER );
		DistDFeInt distDFeInt = converter.convert( invoiceQuery );
		assertThat( distDFeInt.getConsNSU().getNSU(), equalTo( UNIQUE_SEQUENTIAL_NUMBER ) );
	}

	@Test
	public void convertWithHomologEnvironment() {
		invoiceQuery.setEnvironment( Environment.HOMOLOGATION );
		DistDFeInt distDFeInt = converter.convert( invoiceQuery );
		assertThat( distDFeInt.getTpAmb(), equalTo( "2" ) );
	}

	@Test
	public void convertWithProductionEnvironment() {
		invoiceQuery.setEnvironment( Environment.PRODUCTION );
		DistDFeInt distDFeInt = converter.convert( invoiceQuery );
		assertThat( distDFeInt.getTpAmb(), equalTo( "1" ) );
	}

	private InvoiceQuery buildInvoiceQuery() {
		return InvoiceQuery.builder()
				.document( Document.cnpj( DOCUMENT ) )
				.uf( UF.SC )
				.environment( Environment.PRODUCTION )
				.build();
	}

}
