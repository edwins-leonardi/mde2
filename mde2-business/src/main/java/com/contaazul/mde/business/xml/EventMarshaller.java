package com.contaazul.mde.business.xml;

import com.contaazul.mde.business.action.MDeMarshaller;
import com.contaazul.mde.proc_conf_recebto_nfe_v1_00.TEnvEvento;

public class EventMarshaller extends MDeMarshaller<TEnvEvento> {

	@Override
	protected Class<TEnvEvento> getClazz() {
		return TEnvEvento.class;
	}

	@Override
	protected String getRootTag() {
		return "envEvento";
	}

}
