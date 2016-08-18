package com.rocketmiles.cashregister.changestrategy;

import static com.rocketmiles.monies.TestMoniesBuilder.build90in21468;
import static com.rocketmiles.monies.TestMoniesBuilder.buildMoney;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rocketmiles.cashregister.CashRegisterException;
import com.rocketmiles.cashregister.CashRegisterException.CashRegisterExceptionType;
import com.rocketmiles.monies.Monies;

public class ChangeStrategyTest {

	FirstOneChangeStrategy target = new FirstOneChangeStrategy();

	@Test
	public void testChange() throws CashRegisterException {
		Monies change = target.change(30, build90in21468());
		assertEquals(30, change.getMonetaryValue());
	}

	@Test(expected = CashRegisterException.class)
	public void testChangeCantBeMade() throws Exception {
		try {
			target.change(37, buildMoney(2, 1, 1, 0, 0));
		} catch (CashRegisterException crex) {
			assertEquals(CashRegisterExceptionType.NO_EXACT_CHANGE, crex.getExceptionType());
			throw crex;
		}
	}

	@Test(expected = CashRegisterException.class)
	public void testChangeCantBeMadeEnoughFundsButWrongDenominations() throws Exception {
		try {
			target.change(14, buildMoney(1, 0, 2, 1, 0));
		} catch (CashRegisterException crex) {
			assertEquals(CashRegisterExceptionType.NO_EXACT_CHANGE, crex.getExceptionType());
			throw crex;
		}
	}

	@Test(expected = CashRegisterException.class)
	public void testChangeCantBeMadeNotEnoughFunds() throws Exception {
		try {
			target.change(14, buildMoney(0, 0, 2, 1, 0));
		} catch (CashRegisterException crex) {
			assertEquals(CashRegisterExceptionType.INSUFFICIENT_AMT, crex.getExceptionType());
			throw crex;
		}
	}
}
