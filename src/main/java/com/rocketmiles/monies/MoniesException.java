package com.rocketmiles.monies;

import com.rocketmiles.exception.ExceptionWithOptionalDetails;

@SuppressWarnings("serial")
public class MoniesException extends ExceptionWithOptionalDetails {
	private static final String INSUFFICIENT_BANKNOTE_TYPE_ERROR_MSG = "Monies does not contain sufficient quantity of banknote type ";
	private static final String INSUFFICIENT_FUNDS_ERROR_MSG = "Monies has less funds than required to process this transation ";
	private static final String INVALID_DENOMINATION_ERROR_MSG = "Denomination does not match any banknote ";

	public enum MoniesExceptionType {
		INSUFFICIENT_BANKNOTE_TYPE_COUNT(INSUFFICIENT_BANKNOTE_TYPE_ERROR_MSG), INSUFFICIENT_AMT(
				INSUFFICIENT_FUNDS_ERROR_MSG), INVALID_DENOMINATION(INVALID_DENOMINATION_ERROR_MSG);

		private String errorMessage;

		private MoniesExceptionType(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

	}

	private MoniesExceptionType exceptionType;

	public MoniesException(MoniesExceptionType type, Object... additionalParameters) {
		super(type.errorMessage, additionalParameters);
		this.exceptionType = type;
		this.additionalParameters = additionalParameters;
	}

	public MoniesExceptionType getExceptionType() {
		return exceptionType;
	}
}
