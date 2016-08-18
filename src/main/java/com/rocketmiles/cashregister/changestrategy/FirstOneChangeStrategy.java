package com.rocketmiles.cashregister.changestrategy;

import java.util.List;

/**
 * This change strategy picks the first available change combination
 * @author dmitrif
 *
 */
public class FirstOneChangeStrategy extends BaseChangeStrategy {

	@Override
	protected List<Integer> selectChangeCombination(List<List<Integer>> combinations) {
		return combinations.get(0);
	}

}
