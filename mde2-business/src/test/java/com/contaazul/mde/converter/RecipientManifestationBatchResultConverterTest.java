package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.contaazul.mde.api.request.RecipientManifestationBatchResult;
import com.contaazul.mde.api.request.RecipientManifestationResult;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TRetEnvEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TretEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TretEvento.InfEvento;

public class RecipientManifestationBatchResultConverterTest {
	private static final String BATCH_SUCCESS_CODE = "128";
	private static final String EVENT_SUCCESS_CODE = "135";
	public static final String REASON = "Processado";
	public static final String INVOICE_KEY = "NFe35150410807909000124550010000034831413600373";

	@InjectMocks
	private RecipientManifestationBatchResultConverter converter;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		RecipientManifestationBatchResult batchResult = converter.convert( buildBatchResult() );
		assertThat( batchResult.isSuccess(), equalTo( true ) );
		assertThat( batchResult.getReason(), equalTo( REASON ) );
		RecipientManifestationResult result = batchResult.getResults().get( 0 );
		assertThat( result.isSuccess(), equalTo( true ) );
		assertThat( result.getReason(), equalTo( REASON ) );
		assertThat( result.getInvoiceKey(), equalTo( INVOICE_KEY ) );
	}

	private TRetEnvEvento buildBatchResult() {
		TRetEnvEvento tRetEnvEvento = new TRetEnvEvento();
		tRetEnvEvento.setCStat( BATCH_SUCCESS_CODE );
		tRetEnvEvento.setXMotivo( REASON );
		tRetEnvEvento.getRetEvento().add( buildEventResult() );
		return tRetEnvEvento;
	}

	private TretEvento buildEventResult() {
		TretEvento tretEvento = new TretEvento();
		tretEvento.setInfEvento( buildEventInfo() );
		return tretEvento;
	}

	private InfEvento buildEventInfo() {
		InfEvento eventInfo = new InfEvento();
		eventInfo.setChNFe( INVOICE_KEY );
		eventInfo.setCStat( EVENT_SUCCESS_CODE );
		eventInfo.setXMotivo( REASON );
		return eventInfo;
	}

}
