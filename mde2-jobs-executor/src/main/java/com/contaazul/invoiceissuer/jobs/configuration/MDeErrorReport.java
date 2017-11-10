package com.contaazul.invoiceissuer.jobs.configuration;

import com.contaazul.distributedjob.ErrorReport;
import com.dripstat.api.DripStat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j;

@Log4j
public class MDeErrorReport implements ErrorReport {
	@Override
	public void report(String name, String element, Throwable exception) {
		log.debug( "Failed to process element " + element + " on queue " + name, exception );
		final Map<String, String> data = new HashMap<>();
		data.put( "name", name );
		data.put( "element", element );
		DripStat.setException( exception, data );
	}

	@Override
	public void report(final String name, final Throwable exception) {
		log.debug( "Failed to process queue " + name, exception );
		DripStat.setException( exception, Collections.singletonMap( "name", name ) );
	}
}
