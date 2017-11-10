package com.contaazul.mde.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvoiceSituation {
	AUTHORIZED("1"), DENIED("2");

	private String code;

	public static InvoiceSituation fromCode(String code) {
		for (InvoiceSituation value : values())
			if (value.getCode().equals( code ))
				return value;
		throw new IllegalArgumentException( code );
	}
}
