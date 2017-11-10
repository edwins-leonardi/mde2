package com.contaazul.mde.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.contaazul.invoiceissuer.api.nfe.OperationType;

public class OperationTypeConverterTest {
	public static final String IN = "0";
	public static final String OUT = "1";

	@InjectMocks
	private OperationTypeConverter operationTypeConverter;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void convert() {
		assertThat( operationTypeConverter.convert( IN ), equalTo( OperationType.IN ) );
		assertThat( operationTypeConverter.convert( OUT ), equalTo( OperationType.OUT ) );
	}

}
