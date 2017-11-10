package com.contaazul.mde.job.processor;

import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_FAIL;
import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_START;
import static com.contaazul.mde.api.request.RecipientManifestationBatchResult.newEventErrorResult;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.aws.sqs.SQSService;
import com.contaazul.invoiceissuer.entities.ActionLog;
import com.contaazul.invoiceissuer.entities.ActionLogMessage;
import com.contaazul.invoiceissuer.entities.ActionLogType;
import com.contaazul.invoiceissuer.entities.App;
import com.contaazul.invoiceissuer.service.LogService;
import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.api.request.RecipientManifestationRequest;
import com.contaazul.mde.aws.MDeS3Service;
import com.contaazul.mde.business.RecipientManifestationService;
import com.contaazul.mde.business.action.ParentResult;
import com.contaazul.mde.business.keyprovider.ProcessorService;
import com.contaazul.nfe.key.KeyProvider;
import com.dripstat.api.DripStat;

@Log4j
public class RecipientManifestationProcessor {
	@Inject
	private MDeS3Service mdeService;
	@Inject
	private RecipientManifestationService service;
	@Inject
	private ProcessorService processorService;
	@Inject
	private LogService logService;
	@Inject
	private SQSService sqsService;

	public void executeEvent(RecipientManifestationRequest request, String queue) {
		log.info( logService.log( ActionLog.builder()
				.app( App.MDE )
				.invoiceId( request.getIntegrationId() )
				.tenantId( request.getCompanyId() )
				.type( ActionLogType.MDE )
				.message( MDE_START )
				.build() ) );
		try {
			handle( work( request, queue ).getResult() );
		} catch (Exception e) {
			handle( e, request, queue );
		}
	}

	private ParentResult<RecipientManifestationBatchResult> work(RecipientManifestationRequest request, String queue) {
		S3Context s3Context = mdeService.createContext( request.getCompanyId() );
		return service.process( request, getKeyProvider( request, s3Context ), queue );
	}

	private void handle(RecipientManifestationBatchResult result) {
		log.info( logService.log( ActionLog.builder()
				.app( App.MDE )
				.invoiceId( result.getIntegrationId() )
				.tenantId( result.getCompanyId() )
				.text( result.getReason() )
				.type( ActionLogType.MDE )
				.data( result.getResultXml() )
				.message( ActionLogMessage.MDE_SUCCESS )
				.build() ) );
	}

	private void handle(Exception e, RecipientManifestationRequest request, String queue) {
		String cause = new ExceptionCause( e ).getText();
		Long integrationId = request.getIntegrationId();
		Long companyId = request.getCompanyId();
		String message = logService.log( ActionLog.builder()
				.app( App.MDE )
				.invoiceId( integrationId )
				.tenantId( companyId )
				.type( ActionLogType.MDE )
				.message( MDE_FAIL )
				.text( cause )
				.build() );
		DripStat.setException( new Exception( message ) );
		log.error( message, e );
		sqsService.newMessage( queue, newEventErrorResult( message, companyId, integrationId ) );
	}

	private KeyProvider getKeyProvider(RecipientManifestationRequest request, S3Context context) {
		return processorService.buildKeyProvider( request.getKeyFilename(), request.getKeyPassword(),
				request.getKeyToken(), request.getEnvironment(), context );
	}

}
