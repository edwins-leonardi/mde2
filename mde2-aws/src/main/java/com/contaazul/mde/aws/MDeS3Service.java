package com.contaazul.mde.aws;

import javax.inject.Inject;

import com.contaazul.aws.s3.S3Context;
import com.contaazul.aws.s3.S3Service;

public class MDeS3Service {
	@Inject
	private S3Service s3;

	public S3Context createContext(Long tenantId) {
		return new MDeS3Context( tenantId, s3.createContext( tenantId ) );
	}
}
