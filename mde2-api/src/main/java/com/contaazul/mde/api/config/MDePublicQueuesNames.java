package com.contaazul.mde.api.config;

import com.contaazul.invoiceissuer.config.InvoiceIssuerPublicQueueNames;

public interface MDePublicQueuesNames extends InvoiceIssuerPublicQueueNames {
	@DefaultValue("mde-retrieve-request")
	@Key("mde.queue.retrieve.request.name")
	String queueRetrieveRequestName();

	@DefaultValue("mde-retrieve-request-retry")
	@Key("mde.queue.retrieve.request.retry.name")
	String queueRetrieveRequestRetryName();

	@DefaultValue("mde-retrieve-response")
	@Key("mde.queue.retrieve.response.name")
	String queueRetrieveResponseName();

	@DefaultValue("mde-event-acknowledge-response")
	@Key("mde.queue.event.acknowledge.response")
	String queueResponseEventAcknowledge();

	@DefaultValue("mde-event-acknowledge-request")
	@Key("mde.queue.event.acknowledge.request")
	String queueRequestEventAcknowledge();

	@DefaultValue("mde-event-confirm-response")
	@Key("mde.queue.event.confirm.response")
	String queueResponseEventConfirm();

	@DefaultValue("mde-event-confirm-request")
	@Key("mde.queue.event.confirm.request")
	String queueRequestEventConfirm();

	@DefaultValue("mde-event-reject-response")
	@Key("mde.queue.retrieve.event.reject.response")
	String queueResponseEventReject();

	@DefaultValue("mde-event-reject-request")
	@Key("mde.queue.retrieve.event.reject.request")
	String queueRequestEventReject();

	@DefaultValue("mde-event-unrealised-response")
	@Key("mde.queue.event.unrealised.response")
	String queueResponseEventUnrealised();

	@DefaultValue("mde-event-unrealised-request")
	@Key("mde.queue.event.unrealised.request")
	String queueRequestEventUnrealised();

}
