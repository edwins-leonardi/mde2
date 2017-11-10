package com.contaazul.mde.business.action;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.inject.Inject;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import com.contaazul.mde.config.MDeConfig;
import com.contaazul.nfe.key.KeyProvider;
import com.contaazul.nfe.socket.DynamicSocketFactory;
import com.contaazul.nfe.socket.TrustManagersFactory;

public class Authenticator {
	@Inject
	private MDeConfig config;
	private static final int TIMEOUT = 30 * 1000;

	public void setup(ServiceClient client, KeyProvider keyProvider) {
		ProtocolSocketFactory socketFactory = createSocketFactory( keyProvider );
		Protocol protocol = new Protocol( "https", socketFactory, 443 );
		client.getOptions().setProperty( HTTPConstants.SO_TIMEOUT, TIMEOUT );
		client.getOptions().setProperty( HTTPConstants.CUSTOM_PROTOCOL_HANDLER, protocol );
	}

	private ProtocolSocketFactory createSocketFactory(KeyProvider keyProvider) {
		PrivateKey key = keyProvider.loadPrivateKey();
		X509Certificate cert = keyProvider.loadCertificate();
		TrustManagersFactory trustmanagerFactory = createTrustManagerFactory();
		return new DynamicSocketFactory( cert, key, trustmanagerFactory );
	}

	private TrustManagersFactory createTrustManagerFactory() {
		return new TrustManagersFactory( config.trustStorePath(), config.trustStorePassword() );
	}
}
