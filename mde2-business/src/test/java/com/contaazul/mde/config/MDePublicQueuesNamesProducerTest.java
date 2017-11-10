package com.contaazul.mde.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.contaazul.mde.api.config.MDePublicQueuesNames;

public class MDePublicQueuesNamesProducerTest {
	private MDePublicQueuesNamesProducer provider;

	@Before
	public void setup() {
		provider = new MDePublicQueuesNamesProducer();
	}

	@Test
	public void getInstance() {
		MDePublicQueuesNames queues = provider.getInstance();
		assertThat( queues.queueRetrieveRequestName(), equalTo( "mde-retrieve-request" ) );
		assertThat( queues.queueRetrieveResponseName(), equalTo( "mde-retrieve-response" ) );
	}

}
