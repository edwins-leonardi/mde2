package com.contaazul.mde.aws;

import lombok.NonNull;

import com.contaazul.aws.s3.S3Context;

public class MDeS3Context extends S3Context {
	private static final String FOLDER = "/invoices/imported";

	public MDeS3Context(@NonNull Long tenantId, S3Context context) {
		super( context.getS3Client(), tenantId + FOLDER, context.getBucketname() );
	}
}
