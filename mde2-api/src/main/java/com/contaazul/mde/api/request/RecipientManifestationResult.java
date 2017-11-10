package com.contaazul.mde.api.request;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.contaazul.invoiceissuer.api.request.Result;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipientManifestationResult extends Result {
	private static final List<Integer> SUCCESS_CODES = Arrays.asList( 135 );
	private static final List<Integer> ERROR_CODES = Arrays.asList( 596, 650, 651, 655 );
	private static final Integer DUPLICATE_ERROR_CODE = 573;
	private String invoiceKey;

	@Override
	public boolean isSuccess() {
		return SUCCESS_CODES.contains( getCode() );
	}

	public Boolean isError() {
		return ERROR_CODES.contains( getCode() );
	}

	public boolean isRetriable() {
		return !isSuccess() && !isError();
	}

	public boolean isDuplicate() {
		return getCode() == DUPLICATE_ERROR_CODE;
	}

}
