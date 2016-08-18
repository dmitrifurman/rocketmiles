package com.rocketmiles.cashregister.changestrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import com.rocketmiles.cashregister.CashRegisterException;
import com.rocketmiles.cashregister.CashRegisterException.CashRegisterExceptionType;
import com.rocketmiles.monies.BanknoteType;
import com.rocketmiles.monies.Monies;
import com.rocketmiles.monies.MoniesException;

/**
 * Base class that hands most of Change functionality while deferring specifics
 * on which change set to choose to concrete strategy implementations.
 * 
 * @author dmitrif
 *
 */
public abstract class BaseChangeStrategy implements ChangeStrategy {

	public static final int MAX_NUMBER_OF_ATTEMPTS_FOR_FINDING_EXACT_CHANGE = 1000;

	/**
	 * This method utilizes random search algorithm in order to identify a
	 * combination of suitable banknotes to process a change. There are other
	 * algorithms available for this type of problem. This one was chosen due to
	 * its relatively lower complexity and readability. The point of this
	 * function is to do better than a human would giving out the change and
	 * this function does that.
	 * 
	 */
	@Override
	public Monies change(int amount, Monies balance) throws CashRegisterException {
		if (balance.getMonetaryValue() < amount) {
			throw new CashRegisterException(CashRegisterExceptionType.INSUFFICIENT_AMT, amount);
		}

		List<Integer> searchSpace = createChangeCombinationSearchSpace(amount, balance);

		// if the sum of all eligible banknotes is less than
		// the change amount we don't proceed.
		// For example if we have to change 14 and after filtering 20's
		// we are left with 5, 5, and 2. While the cash register had more
		// money originally than the change amount, the change banknotes that
		// we have are not appropriate to process the change.
		if (searchSpace.stream().mapToInt(Integer::intValue).sum() < amount) {
			throw new CashRegisterException(CashRegisterExceptionType.NO_EXACT_CHANGE, amount);
		}

		ArrayList<Integer> tempSearchSpace = new ArrayList<Integer>(searchSpace);
		ArrayList<Integer> selectedBankNotes = new ArrayList<Integer>();
		ArrayList<List<Integer>> goodCombinations = new ArrayList<List<Integer>>();
		int remainingAmount = amount;

		Random random = new Random();
		int randomIndex;
		// Randomized search with pre-set size
		for (int i = 0; i < MAX_NUMBER_OF_ATTEMPTS_FOR_FINDING_EXACT_CHANGE; i++) {
			randomIndex = random.nextInt(tempSearchSpace.size());
			Integer banknote = tempSearchSpace.get(randomIndex);
			// Since we continuously shrink the search set we do
			// not to have numbers greater than amount. So we don't need to
			// check
			// if the remaining amount is greater or equal to remaining
			// banknotes.
			remainingAmount -= banknote;
			selectedBankNotes.add(banknote);

			shrinkChangeCombinationSearchSpace(randomIndex, remainingAmount, tempSearchSpace);

			// if we found a combination we store it
			if (remainingAmount == 0) {
				storeNewCombination(selectedBankNotes, goodCombinations);
			}
			// if we found a combination we reload everything
			// and look for others up to our number of randomized tries
			// or if there are no more banknotes that are smaller or equal
			// to the amount we have remaining.
			if (remainingAmount == 0 || tempSearchSpace.size() == 0) {
				selectedBankNotes = new ArrayList<Integer>();
				remainingAmount = amount;
				tempSearchSpace = new ArrayList<Integer>(searchSpace);
			}
		}
		// We have multiple combinations here.
		// The code is setup to handle these combinations differently through
		// strategy pattern. For the time being only one strategy has been
		// implemented
		// that takes the first combination from the list. Others could the
		// combinations
		// with fewest number of banknotes or the banknotes with the smallest
		// denominations.
		if (goodCombinations.size() > 0) {
			try {
				Monies changedMonies = convertBankNotesToMonies(selectChangeCombination(goodCombinations));
				return changedMonies;
			} catch (MoniesException mex) {
				throw new CashRegisterException(CashRegisterExceptionType.UNEXPECTED_CURRENCY, mex);
			}
		} else {
			throw new CashRegisterException(CashRegisterExceptionType.NO_EXACT_CHANGE, amount);
		}
	}

	private List<Integer> createChangeCombinationSearchSpace(int amount, Monies balance) {
		// Eliminate denominations larger than change amount
		Map<BanknoteType, Integer> eligibleBanknoteTypes = balance.getBanknoteCounts().entrySet().stream()
				.filter(map -> map.getKey().getDenomination() <= amount)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		// Create a search space with all available banknotes that can be use to
		// make a change.
		// We create this list once and reuse as we randomly attempt a number of
		// combinations
		// in order to find an exact change.
		ArrayList<Integer> searchSpace = new ArrayList<Integer>();
		for (Entry<BanknoteType, Integer> entry : eligibleBanknoteTypes.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				searchSpace.add(entry.getKey().getDenomination());
			}
		}
		return searchSpace;
	}

	private void shrinkChangeCombinationSearchSpace(int usedBanknoteIndex, int remainingAmount,
			List<Integer> tempSearchSpace) {
		// eliminate used banknote from further consideration
		tempSearchSpace.remove(usedBanknoteIndex);
		// eliminate denominations larger than remaining amount
		for (Iterator<Integer> iterator = tempSearchSpace.iterator(); iterator.hasNext();) {
			if (iterator.next() > remainingAmount) {
				iterator.remove();
			}
		}
	}

	private void storeNewCombination(List<Integer> newBanknoteCombination,
			List<List<Integer>> existingBanknoteCombinations) {
		// Sort so we can compare the combinations for duplicate
		Collections.sort(newBanknoteCombination);
		boolean combinationExists = false;
		// Check if this combination already exists
		for (List<Integer> combination : existingBanknoteCombinations) {
			if (newBanknoteCombination.equals(combination)) {
				combinationExists = true;
				break;
			}
		}
		if (!combinationExists) {
			existingBanknoteCombinations.add(newBanknoteCombination);
		}
	}

	private Monies convertBankNotesToMonies(List<Integer> banknotes) throws MoniesException {
		HashMap<BanknoteType, Integer> banknoteCounts = new HashMap<BanknoteType, Integer>();
		for (Integer banknote : banknotes) {
			BanknoteType key = BanknoteType.findByDenomination(banknote);
			int banknoteCount = banknoteCounts.containsKey(key) ? banknoteCounts.get(key) : 0;
			banknoteCounts.put(key, banknoteCount + 1);
		}
		return new Monies(banknoteCounts);
	}

	abstract protected List<Integer> selectChangeCombination(List<List<Integer>> goodCombinations);

}
