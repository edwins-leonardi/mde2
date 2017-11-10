package com.contaazul.invoiceissuer.jobs;

import static java.lang.String.format;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import com.contaazul.aws.sqs.SQSMessage;
import com.contaazul.distributedjob.AbstractJob;
import com.contaazul.distributedjob.JobException;
import com.contaazul.invoiceissuer.config.InvoiceIssuerPublicQueueNames;
import com.contaazul.invoiceissuer.jobs.sqs.Shutdownable;
import com.contaazul.invoiceissuer.jobs.sqs.ShutdownableSQSService;

@Log4j
public abstract class AbstractSqsJob<E> extends AbstractJob<SQSMessage<E>> {
	@Inject
	@Shutdownable
	protected ShutdownableSQSService sqs;
	@Inject
	protected InvoiceIssuerPublicQueueNames config;

	@PreDestroy
	public void shutdown() {
		sqs.shutdown();
	}

	@Override
	public boolean shouldExecute() {
		if (sqs.isShutingdown()) {
			log.info( format(
					"Skipping '%s' because host is disabled.",
					getClass().getSimpleName() ) );
			return false;
		}
		return true;
	}

	@Override
	public final void processElement(SQSMessage<E> msg) throws Exception {
		if (!shouldExecute())
			throw new JobException( "Host is disabled, shutting down!" );
		doProcessSqsElement( msg.getData() );
		sqs.delete( queueName(), msg );

	}

	@Override
	public Iterable<SQSMessage<E>> findElementsToProcess() throws Exception {
		return sqs.iterable( queueName(), sqsClass() );
	}

	@Override
	public final String stringfy(SQSMessage<E> msg) {
		return msg.getMessageId();
	}

	@Override
	public void handleLockFailure(SQSMessage<E> msg) {
		sqs.delete( queueName(), msg );
	}

	protected abstract void doProcessSqsElement(E data) throws Exception;

	protected abstract String queueName();

	protected abstract Class<E> sqsClass();
}
