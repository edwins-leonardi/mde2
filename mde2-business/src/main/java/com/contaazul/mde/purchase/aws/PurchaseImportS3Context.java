package com.contaazul.mde.purchase.aws;

import lombok.NonNull;

import com.contaazul.aws.s3.S3Context;

public class PurchaseImportS3Context extends S3Context {
	private static final String FOLDER = "/notafiscal/purchase/import";

	public PurchaseImportS3Context(@NonNull Long tenantId, S3Context context) {
		super( context.getS3Client(), tenantId + FOLDER, context.getBucketname() );
	}

}
