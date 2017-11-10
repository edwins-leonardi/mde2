package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.invoiceissuer.config.IssuerConstants;
import com.contaazul.mde.api.request.RecipientManifestation;
import com.contaazul.mde.api.request.RecipientManifestationBatch;
import com.contaazul.mde.business.action.LayoutVersion;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEnvEvento;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEvento;

public class RecipientManifestationBatchConverterTest {
	@Mock
	private RecipientManifestationConverter converter;
	@InjectMocks
	private RecipientManifestationBatchConverter batchConverter;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void process() {
		RecipientManifestation anEvent = new RecipientManifestation();
		RecipientManifestation anotherEvent = new RecipientManifestation();
		TEvento anEventConverted = new TEvento();
		TEvento anotherEventConverted = new TEvento();
		when( converter.convert( anEvent ) ).thenReturn( anEventConverted );
		when( converter.convert( anotherEvent ) ).thenReturn( anotherEventConverted );
		TEnvEvento tEnvEvento = batchConverter.convert( buildBatch( anEvent, anotherEvent ) );
		assertThat( tEnvEvento.getIdLote(), equalTo( IssuerConstants.PACKAGE_NUMBER ) );
		assertThat( tEnvEvento.getVersao(), equalTo( LayoutVersion.V1_00.getCode() ) );
		assertThat( tEnvEvento.getEvento(), contains( anEventConverted, anotherEventConverted ) );
	}

	private RecipientManifestationBatch buildBatch(RecipientManifestation... events) {
		return RecipientManifestationBatch.builder()
				.events( Arrays.asList( events ) )
				.build();
	}

}
