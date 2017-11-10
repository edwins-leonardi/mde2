package com.contaazul.mde.business.xml;

import com.contaazul.mde.business.action.MDeMarshaller;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt;

public class QueryMarshaller extends MDeMarshaller<DistDFeInt> {

	@Override
	protected Class<DistDFeInt> getClazz() {
		return DistDFeInt.class;
	}

	@Override
	protected String getRootTag() {
		return "distDFeInt";
	}

}
