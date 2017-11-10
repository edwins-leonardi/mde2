package com.contaazul.mde.converter;

import lombok.AllArgsConstructor;

import com.contaazul.mde.api.request.EventType;

@AllArgsConstructor
public class EventIDBuilder {
	private static final String TAG = "ID";
	private static final String SEQUENTIAL_NUMBER = "01";
	private EventType eventType;
	private String invoiceKey;

	public String build() {
		return new StringBuilder( TAG )
				.append( eventType.getCode() )
				.append( invoiceKey )
				.append( SEQUENTIAL_NUMBER )
				.toString();
	}

}
