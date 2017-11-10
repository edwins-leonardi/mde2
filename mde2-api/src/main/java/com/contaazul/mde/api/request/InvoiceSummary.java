package com.contaazul.mde.api.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.contaazul.invoiceissuer.api.Issuer;
import com.contaazul.invoiceissuer.api.nfe.OperationType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InvoiceSummary {
	private String invoiceKey;
	private Issuer issuer;
	private Date emission;
	private OperationType operationType;
	private BigDecimal value;
	private String protocolNumber;
	private InvoiceSituation situation;
}
