package com.contaazul.invoiceissuer.jobs;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisProducer {
@Produces
@ApplicationScoped
public RedissonClient create() {
	final Config config = new Config();
		config.useMasterSlaveServers().setMasterAddress( System.getProperty( "redis.url", "127.0.0.1:6379" ) );
		return Redisson.create( config );
	}
}
