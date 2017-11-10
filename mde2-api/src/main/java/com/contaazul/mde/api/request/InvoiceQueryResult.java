package com.contaazul.mde.api.request;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.contaazul.invoiceissuer.api.request.Result;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceQueryResult extends Result {
	private static final List<Integer> SUCCESS_CODES = Arrays.asList( 138, 137, 656 );

	private String lastUniqueSequentialNumber;
	private String maxAvailableSequentialNumber;
	private InvoiceBatch invoices;
	private String resultXml;

	@Override
	public boolean isSuccess() {
		return SUCCESS_CODES.contains( getCode() );
	}

	public InvoiceQueryResult(int code, Long companyId, String reason) {
		this.code = code;
		this.companyId = companyId;
		this.reason = reason;
	}

	public static InvoiceQueryResult newErrorResult(Long companyId, String reason) {
		return new InvoiceQueryResult( -1, companyId, reason );
	}

}
