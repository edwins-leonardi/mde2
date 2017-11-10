package com.contaazul.mde.business.xml.context;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.contaazul.mde.proc_nfe_v3_10.TNfeProc;

@Singleton
public class InvoiceContext {
	private JAXBContext context;

	@PostConstruct
	public void get() {
		try {
			context = JAXBContext.newInstance( TNfeProc.class );
		} catch (JAXBException e) {
			throw new RuntimeException( e );
		}
	}

	public JAXBContext getContext() {
		return context;
	}

}
