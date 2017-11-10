package com.contaazul.invoiceissuer.monitoring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvoiceIssuerMonitoringStatusDTO {
	private String type;
	private String status;
	private String app;
	private Long count;

	public InvoiceIssuerMonitoringStatusDTO(String type, String status, String app, Long count) {
		super();
		this.type = type;
		this.status = status;
		this.app = app;
		this.count = count;
	}
}
