package com.contaazul.mde.api.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.contaazul.invoiceissuer.api.request.Result;

@Getter
@Setter
@NoArgsConstructor
public class RecipientManifestationBatchResult extends Result {
	private static final int ERROR_CODE = -1;
	private static final int SUCCESS_CODE = 128;
	private List<RecipientManifestationResult> results;
	private String resultXml;

	public RecipientManifestationBatchResult(int code, String reason, Long companyId, Long integrationId) {
		this.reason = reason;
		this.companyId = companyId;
		this.integrationId = integrationId;
	}

	@Override
	public boolean isSuccess() {
		return getCode() == SUCCESS_CODE;
	}

	public static RecipientManifestationBatchResult newEventErrorResult(String message, Long companyId,
			Long integrationId) {
		return new RecipientManifestationBatchResult( ERROR_CODE, message, companyId, integrationId );
	}
}
