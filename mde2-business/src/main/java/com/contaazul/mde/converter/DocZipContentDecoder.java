package com.contaazul.mde.converter;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

import com.contaazul.mde.converter.exception.DocZipDecodingException;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt.DocZip;
import com.google.common.io.CharStreams;

public class DocZipContentDecoder {
	public String decode(DocZip docZip) {
		String content = printBase64Binary( docZip.getValue() );
		try {
			return CharStreams.toString( readGzipped( decodeBase64( content ) ) );
		} catch (Exception e) {
			throw new DocZipDecodingException( e, content );
		}
	}

	private InputStreamReader readGzipped(InputStream stream) throws IOException {
		return new InputStreamReader( new GZIPInputStream( stream ) );
	}

	private InputStream decodeBase64(String s) {
		return new ByteArrayInputStream( Base64.getDecoder().decode( s ) );
	}
}
