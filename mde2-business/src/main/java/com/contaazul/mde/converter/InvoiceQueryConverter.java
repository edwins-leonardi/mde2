package com.contaazul.mde.converter;

import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.invoiceissuer.api.document.DocumentType;
import com.contaazul.mde.api.request.InvoiceQuery;
import com.contaazul.mde.business.action.LayoutVersion;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt.ConsNSU;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt.DistNSU;

public class InvoiceQueryConverter {

	public DistDFeInt convert(InvoiceQuery invoiceQuery) {
		DistDFeInt distDFeInt = new DistDFeInt();
		setDocument( distDFeInt, invoiceQuery.getDocument() );
		distDFeInt.setCUFAutor( invoiceQuery.getUf().getCode() );
		distDFeInt.setTpAmb( invoiceQuery.getEnvironment().getCode() );
		setSequentialNumber( invoiceQuery, distDFeInt );
		distDFeInt.setVersao( LayoutVersion.V1_00.getCode() );
		return distDFeInt;
	}

	private void setDocument(DistDFeInt distDFeInt, Document document) {
		if (document.getType() == DocumentType.CNPJ)
			distDFeInt.setCNPJ( document.getValue() );
		else
			distDFeInt.setCPF( document.getValue() );
	}

	private void setSequentialNumber(InvoiceQuery invoiceQuery, DistDFeInt distDFeInt) {
		if (invoiceQuery.getLastUniqueSequentialNumber() != null)
			distDFeInt.setDistNSU( createDistNSU( invoiceQuery ) );
		else
			distDFeInt.setConsNSU( createConsNSU( invoiceQuery ) );
	}

	private DistNSU createDistNSU(InvoiceQuery invoiceQuery) {
		DistNSU distNSU = new DistNSU();
		distNSU.setUltNSU( invoiceQuery.getLastUniqueSequentialNumber() );
		return distNSU;
	}

	private ConsNSU createConsNSU(InvoiceQuery invoiceQuery) {
		ConsNSU consNSU = new ConsNSU();
		consNSU.setNSU( invoiceQuery.getUniqueSequentialNumber() );
		return consNSU;
	}

}
