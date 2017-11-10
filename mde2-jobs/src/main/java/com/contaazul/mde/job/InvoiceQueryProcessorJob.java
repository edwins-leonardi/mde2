package com.contaazul.mde.job;

import javax.inject.Inject;

import com.contaazul.invoiceissuer.api.monitoring.JobMetadata;
import com.contaazul.invoiceissuer.api.monitoring.JobMonitor;
import com.contaazul.invoiceissuer.jobs.AbstractSqsJob;
import com.contaazul.mde.api.config.MDePublicQueuesNames;
import com.contaazul.mde.api.request.InvoiceQueryRequest;
import com.contaazul.mde.job.processor.InvoiceQueryProcessor;

public class InvoiceQueryProcessorJob extends AbstractSqsJob<InvoiceQueryRequest> {

	@Inject
	private InvoiceQueryProcessor processor;

	@Inject
	private MDePublicQueuesNames queues;

	@Override
	@JobMonitor
	@JobMetadata(queueName = "mde-retrieve-request", appName = "mde")
	protected void doProcessSqsElement(InvoiceQueryRequest invoiceQueryRequest) throws Exception {
		processor.executeQuery( invoiceQueryRequest );
	}

	@Override
	protected String queueName() {
		return queues.queueRetrieveRequestName();
	}

	@Override
	protected Class<InvoiceQueryRequest> sqsClass() {
		return InvoiceQueryRequest.class;
	}

}
