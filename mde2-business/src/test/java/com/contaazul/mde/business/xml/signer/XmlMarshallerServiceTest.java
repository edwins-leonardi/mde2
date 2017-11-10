package com.contaazul.mde.business.xml.signer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.contaazul.invoiceissuer.parser.XmlObjectParser;
import com.contaazul.mde.business.action.XmlMarshalException;
import com.contaazul.mde.business.action.XmlParseException;
import com.contaazul.mde.business.xml.QueryMarshaller;
import com.contaazul.mde.dist_dfe_int_v1_00.DistDFeInt;

public class XmlMarshallerServiceTest {
	@Mock
	private XmlObjectParser xmlObjectParser;
	@Mock
	private QueryMarshaller queryMarshaller;
	@Mock
	private InputStream stream;
	@Mock
	private XmlObject xml;

	@InjectMocks
	private XmlMarshallerService service;

	@Before
	public void setup() {
		initMocks( this );
	}

	@Test
	public void marshalQuery() throws Exception {
		DistDFeInt distDFeInt = new DistDFeInt();
		when( queryMarshaller.asStream( distDFeInt ) ).thenReturn( stream );
		when( xmlObjectParser.parse( stream ) ).thenReturn( xml );
		assertThat( service.marshalQuery( distDFeInt ), equalTo( xml ) );
	}

	@Test(expected = XmlMarshalException.class)
	public void marshalQueryErrorMarshalling() throws Exception {
		DistDFeInt distDFeInt = new DistDFeInt();
		doThrow( XmlException.class ).when( queryMarshaller ).asStream( distDFeInt );
		assertThat( service.marshalQuery( distDFeInt ), equalTo( xml ) );
	}

	@Test
	public void parse() throws Exception {
		InputStream inputStream = mock( InputStream.class );
		XmlObject xmlObject = mock( XmlObject.class );
		when( xmlObjectParser.parse( inputStream ) ).thenReturn( xmlObject );
		XmlObject result = service.parse( inputStream );
		assertThat( result, equalTo( xmlObject ) );
	}

	@Test(expected = XmlParseException.class)
	public void errorWhenParsing() throws Exception {
		InputStream inputStream = mock( InputStream.class );
		doThrow( IOException.class ).when( xmlObjectParser ).parse( inputStream );
		service.parse( inputStream );
	}

}
