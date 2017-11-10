package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;

import com.contaazul.mde.converter.exception.DocZipDecodingException;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class DocZipContentDecoderTest {
	private static final String DOCZIP_BASE64_GZIP = "src/test/resources/com/contaazul/mde/converter/doczip_base64_gzip.txt";
	private static final String DOCZIP_XML = "src/test/resources/com/contaazul/mde/converter/doczip.xml";
	private DocZipContentDecoder decoder;

	@Before
	public void setup() {
		decoder = new DocZipContentDecoder();
	}

	@Test
	public void decode() throws Exception {
		DocZip docZip = buildDocZip();
		String xml = read( DOCZIP_XML );
		assertThat( decoder.decode( docZip ), equalTo( xml ) );
	}

	private DocZip buildDocZip() throws Exception {
		DocZip docZip = new DocZip();
		docZip.setValue( DatatypeConverter.parseBase64Binary( (read( DOCZIP_BASE64_GZIP )) ) );
		return docZip;
	}

	private String read(String pathname) throws Exception {
		return Files.toString( new File( pathname ), Charsets.UTF_8 );
	}

	@Test(expected = DocZipDecodingException.class)
	public void decodeInvalidStream() throws Exception {
		DocZip docZip = new DocZip();
		docZip.setValue( "not valid".getBytes() );
		decoder.decode( docZip );
	}
}
