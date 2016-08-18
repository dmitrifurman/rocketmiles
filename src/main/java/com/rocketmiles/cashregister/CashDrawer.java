package com.rocketmiles.cashregister;

import java.util.HashMap;
import java.util.Map;

import com.rocketmiles.monies.BanknoteType;
import com.rocketmiles.monies.Monies;

/**
 * Class representing a drawer of the Cash Register that includes sections for
 * various banknote types.
 * 
 * @author dmitrif
 *
 */
public class CashDrawer {
	// While Monies class and this map look
	// similar there are significant distinctions.
	// Monies is immutable class that is represents
	// a group of money. If the group does not have
	// certain banknote type it is not represented in the Monies.
	// cashSections of the CashDrawer are property of the Drawer.
	// If a section that is designated for certain banknote type is empty
	// it is represented by 0. This property is also mutable as
	// the state of the CashDrawer changes with deposits and withdrawals of
	// money.
	private Map<BanknoteType, Integer> cashSections;

	public CashDrawer(Monies monies) {
		cashSections = new HashMap<BanknoteType, Integer>();
		for (BanknoteType banknoteType : BanknoteType.values()) {
			cashSections.put(banknoteType, 0);
		}
		if (monies != null) {
			monies.getBanknoteCounts().entrySet().forEach(map -> cashSections.put(map.getKey(), map.getValue()));
		}
	}

	public CashDrawer() {
		this(null);
	}

	public Monies open() {
		return new Monies(cashSections);
	}

	public void close(Monies updatedMonies) {
		Map<BanknoteType, Integer> updatedBanknoteCounts = updatedMonies.getBanknoteCounts();
		for (BanknoteType banknoteType : cashSections.keySet()) {
			int updatedBanknoteTypeQuantity = updatedBanknoteCounts.containsKey(banknoteType)
					? updatedBanknoteCounts.get(banknoteType) : 0;
			cashSections.put(banknoteType, updatedBanknoteTypeQuantity);
		}
	}
}
