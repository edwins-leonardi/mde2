package com.contaazul.mde.business;

import javax.inject.Inject;

import com.contaazul.aws.sqs.SQSService;
import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.api.request.RecipientManifestationRequest;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.nfe.key.KeyProvider;

public class RecipientManifestationService {
	@Inject
	private SQSService sqsService;
	@Inject
	private Interactor interactor;
	@Inject
	private EventXMLBridge eventXMLBridge;

	public ParentResult<RecipientManifestationBatchResult> process(
			RecipientManifestationRequest request,
			KeyProvider keyProvider,
			String queue) {
		ParentResult<RecipientManifestationBatchResult> result = interactor.event( request.getBatch(), keyProvider );
		RecipientManifestationBatchResult innerResult = result.getResult();
		innerResult.setCompanyId( request.getCompanyId() );
		innerResult.setIntegrationId( request.getIntegrationId() );
		eventXMLBridge.save( result );
		sqsService.newMessage( queue, result.getResult() );
		return result;
	}
}
