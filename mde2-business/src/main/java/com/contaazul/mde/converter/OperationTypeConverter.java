package com.contaazul.mde.converter;

import com.contaazul.invoiceissuer.api.nfe.OperationType;

public class OperationTypeConverter {
	public static final String IN = "0";
	public static final String OUT = "1";

	public OperationType convert(String code) {
		if (IN.equals( code ))
			return OperationType.IN;
		return OperationType.OUT;
	}

}
