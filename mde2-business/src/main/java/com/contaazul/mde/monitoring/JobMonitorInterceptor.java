package com.contaazul.mde.monitoring;

import java.io.Serializable;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import com.contaazul.invoiceissuer.api.monitoring.JobMetadata;
import com.contaazul.invoiceissuer.api.monitoring.JobMonitor;

import io.prometheus.client.Gauge.Child;

@JobMonitor
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class JobMonitorInterceptor implements Serializable {
	private static final long serialVersionUID = 3777194619459480324L;

	@AroundInvoke
	public Object log(InvocationContext context) throws Exception {
		String className = context.getMethod().getDeclaringClass().getSimpleName();
		JobMetadata metadata = context.getMethod().getAnnotation( JobMetadata.class );

		incrementNumberOfJobsRunning( className, metadata );
		Object result = context.proceed();
		decrementNumberOfJobsRunning( className, metadata );

		return result;
	}

	private void incrementNumberOfJobsRunning(String className, JobMetadata metadata) {
		getGaugeWithAppropriateLabels( className, metadata ).inc();
	}

	private void decrementNumberOfJobsRunning(String className, JobMetadata metadata) {
		getGaugeWithAppropriateLabels( className, metadata ).dec();
	}

	private Child getGaugeWithAppropriateLabels(String className, JobMetadata metadata) {
		if (metadata != null)
			return MDEPrometheusMetrics.jobsRunningCount.labels( className, metadata.queueName(),
					metadata.appName() );
		else
			return MDEPrometheusMetrics.jobsRunningCount.labels( className, "", "" );
	}
}
