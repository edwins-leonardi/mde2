package com.contaazul.mde.business.action;

public interface ResultConverter<T, U> {

	U convert(T t);

}
