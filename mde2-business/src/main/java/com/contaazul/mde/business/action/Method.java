package com.contaazul.mde.business.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Method {
	QUERY("nfeDistDFeInteresse"),
	EVENT("nfeRecepcaoEvento");

	private final String name;
}
