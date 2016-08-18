package com.rocketmiles.monies;

import com.rocketmiles.monies.MoniesException.MoniesExceptionType;

/**
 * Defines all available banknotes in our currency.
 * 
 * @author dmitrif
 *
 */
public enum BanknoteType {
	TWENTY(20), TEN(10), FIVE(5), TWO(2), ONE(1);

	private int denomination;

	private BanknoteType(int denomination) {
		this.denomination = denomination;
	}

	public int getDenomination() {
		return denomination;
	}

	public static BanknoteType findByDenomination(int denomination) throws MoniesException {
		for (BanknoteType banknoteType : BanknoteType.values()) {
			if (banknoteType.getDenomination() == denomination) {
				return banknoteType;
			}
		}
		throw new MoniesException(MoniesExceptionType.INVALID_DENOMINATION, denomination);
	}
}
