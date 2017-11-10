package com.contaazul.mde.purchase.aws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.danfe.DanfeEmitter;
import com.contaazul.danfe.DanfeEmitterException;
import com.contaazul.mde.business.action.ActionException;
import com.contaazul.mde.converter.DanfeGenerationException;
import com.contaazul.mde.purchase.aws.exception.S3InvoiceSavingException;
import com.google.common.base.Charsets;

public class PurchaseImportXMLBridge {
	private static final String PDF_EXTENSION = ".pdf";
	private static final String XML_EXTENSION = ".xml";
	@Inject
	private PurchaseImportS3Service s3Service;
	@Inject
	private DanfeEmitter danfeEmitter;

	public void save(Long tenantId, String filename, String xml) throws ActionException {
		S3Context context = s3Service.createContext( tenantId );
		save( context, filename + XML_EXTENSION, createStream( xml ) );
		save( context, filename + PDF_EXTENSION, generateDANFE( xml, filename ) );
	}

	private InputStream generateDANFE(String xml, String filename) {
		try {
			return danfeEmitter.emitDanfe( createStream( xml ) );
		} catch (DanfeEmitterException e) {
			throw new DanfeGenerationException( e, filename );
		}
	}

	private void save(S3Context context, String filename, InputStream stream) {
		try {
			context.save( filename, stream );
		} catch (IOException e) {
			throw new S3InvoiceSavingException( e, filename );
		}
	}

	private InputStream createStream(String xml) {
		return new ByteArrayInputStream( xml.getBytes( Charsets.UTF_8 ) );
	}
}
