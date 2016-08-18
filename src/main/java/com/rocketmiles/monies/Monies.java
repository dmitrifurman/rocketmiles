package com.rocketmiles.monies;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rocketmiles.monies.MoniesException.MoniesExceptionType;

/**
 * Class that represents a characteristics of money used together. It makes a
 * distinction on what kind of banknotes constitute the money. This class is
 * immutable to preserve integrity of the money.
 * 
 * @author dmitrif
 *
 */
public final class Monies {
	private final Map<BanknoteType, Integer> banknoteCounts;

	public Monies(Map<BanknoteType, Integer> banknoteCounts) {
		// remove any counts that have 0's
		this.banknoteCounts = banknoteCounts.entrySet().stream().filter(p -> p.getValue() > 0)
				.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}

	public Map<BanknoteType, Integer> getBanknoteCounts() {
		return new HashMap<BanknoteType, Integer>(banknoteCounts);
	}

	public int getMonetaryValue() {
		return banknoteCounts.entrySet().stream().mapToInt(p -> p.getKey().getDenomination() * p.getValue()).sum();
	}

	/**
	 * Combines two sets of Monies into one. Preserves banknote integrity
	 * 
	 */
	public Monies combine(Monies monies) {
		// converts each map into an entry set
		// converts each set into an entry stream,
		// then "concatenates" it in place of the original set
		// // collects into a map where each entry is based
		// on the entries in the stream such that if a value already exist for
		// a given key, the sum of both values is taken
		Map<BanknoteType, Integer> banknoteCountsCombined = Stream.of(banknoteCounts, monies.getBanknoteCounts())
				.map(Map::entrySet).flatMap(Collection::stream)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
		return new Monies(banknoteCountsCombined);
	}

	/**
	 * Divides Monies based on requested division. Preserves banknote integrity.
	 * 
	 */
	public Monies divide(Monies monies) throws MoniesException {
		if (this.getMonetaryValue() < monies.getMonetaryValue()) {
			throw new MoniesException(MoniesExceptionType.INSUFFICIENT_AMT);
		}

		Map<BanknoteType, Integer> banknoteCountsToDivide = monies.getBanknoteCounts();
		// pre-populate divided counts with the original in case the monies to
		// be subtracted
		// does not contain some banknote types.
		Map<BanknoteType, Integer> banknoteCountsDivided = new HashMap<BanknoteType, Integer>(banknoteCounts);

		for (Map.Entry<BanknoteType, Integer> banknoteCountToDivide : banknoteCountsToDivide.entrySet()) {
			BanknoteType bankNoteTypeToDivide = banknoteCountToDivide.getKey();
			if (banknoteCounts.containsKey(bankNoteTypeToDivide)) {
				int dividedCount = banknoteCounts.get(bankNoteTypeToDivide)
						- banknoteCountsToDivide.get(bankNoteTypeToDivide);
				if (dividedCount >= 0) {
					banknoteCountsDivided.put(bankNoteTypeToDivide, dividedCount);
				} else {
					throw new MoniesException(MoniesExceptionType.INSUFFICIENT_BANKNOTE_TYPE_COUNT,
							bankNoteTypeToDivide);
				}
			} else {
				throw new MoniesException(MoniesExceptionType.INSUFFICIENT_BANKNOTE_TYPE_COUNT, bankNoteTypeToDivide);
			}
		}
		return new Monies(banknoteCountsDivided);
	}

	@Override
	public boolean equals(Object monies) {
		if (monies instanceof Monies) {
			return this.banknoteCounts.equals(((Monies) monies).banknoteCounts);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("$").append(getMonetaryValue()).append(" ")
				.append(banknoteCounts.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
						.map(map -> map.getKey().toString() + ": " + map.getValue()).collect(Collectors.joining(", ")));

		return stringBuilder.toString();
	}
}
