package com.contaazul.mde.config;

import org.aeonbits.owner.Config;

public interface MDeConfig extends Config {
	@Key("production")
	@DefaultValue("false")
	boolean isProduction();

	@Key("javax.net.ssl.trustStore")
	String trustStorePath();

	@Key("javax.net.ssl.trustStore.pwd")
	String trustStorePassword();

	@Key("javax.net.ssl.keyStore")
	String defaultKeyPath();

	@Key("javax.net.ssl.keyStorePassword")
	String defaultKeyPassword();

}
