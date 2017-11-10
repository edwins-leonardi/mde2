package com.contaazul.mde.business.xml.context;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.contaazul.mde.ret_dist_dfe_int_v1_00.RetDistDFeInt;

@Singleton
public class QueryResponseContext {
	private JAXBContext context;

	@PostConstruct
	public void get() {
		try {
			context = JAXBContext.newInstance( RetDistDFeInt.class );
		} catch (JAXBException e) {
			throw new RuntimeException( e );
		}
	}

	public JAXBContext getContext() {
		return context;
	}

}
