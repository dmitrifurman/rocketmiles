package com.rocketmiles.cashregister.changestrategy;

import com.rocketmiles.cashregister.CashRegisterException;
import com.rocketmiles.monies.Monies;

public interface ChangeStrategy {
	Monies change(int amount, Monies balance) throws CashRegisterException;

}
