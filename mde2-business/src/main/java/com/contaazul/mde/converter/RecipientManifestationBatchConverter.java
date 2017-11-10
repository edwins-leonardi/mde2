package com.contaazul.mde.converter;

import javax.inject.Inject;

import com.contaazul.invoiceissuer.config.IssuerConstants;
import com.contaazul.mde.api.request.RecipientManifestation;
import com.contaazul.mde.api.request.RecipientManifestationBatch;
import com.contaazul.mde.business.action.LayoutVersion;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEnvEvento;

public class RecipientManifestationBatchConverter {
	@Inject
	private RecipientManifestationConverter converter;

	public TEnvEvento convert(RecipientManifestationBatch batch) {
		TEnvEvento tEnvEvento = new TEnvEvento();
		tEnvEvento.setIdLote( IssuerConstants.PACKAGE_NUMBER );
		tEnvEvento.setVersao( LayoutVersion.V1_00.getCode() );
		for (RecipientManifestation event : batch.getEvents())
			tEnvEvento.getEvento().add( converter.convert( event ) );
		return tEnvEvento;

	}

}
