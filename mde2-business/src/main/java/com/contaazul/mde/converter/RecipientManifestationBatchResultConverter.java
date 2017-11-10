package com.contaazul.mde.converter;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.api.request.RecipientManifestationResult;
import com.contaazul.mde.business.action.ResultConverter;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TRetEnvEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TretEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TretEvento.InfEvento;

public class RecipientManifestationBatchResultConverter implements
		ResultConverter<TRetEnvEvento, RecipientManifestationBatchResult> {

	public RecipientManifestationBatchResult convert(TRetEnvEvento tRetEnvEvento) {
		RecipientManifestationBatchResult result = new RecipientManifestationBatchResult();
		result.setCode( Integer.parseInt( tRetEnvEvento.getCStat() ) );
		result.setReason( tRetEnvEvento.getXMotivo() );
		result.setResults( convert( tRetEnvEvento.getRetEvento() ) );
		return result;
	}

	private List<RecipientManifestationResult> convert(List<TretEvento> retEvento) {
		return retEvento.stream()
				.map( e -> convert( e ) )
				.collect( toList() );
	}

	private RecipientManifestationResult convert(TretEvento tretEvento) {
		InfEvento infEvento = tretEvento.getInfEvento();
		RecipientManifestationResult result = new RecipientManifestationResult();
		result.setCode( Integer.parseInt( infEvento.getCStat() ) );
		result.setReason( infEvento.getXMotivo() );
		result.setInvoiceKey( infEvento.getChNFe() );
		return result;
	}

}
