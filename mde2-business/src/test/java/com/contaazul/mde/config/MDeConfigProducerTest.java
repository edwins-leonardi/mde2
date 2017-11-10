package com.contaazul.mde.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class MDeConfigProducerTest {
	private MDeConfigProducer provider;

	@Before
	public void setup() {
		provider = new MDeConfigProducer();
	}

	@Test
	public void getInstance() {
		MDeConfig queues = provider.getInstance();
		assertThat( queues, is( notNullValue() ) );
	}

}
