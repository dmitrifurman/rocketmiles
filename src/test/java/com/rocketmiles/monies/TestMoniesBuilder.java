package com.rocketmiles.monies;

import java.util.HashMap;

public class TestMoniesBuilder {

	public static Monies build90in21468() {
		return buildMoney(2, 1, 4, 6, 8);
	}

	public static Monies build106in23456() {
		return buildMoney(2, 3, 4, 5, 6);
	}

	public static Monies buildMoney(int twenty, int ten, int five, int two, int one) {
		HashMap<BanknoteType, Integer> banknoteCounts = new HashMap<BanknoteType, Integer>();
		banknoteCounts.put(BanknoteType.TWENTY, twenty);
		banknoteCounts.put(BanknoteType.TEN, ten);
		banknoteCounts.put(BanknoteType.FIVE, five);
		banknoteCounts.put(BanknoteType.TWO, two);
		banknoteCounts.put(BanknoteType.ONE, one);
		return new Monies(banknoteCounts);
	}
}
