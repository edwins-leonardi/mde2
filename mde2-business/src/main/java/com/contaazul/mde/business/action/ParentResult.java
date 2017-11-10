package com.contaazul.mde.business.action;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.contaazul.invoiceissuer.api.request.Result;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentResult<T extends Result> {
	private InputStream xml;
	private T result;
}
