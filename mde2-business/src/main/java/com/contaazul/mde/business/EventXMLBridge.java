package com.contaazul.mde.business;

import static com.google.common.base.Charsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.aws.MDeS3Service;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.mde.purchase.aws.exception.S3InvoiceSavingException;

public class EventXMLBridge {
	private static final String REQUEST_FILENAME = "%d_eventRequest.xml";
	private static final String RESPONSE_FILE_NAME = "%d_eventResponse.xml";
	@Inject
	private MDeS3Service s3Service;

	public void save(ParentResult<RecipientManifestationBatchResult> result) {
		RecipientManifestationBatchResult eventResult = result.getResult();
		Long companyId = eventResult.getCompanyId();
		Long integrationId = eventResult.getIntegrationId();
		saveRequest( companyId, integrationId, result.getXml() );
		InputStream responseXml = new ByteArrayInputStream( result.getResult().getResultXml().getBytes( UTF_8 ) );
		saveResponse( companyId, integrationId, responseXml );
	}

	private void saveRequest(Long companyId, Long integrationId, InputStream inputStream) {
		save( companyId, String.format( REQUEST_FILENAME, integrationId ), inputStream );
	}

	private void saveResponse(Long companyId, Long integrationId, InputStream inputStream) {
		save( companyId, String.format( RESPONSE_FILE_NAME, integrationId ), inputStream );
	}

	private void save(Long tenantId, String filename, InputStream inputStream) {
		try {
			s3Service.createContext( tenantId ).save( filename, inputStream );
		} catch (IOException e) {
			throw new S3InvoiceSavingException( e, filename );
		}
	}
}
