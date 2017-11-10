package com.contaazul.mde.business.keyprovider;

import javax.inject.Inject;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.invoiceissuer.api.Environment;
import com.contaazul.nfe.key.KeyProvider;
import com.contaazul.nfe.key.PrivateKeyData;
import com.contaazul.nfe.key.SimpleKeyProvider;
import com.contaazul.security.cryptography.InvoiceCryptographyService;
import com.google.common.annotations.VisibleForTesting;

import lombok.extern.log4j.Log4j;

@Log4j
public class ProcessorService {

	@Inject
	private InvoiceCryptographyService invoiceCryptographyService;

	public KeyProvider buildKeyProvider(String keyFilename, char[] keyPassword, char[] token, Environment environment,
			S3Context context) {
		PrivateKeyData data = PrivateKeyData.builder()
				.password( retrievePassword( keyPassword, token, environment ) )
				.key( context.retrieve( keyFilename ) )
				.build();
		return buildKeyProvider( data );
	}

	@VisibleForTesting
	protected SimpleKeyProvider buildKeyProvider(PrivateKeyData data) {
		return new SimpleKeyProvider( data );
	}

	private char[] retrievePassword(char[] keyPassword, char[] token, Environment environment) {
		char[] password = invoiceCryptographyService.decrypt( token, environment );
		if (password != null && password.length > 0) {
			log.info( "Using decrypted password" );
			return password;
		} else
			log.info( "Using normal password" );
		return keyPassword;
	}

}
