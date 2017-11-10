package com.contaazul.mde.converter;

import static com.contaazul.mde.api.request.EventType.ACKNOWLEDGE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.contaazul.invoiceissuer.api.Environment;
import com.contaazul.invoiceissuer.api.UF;
import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.mde.api.request.RecipientManifestation;
import com.contaazul.mde.business.action.LayoutVersion;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento.InfEvento;

public class RecipientManifestationConverterTest {
	private static final String NATIONAL_ENVIRONMENT = "91";
	private static final String DOCUMENT = "19671095000101";
	private static final String INVOICE_KEY = "NFe35080599999090910270550010000000015180051271";
	private static final String ID = "ID210210NFe3508059999909091027055001000000001518005127101";
	private static final String DESCRIPTION = "Ciencia da Operacao";
	private static final String REASON = "Agora eu estou justificando";

	@InjectMocks
	private RecipientManifestationConverter converter;
	private RecipientManifestation event;

	@Before
	public void setup() {
		event = buildRecipientManifestation( INVOICE_KEY );
		initMocks( this );
	}

	@Test
	public void convertWithInfEvento() {
		TEvento tEnvEvento = converter.convert( event );
		verifyEventInformation( tEnvEvento.getInfEvento(), INVOICE_KEY, ID );
	}

	@Test
	public void convertWithCPF() {
		event.setDocument( Document.cpf( DOCUMENT ) );
		TEvento tEnvEvento = converter.convert( event );
		assertThat( tEnvEvento.getInfEvento().getCPF(), equalTo( DOCUMENT ) );
	}

	private void verifyEventInformation(InfEvento infEvento, String invoiceKey, String id) {
		assertThat( infEvento.getChNFe(), equalTo( invoiceKey ) );
		assertThat( infEvento.getId(), equalTo( id ) );
		assertThat( infEvento.getCNPJ(), equalTo( DOCUMENT ) );
		assertThat( infEvento.getCOrgao(), equalTo( NATIONAL_ENVIRONMENT ) );
		assertThat( infEvento.getDhEvento(), notNullValue() );
		assertThat( infEvento.getDetEvento().getDescEvento(), equalTo( DESCRIPTION ) );
		assertThat( infEvento.getNSeqEvento(), equalTo( "1" ) );
		assertThat( infEvento.getTpAmb(), equalTo( Environment.HOMOLOGATION.getCode() ) );
		assertThat( infEvento.getTpEvento(), equalTo( "210210" ) );
		assertThat( infEvento.getVerEvento(), equalTo( LayoutVersion.V1_00.getCode() ) );
		assertThat( infEvento.getDetEvento().getVersao(), equalTo( LayoutVersion.V1_00.getCode() ) );
		assertThat( infEvento.getDetEvento().getXJust(), equalTo( REASON ) );
	}

	private RecipientManifestation buildRecipientManifestation(String invoiceKey) {
		return RecipientManifestation.builder()
				.uf( UF.AC )
				.environment( Environment.HOMOLOGATION )
				.document( Document.cnpj( DOCUMENT ) )
				.invoiceKey( invoiceKey )
				.type( ACKNOWLEDGE )
				.reason( REASON )
				.build();
	}

}
