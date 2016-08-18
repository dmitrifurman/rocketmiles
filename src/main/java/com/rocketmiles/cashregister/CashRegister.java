package com.rocketmiles.cashregister;

import com.rocketmiles.cashregister.CashRegisterException.CashRegisterExceptionType;
import com.rocketmiles.cashregister.changestrategy.ChangeStrategy;
import com.rocketmiles.cashregister.changestrategy.FirstOneChangeStrategy;
import com.rocketmiles.monies.Monies;
import com.rocketmiles.monies.MoniesException;

/**
 * Main class that represents Cash Register. It mostly redirects various duties
 * to other classes
 * 
 * @author dmitrif
 *
 */
public class CashRegister {

	CashDrawer cashDrawer;

	// Only one strategy is implemented
	// However the system setup to be able to handle different ones
	// If a container like Spring is introduced to a program
	// The strategy can be wired through configuration
	ChangeStrategy changeStrategy = new FirstOneChangeStrategy();

	public CashRegister(Monies monies) {
		cashDrawer = new CashDrawer(monies);
	}

	public CashRegister() {
		this(null);
	}

	public Monies change(int amount) throws CashRegisterException {
		Monies changedMonies = changeStrategy.change(amount, cashDrawer.open());
		try {
			take(changedMonies);
		} catch (MoniesException mex) {
			throw new CashRegisterException(CashRegisterExceptionType.UNEXPECTED_CURRENCY, mex);
		}
		return changedMonies;
	}

	public void put(Monies monies) {
		Monies existingMoney = cashDrawer.open();
		cashDrawer.close(existingMoney.combine(monies));
	}

	public void take(Monies monies) throws MoniesException {
		Monies existingMoney = cashDrawer.open();
		cashDrawer.close(existingMoney.divide(monies));
	}

	public int getMonetaryValue() {
		return cashDrawer.open().getMonetaryValue();
	}

	public Monies balance() {
		return cashDrawer.open();
	}
}
