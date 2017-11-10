package com.contaazul.mde.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.contaazul.invoiceissuer.api.Environment;
import com.contaazul.invoiceissuer.api.document.Document;
import com.contaazul.invoiceissuer.api.document.DocumentType;
import com.contaazul.mde.api.request.RecipientManifestation;
import com.contaazul.mde.business.action.LayoutVersion;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento.InfEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento.InfEvento.DetEvento;

public class RecipientManifestationConverter {
	private static final String NATIONAL_ENVIRONMENT = "91";
	private static final String SEQUENTIAL_NUMBER = "1";
	private static final String FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ssXXX";

	public TEvento convert(RecipientManifestation event) {
		TEvento tEvento = new TEvento();
		tEvento.setInfEvento( createInfEvento( event, event.getEnvironment() ) );
		tEvento.setVersao( LayoutVersion.V1_00.getCode() );
		return tEvento;
	}

	private InfEvento createInfEvento(RecipientManifestation recipientManifestation, Environment environment) {
		InfEvento infEvento = new InfEvento();
		infEvento.setChNFe( recipientManifestation.getInvoiceKey() );
		setDocument( recipientManifestation.getDocument(), infEvento );
		infEvento.setCOrgao( NATIONAL_ENVIRONMENT );
		infEvento.setTpEvento( recipientManifestation.getType().getCode() );
		infEvento.setDetEvento( createDetEvento( recipientManifestation ) );
		infEvento.setDhEvento( new SimpleDateFormat( FORMAT_DATE ).format( new Date() ) );
		infEvento.setId( new EventIDBuilder( recipientManifestation.getType(),
				recipientManifestation.getInvoiceKey() ).build() );
		infEvento.setNSeqEvento( SEQUENTIAL_NUMBER );
		infEvento.setTpAmb( environment.getCode() );
		infEvento.setVerEvento( LayoutVersion.V1_00.getCode() );
		return infEvento;
	}

	private void setDocument(Document document, InfEvento infEvento) {
		if (document.getType() == DocumentType.CNPJ)
			infEvento.setCNPJ( document.getValue() );
		else
			infEvento.setCPF( document.getValue() );
	}

	private DetEvento createDetEvento(RecipientManifestation recipientManifestation) {
		DetEvento detEvento = new DetEvento();
		detEvento.setDescEvento( recipientManifestation.getType()
				.getDescription() );
		detEvento.setVersao( LayoutVersion.V1_00.getCode() );
		detEvento.setXJust( recipientManifestation.getReason() );
		return detEvento;
	}

}
