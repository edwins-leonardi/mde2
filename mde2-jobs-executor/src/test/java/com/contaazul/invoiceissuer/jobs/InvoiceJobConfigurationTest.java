package com.contaazul.invoiceissuer.jobs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.contaazul.distributedjob.Job;
import com.contaazul.invoiceissuer.jobs.configuration.MDeJobConfiguration;

public class InvoiceJobConfigurationTest {
	@InjectMocks
	private MDeJobConfiguration jobs;

	@Before
	public void init() {
		initMocks( this );
	}

	@Test
	public void checksIfJobIsEnabled() throws Exception {
		assertThat( jobs.isJobEnabled( AbstractSqsJob.class ), is( true ) );
	}

	@Test
	public void limitsConcurrenctExecutions() throws Exception {
		assertThat( jobs.maxConcurrentExecutions( mock( Job.class ) ), equalTo( 5 ) );
	}
}
