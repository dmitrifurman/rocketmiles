package com.rocketmiles.monies;

import static com.rocketmiles.monies.TestMoniesBuilder.build90in21468;
import static com.rocketmiles.monies.TestMoniesBuilder.buildMoney;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.rocketmiles.monies.MoniesException.MoniesExceptionType;

public class MoniesTest {

	@Test
	public void testCombine() {
		Monies original = build90in21468();
		Monies toCombine = build90in21468();
		Monies combined = original.combine(toCombine);
		assertEquals(180, combined.getMonetaryValue());
		Map<BanknoteType, Integer> banknoteCounts = combined.getBanknoteCounts();
		assertEquals(4, banknoteCounts.get(BanknoteType.TWENTY).intValue());
		assertEquals(2, banknoteCounts.get(BanknoteType.TEN).intValue());
		assertEquals(8, banknoteCounts.get(BanknoteType.FIVE).intValue());
		assertEquals(12, banknoteCounts.get(BanknoteType.TWO).intValue());
		assertEquals(16, banknoteCounts.get(BanknoteType.ONE).intValue());
	}

	@Test
	public void testDivideSuccess() throws Exception {
		Monies original = build90in21468();
		Monies toDivide = buildMoney(1, 0, 3, 4, 6);
		Monies divided = original.divide(toDivide);
		assertEquals(41, divided.getMonetaryValue());
	}

	@Test(expected = MoniesException.class)
	public void testDivideFailureAmountLarger() throws Exception {
		Monies original = build90in21468();
		Monies toDivide = buildMoney(3, 2, 5, 7, 9);
		try {
			original.divide(toDivide);
		} catch (MoniesException mex) {
			assertEquals(MoniesExceptionType.INSUFFICIENT_AMT, mex.getExceptionType());
			throw mex;
		}
	}

	@Test(expected = MoniesException.class)
	public void testDivideFailureInsufficientBanknoteTypeCount() throws Exception {
		Monies original = build90in21468();
		Monies toDivide = buildMoney(1, 2, 3, 5, 7);
		try {
			original.divide(toDivide);
		} catch (MoniesException mex) {
			assertEquals(MoniesExceptionType.INSUFFICIENT_BANKNOTE_TYPE_COUNT, mex.getExceptionType());
			throw mex;
		}
	}

	@Test(expected = MoniesException.class)
	public void testDivideFailureInsufficientBanknoteTypeCount2() throws Exception {
		Monies original = buildMoney(1, 0, 3, 5, 7);
		Monies toDivide = buildMoney(0, 1, 2, 4, 6);
		try {
			original.divide(toDivide);
		} catch (MoniesException mex) {
			assertEquals(MoniesExceptionType.INSUFFICIENT_BANKNOTE_TYPE_COUNT, mex.getExceptionType());
			throw mex;
		}
	}
}
