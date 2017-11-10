package com.contaazul.mde.business.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.contaazul.invoiceissuer.api.Environment;
import com.contaazul.invoiceissuer.api.UF;
import com.contaazul.invoiceissuer.config.IssuerConstants;
import com.contaazul.mde.business.action.LayoutVersion;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEnvEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento.InfEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento.InfEvento.DetEvento;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

public class EventMarshallerTest {
	private static final String EVENT_ID_MARKER = "ID";
	private static final String EVENT_TYPE = "210200";
	private static final String EVENT_DESCRIPTION = "Confirmacao da Operacao";
	private static final String INVOICE_KEY = "351105741867470001125511100000001319988179000";
	private static final String SEQ = "1";
	private static final String DOCUMENT = "74186747000112";
	private static final String EVENT_DATE_TIME = "2016-01-01T10:00:00-00:02";

	private static final String EXPECTED_XML_FILE = "src/test/resources/com/contaazul/mde/business/xml/envEvento_example.xml";

	@InjectMocks
	private EventMarshaller eventMarshaller;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void asStream() throws Exception {
		TEnvEvento tEnvEvento = buildEventEnvelope();
		InputStream inputStream = eventMarshaller.asStream( tEnvEvento );
		String xml = CharStreams.toString( new InputStreamReader( inputStream ) );
		String expectedXml = Files.toString( new File( EXPECTED_XML_FILE ), Charsets.UTF_8 );
		assertThat( xml, equalTo( expectedXml ) );
	}

	private TEnvEvento buildEventEnvelope() {
		TEnvEvento tEnvEvento = new TEnvEvento();
		tEnvEvento.setIdLote( IssuerConstants.PACKAGE_NUMBER );
		tEnvEvento.setVersao( LayoutVersion.V1_00.getCode() );
		tEnvEvento.getEvento().add( buildEvent() );
		return tEnvEvento;
	}

	private TEvento buildEvent() {
		TEvento tEvento = new TEvento();
		tEvento.setVersao( LayoutVersion.V1_00.getCode() );
		tEvento.setInfEvento( buildEventInfo() );
		return tEvento;
	}

	private InfEvento buildEventInfo() {
		InfEvento infEvento = new InfEvento();
		infEvento.setId( buildEventInfoId() );
		infEvento.setCOrgao( UF.SC.getCode() );
		infEvento.setTpAmb( Environment.HOMOLOGATION.getCode() );
		infEvento.setCNPJ( DOCUMENT );
		infEvento.setChNFe( INVOICE_KEY );
		infEvento.setDhEvento( EVENT_DATE_TIME );
		infEvento.setTpEvento( EVENT_TYPE );
		infEvento.setNSeqEvento( SEQ );
		infEvento.setVerEvento( LayoutVersion.V1_00.getCode() );
		infEvento.setDetEvento( buildDetEvent() );
		return infEvento;
	}

	private String buildEventInfoId() {
		return new StringBuilder( EVENT_ID_MARKER )
				.append( EVENT_TYPE )
				.append( INVOICE_KEY )
				.append( SEQ )
				.toString();
	}

	private DetEvento buildDetEvent() {
		DetEvento detEvento = new DetEvento();
		detEvento.setVersao( LayoutVersion.V1_00.getCode() );
		detEvento.setDescEvento( EVENT_DESCRIPTION );
		return detEvento;
	}

}
