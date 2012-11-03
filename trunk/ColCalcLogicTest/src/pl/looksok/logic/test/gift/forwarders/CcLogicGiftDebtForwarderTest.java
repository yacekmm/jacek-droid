package pl.looksok.logic.test.gift.forwarders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.test.utils.Constants;
import pl.looksok.logic.test.utils.GiftTestScenarioBuilder;

public class CcLogicGiftDebtForwarderTest extends TestCase {

	private CalculationLogic calc;
	private List<PersonData> inputPaysList;
	private boolean equalPayments = true;
	private HashSet<String> giftReceivers;

	public CcLogicGiftDebtForwarderTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CalculationLogic();
		calc.setEqualPayments(equalPayments);
		calc.setCalculationType(CalculationType.POTLUCK_PARTY_WITH_GIFT);
		inputPaysList = new ArrayList<PersonData>();
		giftReceivers = new HashSet<String>();
		super.setUp();
	}
	
	public void testConstructor(){
		assertNotNull(calc);
	}
	
	public void testForwardedRefunds(){
		giftReceivers.add(Constants.personCName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseFourPeopleGift(68, 0, 0, 0, giftReceivers, 0, 24, 0, 0);
		
		calc.calculate(inputPaysList);
		calc.recalculate();

		HashMap<String, Double> personADebts = calc.getPersonDebts(Constants.personAName);
		HashMap<String, Double> personBDebts = calc.getPersonDebts(Constants.personBName);
		
		HashMap<String, Double> personARefunds = calc.getPersonRefunds(Constants.personAName);
		HashMap<String, Double> personBRefunds = calc.getPersonRefunds(Constants.personBName);
		
		//personA
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personADebts.size() == 0);
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personARefunds.size() == 3);
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 1.0, personARefunds.get(Constants.personBName));
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 17.0, personARefunds.get(Constants.personCName));
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 25.0, personARefunds.get(Constants.personDName));
		
		//personB
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personBDebts.size() == 1);
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personBRefunds.size() == 0);
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 1.0, personBDebts.get(Constants.personAName));
	}
}
