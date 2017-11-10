package com.contaazul.invoiceissuer.jobs.configuration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.contaazul.distributedjob.ErrorReport;
import com.contaazul.distributedjob.Job;
import com.contaazul.distributedjob.JobConfiguration;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MDeJobConfiguration implements JobConfiguration {
	@Inject
	private MDeErrorReport reporter;
	private static final int DEFAULT_MAX_EXECUTIONS = 10;

	@Override
	public boolean isJobEnabled(Class<?> job) {
		return true;
	}

	@Override
	public int maxConcurrentExecutions(Job<?> job) {
		return DEFAULT_MAX_EXECUTIONS;
	}

	@Override
	public ErrorReport reporter() {
		return reporter;
	}

	@Override
	public String prefix() {
		return System.getProperty( "redis.jobs.prefix", "mde" );
	}
}
