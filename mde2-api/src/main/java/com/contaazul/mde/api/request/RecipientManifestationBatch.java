package com.contaazul.mde.api.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.contaazul.invoiceissuer.api.UF;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecipientManifestationBatch {
	private UF uf;
	private List<RecipientManifestation> events;
}
