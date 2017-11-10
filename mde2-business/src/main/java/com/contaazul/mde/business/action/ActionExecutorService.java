package com.contaazul.mde.business.action;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.apache.axis2.client.Stub;
import org.apache.xmlbeans.XmlObject;

import com.contaazul.invoiceissuer.api.UF;
import com.contaazul.invoiceissuer.api.request.Result;
import com.contaazul.invoiceissuer.http.ExponentialBackOff;
import com.contaazul.mde.business.action.exception.ActionMethodNotFoundException;
import com.contaazul.mde.business.action.exception.StubCreationException;
import com.contaazul.mde.business.action.exception.WebServiceInvocationException;
import com.contaazul.mde.config.MDeConfig;
import com.contaazul.nfe.key.KeyProvider;

@Log4j
public class ActionExecutorService {
	@Inject
	private Authenticator authenticator;
	@Inject
	private MDeConfig config;

	public <T extends Result> T execute(KeyProvider keyProvider, Action action, UF uf, XmlObject xml) {
		final Stub stub = buildStub( action, keyProvider );
		final XmlObject header = action.header( uf.getCode() );
		final XmlObject body = action.body( xml );
		final XmlObject result = new ExponentialBackOff().execute( () -> tryToSend( action, stub, header, body ) );
		return action.parse( result );
	}

	private Stub buildStub(Action action, KeyProvider keyProvider) {
		String url = action.getURL( config.isProduction() );
		Stub stub = createStub( action, url );
		authenticator.setup( stub._getServiceClient(), keyProvider );
		return stub;
	}

	private Stub createStub(Action action, String url) {
		try {
			return action.getStub().getConstructor( String.class ).newInstance( url );
		} catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
			throw new StubCreationException( e, action.getStub(), url );
		}
	}

	private XmlObject tryToSend(Action action, Stub stub, XmlObject header, XmlObject body) {
		log.debug( String.format( "XML a ser enviado: \nheader=%s\nbody=%s", header, body ) );
		XmlObject result = send( action, stub, header, body );
		log.debug( "Retorno do SEFAZ:\n" + result );
		return result;
	}

	private XmlObject send(Action action, Stub stub, XmlObject header, XmlObject body) {
		Class<?> stubClass = stub.getClass();
		Method method = getMethod( stubClass, action.getMethod().getName() );
		try {
			return (XmlObject) (header == null ? method.invoke( stub, body ) : method.invoke( stub, body, header ));
		} catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
			throw new WebServiceInvocationException( e, stubClass, method );
		}
	}

	private Method getMethod(Class<?> clazz, String name) {
		return Stream.of( clazz.getMethods() )
				.filter( method -> method.getName().equals( name ) )
				.findFirst()
				.orElseThrow( () -> new ActionMethodNotFoundException( clazz, name ) );
	}

}
