package com.rocketmiles.cashregister;

import static com.rocketmiles.monies.TestMoniesBuilder.build90in21468;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rocketmiles.monies.Monies;

public class CashRegisterTest {

	private CashRegister target;

	@Test
	public void testChange() throws CashRegisterException {
		target = new CashRegister(build90in21468());
		Monies change = target.change(30);
		assertEquals(30, change.getMonetaryValue());
		assertEquals(60, target.getMonetaryValue());
	}
}
