package com.contaazul.mde.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.aeonbits.owner.ConfigFactory;

public class MDeConfigProducer {

	@Produces
	@ApplicationScoped
	public MDeConfig getInstance() {
		return ConfigFactory.create( MDeConfig.class, System.getProperties() );
	}
}
