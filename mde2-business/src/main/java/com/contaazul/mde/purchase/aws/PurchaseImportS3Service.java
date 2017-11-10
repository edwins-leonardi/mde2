package com.contaazul.mde.purchase.aws;

import javax.inject.Inject;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.aws.s3.S3Service;

public class PurchaseImportS3Service {
	@Inject
	private S3Service s3;

	public S3Context createContext(Long tenantId) {
		return new PurchaseImportS3Context( tenantId, s3.createContext( tenantId ) );
	}

}
