package com.contaazul.mde.job.processor;

import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_FAIL;
import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_START;
import static com.contaazul.invoiceissuer.entities.ActionLogMessage.MDE_SUCCESS;
import static com.contaazul.invoiceissuer.entities.ActionLogType.MDE;
import static com.contaazul.mde.api.request.InvoiceQueryResult.newErrorResult;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.aws.sqs.SQSService;
import com.contaazul.invoiceissuer.entities.ActionLog;
import com.contaazul.invoiceissuer.entities.ActionLogMessage;
import com.contaazul.invoiceissuer.entities.ActionLogType;
import com.contaazul.invoiceissuer.entities.App;
import com.contaazul.invoiceissuer.service.LogService;
import com.contaazul.mde.api.config.MDePublicQueuesNames;
import com.contaazul.mde.api.request.InvoiceQuery;
import com.contaazul.mde.api.request.InvoiceQueryRequest;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.aws.MDeS3Service;
import com.contaazul.mde.business.InvoiceQueryService;
import com.contaazul.mde.business.keyprovider.ProcessorService;
import com.contaazul.nfe.key.KeyProvider;
import com.google.gson.Gson;

@Log4j
public class InvoiceQueryProcessor {
	@Inject
	private MDePublicQueuesNames queues;
	@Inject
	private SQSService sqsService;
	@Inject
	private MDeS3Service mdeS3Service;
	@Inject
	private InvoiceQueryService service;
	@Inject
	private ProcessorService processorService;
	@Inject
	private LogService logService;

	public void executeQuery(InvoiceQueryRequest invoiceQueryRequest) {
		InvoiceQuery invoiceQuery = invoiceQueryRequest.getInvoiceQuery();
		log.info( logService.log( ActionLog.builder()
				.app( App.MDE )
				.tenantId( invoiceQueryRequest.getCompanyId() )
				.key( invoiceQuery.getLastUniqueSequentialNumber() )
				.message( MDE_START )
				.type( MDE )
				.data( new Gson().toJson( invoiceQuery ) )
				.build() ) );
		try {
			S3Context context = mdeS3Service.createContext( invoiceQueryRequest.getCompanyId() );
			InvoiceQueryResult result = service.process( invoiceQueryRequest,
					getKeyProvider( invoiceQueryRequest, context ) );
			handle( invoiceQueryRequest, invoiceQuery, result );
		} catch (Exception e) {
			handle( e, invoiceQueryRequest );
		}
	}

	private void handle(Exception e, InvoiceQueryRequest invoiceQueryRequest) {
		InvoiceQuery invoiceQuery = invoiceQueryRequest.getInvoiceQuery();
		String message = new ExceptionCause( e ).getText();
		Long companyId = invoiceQueryRequest.getCompanyId();
		log.info( logService.log( ActionLog.builder()
				.app( App.MDE )
				.tenantId( companyId )
				.key( invoiceQuery.getLastUniqueSequentialNumber() )
				.message( MDE_FAIL )
				.type( MDE )
				.text( message )
				.build() ) );
		log.error( "Erro ao enviar consulta", e );
		sqsService.newMessage( queues.queueRetrieveResponseName(), newErrorResult( companyId, message ) );
	}

	private void handle(InvoiceQueryRequest invoiceQueryRequest, InvoiceQuery invoiceQuery, InvoiceQueryResult result) {
		log.info( logService.log( ActionLog.builder()
				.app( App.MDE )
				.tenantId( invoiceQueryRequest.getCompanyId() )
				.key( invoiceQuery.getLastUniqueSequentialNumber() )
				.message( MDE_SUCCESS )
				.type( MDE )
				.text( result.getReason() )
				.data( result.getResultXml() )
				.build() ) );
		result.setResultXml( null );
		sqsService.newMessage( queues.queueRetrieveResponseName(), result );
	}

	private KeyProvider getKeyProvider(InvoiceQueryRequest request,
			S3Context context) {
		return processorService.buildKeyProvider( request.getKeyFilename(), request.getKeyPassword(),
				request.getKeyToken(), request.getEnvironment(), context );
	}

	public void test() {
		ActionLog l = logService.getFirstLog( 669416l, 6180216l, ActionLogType.ISSUE, ActionLogMessage.QUERYING );
		System.out.println( l );

	}

}
