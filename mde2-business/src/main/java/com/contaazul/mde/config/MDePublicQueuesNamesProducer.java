package com.contaazul.mde.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.aeonbits.owner.ConfigFactory;

import com.contaazul.mde.api.config.MDePublicQueuesNames;

public class MDePublicQueuesNamesProducer {

	@Produces
	@ApplicationScoped
	public MDePublicQueuesNames getInstance() {
		return ConfigFactory.create( MDePublicQueuesNames.class, System.getProperties() );
	}
}
