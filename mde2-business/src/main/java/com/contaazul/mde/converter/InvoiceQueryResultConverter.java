package com.contaazul.mde.converter;

import java.util.ArrayList;

import javax.inject.Inject;

import com.contaazul.mde.api.request.InvoiceBatch;
import com.contaazul.mde.api.request.InvoiceQueryResult;
import com.contaazul.mde.business.action.ResultConverter;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt;
import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt.LoteDistDFeInt;

public class InvoiceQueryResultConverter implements ResultConverter<RetDistDFeInt, InvoiceQueryResult> {
	@Inject
	private InvoiceBatchConverter batchConverter;

	public InvoiceQueryResult convert(RetDistDFeInt retDistDFeInt) {
		InvoiceQueryResult result = InvoiceQueryResult.builder()
				.lastUniqueSequentialNumber( retDistDFeInt.getUltNSU() )
				.maxAvailableSequentialNumber( retDistDFeInt.getMaxNSU() )
				.invoices( convertInvoices( retDistDFeInt.getLoteDistDFeInt() ) )
				.build();
		result.setCode( Integer.valueOf( retDistDFeInt.getCStat() ) );
		result.setReason( retDistDFeInt.getXMotivo() );
		return result;
	}

	private InvoiceBatch convertInvoices(LoteDistDFeInt loteDistDFeInt) {
		if (loteDistDFeInt == null)
			return buildEmptyBatch();
		return batchConverter.convert( loteDistDFeInt );
	}

	private InvoiceBatch buildEmptyBatch() {
		return InvoiceBatch.builder()
				.processedInvoices( new ArrayList<>() )
				.invoiceSummaries( new ArrayList<>() )
				.build();
	}
}
