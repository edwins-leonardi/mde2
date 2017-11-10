package com.contaazul.mde.job;

import javax.inject.Inject;

import com.contaazul.invoiceissuer.api.monitoring.JobMetadata;
import com.contaazul.invoiceissuer.api.monitoring.JobMonitor;
import com.contaazul.invoiceissuer.jobs.AbstractSqsJob;
import com.contaazul.mde.api.config.MDePublicQueuesNames;
import com.contaazul.mde.api.request.RecipientManifestationRequest;
import com.contaazul.mde.job.processor.RecipientManifestationProcessor;

public class ConfirmInvoiceProcessorJob extends AbstractSqsJob<RecipientManifestationRequest> {

	@Inject
	private RecipientManifestationProcessor processor;
	@Inject
	private MDePublicQueuesNames queues;

	@Override
	@JobMonitor
	@JobMetadata(queueName = "mde-event-confirm-request", appName = "mde")
	protected void doProcessSqsElement(RecipientManifestationRequest recipientManifestationRequest) {
		processor.executeEvent( recipientManifestationRequest, queues.queueResponseEventConfirm() );
	}

	@Override
	protected String queueName() {
		return queues.queueRequestEventConfirm();
	}

	@Override
	protected Class<RecipientManifestationRequest> sqsClass() {
		return RecipientManifestationRequest.class;
	}
}
