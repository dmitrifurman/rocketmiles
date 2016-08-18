package com.rocketmiles.cashregister;

import com.rocketmiles.exception.ExceptionWithOptionalDetails;

/**
 * Exception class that handles exceptions coming from CashRegister.
 * 
 * @author dmitrif
 *
 */
@SuppressWarnings("serial")
public class CashRegisterException extends ExceptionWithOptionalDetails {
	private static final String NO_EXACT_CHANGE_TYPE_ERROR_MSG = "Cash register does not have exact change";
	private static final String INSUFFICIENT_FUNDS_ERROR_MSG = "Cash register has less funds than required to process this transation ";
	private static final String UNEXPECTED_CURRENCY_ERROR_MSG = "Cash register has unexpected currency ";

	public enum CashRegisterExceptionType {
		NO_EXACT_CHANGE(NO_EXACT_CHANGE_TYPE_ERROR_MSG), INSUFFICIENT_AMT(
				INSUFFICIENT_FUNDS_ERROR_MSG), UNEXPECTED_CURRENCY(UNEXPECTED_CURRENCY_ERROR_MSG);

		private String errorMessage;

		private CashRegisterExceptionType(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

	}

	private CashRegisterExceptionType exceptionType;

	public CashRegisterException(CashRegisterExceptionType type, Throwable throwable, Object... additionalParameters) {
		super(type.errorMessage, throwable, additionalParameters);
		this.exceptionType = type;
		this.additionalParameters = additionalParameters;
	}

	public CashRegisterException(CashRegisterExceptionType type, Object... additionalParameters) {
		this(type, null, additionalParameters);
	}

	public CashRegisterExceptionType getExceptionType() {
		return exceptionType;
	}
}
