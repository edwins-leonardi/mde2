package com.contaazul.mde.api.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InvoiceBatch {
	List<InvoiceSummary> invoiceSummaries;
	List<Invoice> processedInvoices;
}
