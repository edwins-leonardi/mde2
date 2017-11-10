package com.contaazul.mde.job.processor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ExceptionCauseTest {
	private static final String DEFAULT_MESSAGE = "Erro desconhecido: RuntimeException";
	private static final String MESSAGE = "Aaaaah!";

	@Test
	public void withMessageWithoutCause() {
		assertThat( MESSAGE, equalTo( new ExceptionCause( new RuntimeException( MESSAGE ) ).getText() ) );
	}

	@Test
	public void withoutMessageWithoutCause() {
		assertThat( DEFAULT_MESSAGE, equalTo( new ExceptionCause( new RuntimeException() ).getText() ) );
	}

	@Test
	public void withMessageWithCause() {
		RuntimeException cause = new RuntimeException( MESSAGE );
		assertThat( MESSAGE, equalTo( new ExceptionCause( new RuntimeException( cause ) ).getText() ) );
	}

	@Test
	public void withoutMessageWithCause() {
		RuntimeException cause = new RuntimeException();
		assertThat( DEFAULT_MESSAGE, equalTo( new ExceptionCause( new RuntimeException( cause ) ).getText() ) );
	}

}
