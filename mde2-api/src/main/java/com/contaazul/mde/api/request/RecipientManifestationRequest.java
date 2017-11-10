package com.contaazul.mde.api.request;

import com.contaazul.invoiceissuer.api.Environment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipientManifestationRequest {
	private Long companyId;
	private Long integrationId;
	private String keyFilename;
	private char[] keyPassword;
	private char[] keyToken;
	private Environment environment;
	private RecipientManifestationBatch batch;
}
