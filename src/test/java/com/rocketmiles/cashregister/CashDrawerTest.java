package com.rocketmiles.cashregister;

import static com.rocketmiles.monies.TestMoniesBuilder.build106in23456;
import static com.rocketmiles.monies.TestMoniesBuilder.build90in21468;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.rocketmiles.monies.Monies;

public class CashDrawerTest {
	private CashDrawer target;

	@Before
	public void init() {
		target = new CashDrawer(build106in23456());
	}

	@Test
	public void testOpen() {
		Monies monies = target.open();
		assertEquals(106, monies.getMonetaryValue());
	}

	@Test
	public void testClose() {
		target.close(build90in21468());
		assertEquals(90, target.open().getMonetaryValue());
	}
}
