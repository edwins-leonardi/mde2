package com.contaazul.mde.job;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.contaazul.distributedjob.JobExecutor;

@Stateless
public class ConfirmInvoiceProcessorJobSchedule {
	@Inject
	private JobExecutor jobExecutor;

	@Schedule(hour = "*", minute = "*", second = "*", persistent = false)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void schedule() {
		jobExecutor.execute( ConfirmInvoiceProcessorJob.class );
	}

}
