package com.contaazul.mde.converter;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.contaazul.invoiceissuer.api.Issuer;
import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.invoiceissuer.converter.ConvertionUtils;
import com.contaazul.mde.api.request.InvoiceSituation;
import com.contaazul.mde.api.request.InvoiceSummary;
import com.contaazul.mde.res_nfe_v1_00.ResNFe;

public class InvoiceSummaryConverter {
	@Inject
	public OperationTypeConverter operationTypeConverter;

	public InvoiceSummary convert(ResNFe invoiceSummary) {
		return InvoiceSummary.builder()
				.invoiceKey( invoiceSummary.getChNFe() )
				.issuer( Issuer.builder()
						.document( buildDocument( invoiceSummary ) )
						.name( invoiceSummary.getXNome() )
						.build() )
				.emission( ConvertionUtils.parseUTC( invoiceSummary.getDhEmi() ) )
				.operationType( operationTypeConverter.convert( invoiceSummary.getTpNF() ) )
				.value( new BigDecimal( invoiceSummary.getVNF() ) )
				.protocolNumber( invoiceSummary.getNProt() )
				.situation( InvoiceSituation.fromCode( invoiceSummary.getCSitNFe() ) )
				.build();
	}

	private Document buildDocument(ResNFe invoiceSummary) {
		return invoiceSummary.getCNPJ() != null ?
				Document.cnpj( invoiceSummary.getCNPJ() ) : Document.cpf( invoiceSummary.getCPF() );
	}

}
