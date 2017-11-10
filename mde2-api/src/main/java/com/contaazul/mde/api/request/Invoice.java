package com.contaazul.mde.api.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.contaazul.invoiceissuer.api.Issuer;
import com.contaazul.invoiceissuer.api.Recipient;
import com.contaazul.invoiceissuer.api.nfe.OperationType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Invoice {
	private String invoiceKey;
	private Issuer issuer;
	private Recipient recipient;
	private Date emission;
	private OperationType operationType;
	private String serie;
	private Long number;
	private BigDecimal value;
	private String protocolNumber;
	private String filename;
	private String xml;
}
