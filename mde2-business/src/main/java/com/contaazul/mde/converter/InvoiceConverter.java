package com.contaazul.mde.converter;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.contaazul.invoiceissuer.api.Issuer;
import com.contaazul.invoiceissuer.api.Recipient;
import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.invoiceissuer.converter.ConvertionUtils;
import com.contaazul.mde.api.request.Invoice;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Dest;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Emit;
import com.contaazul.mde.proc_nfe_v3_10.TNFe.InfNFe.Ide;
import com.contaazul.mde.proc_nfe_v3_10.TNfeProc;
import com.contaazul.mde.proc_nfe_v3_10.TProtNFe.InfProt;

public class InvoiceConverter {
	private static final String FILENAME_FORMAT = "%s.xml";

	@Inject
	private OperationTypeConverter operationTypeConverter;

	public Invoice convert(TNfeProc processedInvoice) {
		InfProt protocolInfo = processedInvoice.getProtNFe().getInfProt();
		InfNFe invoiceInfo = processedInvoice.getNFe().getInfNFe();
		Ide identification = invoiceInfo.getIde();
		Dest recipient = invoiceInfo.getDest();
		String invoiceKey = protocolInfo.getChNFe();
		return Invoice.builder()
				.invoiceKey( invoiceKey )
				.issuer( buildIssuer( processedInvoice ) )
				.recipient( buildRecipient( recipient ) )
				.emission( ConvertionUtils.parseUTC( identification.getDhEmi() ) )
				.operationType( operationTypeConverter.convert( identification.getTpNF() ) )
				.value( new BigDecimal( invoiceInfo.getTotal().getICMSTot().getVNF() ) )
				.serie( identification.getSerie() )
				.number( Long.valueOf( identification.getNNF() ) )
				.protocolNumber( protocolInfo.getNProt() )
				.filename( buildFilename( invoiceKey ) )
				.build();
	}

	private Recipient buildRecipient(Dest recipient) {
		return Recipient.builder()
				.document( buildDocument( recipient ) )
				.build();
	}

	private Document buildDocument(Dest recipient) {
		if (recipient.getCNPJ() != null)
			return Document.cnpj( recipient.getCNPJ() );
		return Document.cpf( recipient.getCPF() );
	}

	private String buildFilename(String invoiceKey) {
		return String.format( FILENAME_FORMAT, invoiceKey );
	}

	private Issuer buildIssuer(TNfeProc processedInvoice) {
		Emit issuer = processedInvoice.getNFe().getInfNFe().getEmit();
		return Issuer.builder()
				.document( buildDocument( issuer ) )
				.name( issuer.getXNome() )
				.build();
	}

	private Document buildDocument(Emit issuer) {
		if (issuer.getCNPJ() != null)
			return Document.cnpj( issuer.getCNPJ() );
		return Document.cpf( issuer.getCPF() );
	}

}
