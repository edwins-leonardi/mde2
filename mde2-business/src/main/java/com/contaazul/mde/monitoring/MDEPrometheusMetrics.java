package com.contaazul.mde.monitoring;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class MDEPrometheusMetrics {
	public static final CollectorRegistry registry = new CollectorRegistry( true );

	public static final Gauge jobsRunningCount = Gauge.build()
			.name( "mde_jobs_running_count" )
			.help( "Number of mde jobs running" )
			.labelNames( "job", "queue", "app" )
			.register( registry );
}
