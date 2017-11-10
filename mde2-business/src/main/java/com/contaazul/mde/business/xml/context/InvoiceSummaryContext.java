package com.contaazul.mde.business.xml.context;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.contaazul.mde.res_nfe_v1_00.ResNFe;

@Singleton
public class InvoiceSummaryContext {
	private JAXBContext context;

	@PostConstruct
	public void get() {
		try {
			context = JAXBContext.newInstance( ResNFe.class );
		} catch (JAXBException e) {
			throw new RuntimeException( e );
		}
	}

	public JAXBContext getContext() {
		return context;
	}

}
