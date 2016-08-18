package com.rocketmiles.integrationtest;

import static com.rocketmiles.monies.TestMoniesBuilder.buildMoney;
import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.rocketmiles.cashregister.CashRegister;
import com.rocketmiles.cashregister.CashRegisterException;
import com.rocketmiles.cashregister.CashRegisterException.CashRegisterExceptionType;
import com.rocketmiles.monies.Monies;

public class IntegrationTest {
	private static final Logger LOGGER = Logger.getLogger( IntegrationTest.class.getName() );
	
	@Test
	public void intergrationTest() throws Exception {
		Monies initialMonies = buildMoney(1, 2, 3, 4, 5);
		CashRegister cashRegister = new CashRegister(initialMonies);
		// show current total
		assertEquals(68, cashRegister.getMonetaryValue());
		// and each denomination
		assertEquals(initialMonies, cashRegister.balance());
		LOGGER.log( Level.INFO, initialMonies.toString());
		// put
		cashRegister.put(buildMoney(1, 2, 3, 0, 5));
		// show current total
		assertEquals(128, cashRegister.getMonetaryValue());
		// and each denomination
		assertEquals(buildMoney(2, 4, 6, 4, 10), cashRegister.balance());
		LOGGER.log( Level.INFO, cashRegister.balance().toString());
		// take bills in each denomination: #$20 #$10 #$5 #$2 #$1
		cashRegister.take(buildMoney(1, 4, 3, 0, 10));
		// show current total
		assertEquals(43, cashRegister.getMonetaryValue());
		// and each denomination
		assertEquals(buildMoney(1, 0, 3, 4, 0), cashRegister.balance());
		LOGGER.log( Level.INFO, cashRegister.balance().toString());
		// given amount
		Monies change = cashRegister.change(11);
		// show change in each denomination: #$20 #$10 #$5 #$2 #$1
		assertEquals(buildMoney(0, 0, 1, 3, 0), change);
		LOGGER.log( Level.INFO, change.toString());
		// and remove money from cash register
		// show current total
		assertEquals(32, cashRegister.getMonetaryValue());
		// and each denomination
		assertEquals(buildMoney(1, 0, 2, 1, 0), cashRegister.balance());
		LOGGER.log( Level.INFO, cashRegister.balance().toString());
		// show error no change can be made > change 14
		try {
			cashRegister.change(14);
		} catch (CashRegisterException crex) {
			assertEquals(CashRegisterExceptionType.NO_EXACT_CHANGE, crex.getExceptionType());
		}
	}

	@Test
	public void specificRequest13balanceChange8() throws CashRegisterException {
		CashRegister cashRegister = new CashRegister(buildMoney(0, 0, 1, 2, 4));
		assertEquals(13, cashRegister.getMonetaryValue());
		Monies change = cashRegister.change(8);
		assertEquals(5, cashRegister.getMonetaryValue());
		assertEquals(8, change.getMonetaryValue());
		//System.out.println(change.toString());
		LOGGER.log( Level.INFO, change.toString());
	}
}
