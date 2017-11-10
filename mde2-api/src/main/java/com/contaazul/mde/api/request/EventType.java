package com.contaazul.mde.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

	ACKNOWLEDGE("210210", "Ciencia da Operacao"),
	CONFIRM("210200", "Confirmacao da Operacao"),
	REJECT("210220", "Desconhecimento da Operacao"),
	UNREALISED("210240", "Operacao nao Realizada");

	private String code;
	private String description;
}
