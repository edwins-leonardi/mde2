package com.contaazul.mde.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.contaazul.invoiceissuer.api.Environment;
import com.contaazul.invoiceissuer.api.UF;
import com.contaazul.invoiceissuer.api.document.Document;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipientManifestation {
	private UF uf;
	private Environment environment;
	private Document document;
	private String invoiceKey;
	private EventType type;
	private String reason;
}
