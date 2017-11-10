package com.contaazul.invoiceissuer.jobs.sqs;

import java.util.List;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.contaazul.aws.config.AWSConfig;
import com.contaazul.aws.sqs.AmazonSQSFactory;
import com.contaazul.aws.sqs.MessageProcessorTypeResolver;
import com.contaazul.aws.sqs.SQSService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

@Log4j
@Shutdownable
@NoArgsConstructor
public class ShutdownableSQSService extends SQSService {
	private boolean shutdown = false;

	@VisibleForTesting
	public ShutdownableSQSService(AWSConfig config, AWSCredentialsProvider credentialProvider,
			MessageProcessorTypeResolver typeResolver) {
		super( config, new AmazonSQSFactory( config, credentialProvider ), typeResolver );
	}

	public void shutdown() {
		shutdown = true;
	}

	public boolean isShutingdown() {
		return shutdown;
	}

	@Override
	@VisibleForTesting
	public <E> List<Message> readMessages(int amount, AmazonSQS sqs, String queueUrl) {
		if (isShutingdown()) {
			log.info( "Messages were requested. I returned an empty list because we are going down." );
			return Lists.newArrayList();
		}
		return doReadMessages( amount, sqs, queueUrl );
	}

	@VisibleForTesting
	public List<Message> doReadMessages(int amount, AmazonSQS sqs, String queueUrl) {
		return super.readMessages( amount, sqs, queueUrl );
	}
}
