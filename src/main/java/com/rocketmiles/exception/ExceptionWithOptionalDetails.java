package com.rocketmiles.exception;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base exception class used by CashRegister and Monies Exception classes.
 * 
 * @author dmitrif
 *
 */
@SuppressWarnings("serial")
public class ExceptionWithOptionalDetails extends Exception {

	protected Object[] additionalParameters;

	public ExceptionWithOptionalDetails(String errorMessage, Throwable throwable, Object... additionalParameters) {
		super(errorMessage + Stream.of(additionalParameters).map(Object::toString).collect(Collectors.joining(", ")),
				throwable);
	}

	public ExceptionWithOptionalDetails(String errorMessage, Object... additionalParameters) {
		this(errorMessage, null, additionalParameters);
	}

	public Object[] getAdditionalParameters() {
		return additionalParameters;
	}
}