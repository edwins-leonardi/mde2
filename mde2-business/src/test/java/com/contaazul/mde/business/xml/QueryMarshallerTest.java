package com.contaazul.mde.business.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt.DistNSU;
import com.google.common.io.CharStreams;

public class QueryMarshallerTest {
	private static final String NSU = "00000000045654";
	private static final String VERSION = "1.00";
	private static final String HOMOLOG = "2";
	private static final String UF = "42";
	private static final String DOCUMENT = "52835105000164";

	@InjectMocks
	private QueryMarshaller queryMarshaller;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void asStream() throws Exception {
		DistDFeInt distDFeInt = buildDistDFeInt();
		InputStream inputStream = queryMarshaller.asStream( distDFeInt );
		String xml = CharStreams.toString( new InputStreamReader( inputStream ) );
		assertThat( xml, equalTo( expectedXml() ) );
	}

	private String expectedXml() {
		return new StringBuilder()
				.append( "<distDFeInt versao=\"1.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">" )
				.append( "<tpAmb>2</tpAmb>" )
				.append( "<cUFAutor>42</cUFAutor>" )
				.append( "<CNPJ>52835105000164</CNPJ>" )
				.append( "<distNSU><ultNSU>00000000045654</ultNSU></distNSU>" )
				.append( "</distDFeInt>" )
				.toString();
	}

	private DistDFeInt buildDistDFeInt() {
		DistDFeInt distDFeInt = new DistDFeInt();
		distDFeInt.setTpAmb( HOMOLOG );
		distDFeInt.setVersao( VERSION );
		distDFeInt.setCUFAutor( UF );
		distDFeInt.setCNPJ( DOCUMENT );
		distDFeInt.setDistNSU( buildDistNSU() );
		return distDFeInt;
	}

	private DistNSU buildDistNSU() {
		DistNSU distNSU = new DistNSU();
		distNSU.setUltNSU( NSU );
		return distNSU;
	}

}
