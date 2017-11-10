package com.contaazul.mde.business.xml.context;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TRetEnvEvento;

@Singleton
public class EventResponseContext {
	private JAXBContext context;

	@PostConstruct
	public void get() {
		try {
			context = JAXBContext.newInstance( TRetEnvEvento.class );
		} catch (JAXBException e) {
			throw new RuntimeException( e );
		}
	}

	public JAXBContext getContext() {
		return context;
	}

}
