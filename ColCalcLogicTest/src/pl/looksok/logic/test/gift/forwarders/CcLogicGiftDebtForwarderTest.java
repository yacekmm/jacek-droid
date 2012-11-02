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
	
	public void testDebtsObject(){
		giftReceivers.add(Constants.personCName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseFourPeopleGift(68, 0, 0, 0, giftReceivers, 0, 24, 0, 0);
		
		calc.calculate(inputPaysList);
		calc.recalculate();

		HashMap<String, Double> personADebts = calc.getPersonDebts(Constants.personAName);
		HashMap<String, Double> personBDebts = calc.getPersonDebts(Constants.personBName);
		HashMap<String, Double> personCDebts = calc.getPersonDebts(Constants.personCName);
		HashMap<String, Double> personDDebts = calc.getPersonDebts(Constants.personDName);
		
		HashMap<String, Double> personARefunds = calc.getPerson(Constants.personAName).getRefundsFromOtherPeople();
		HashMap<String, Double> personBRefunds = calc.getPerson(Constants.personBName).getRefundsFromOtherPeople();
		HashMap<String, Double> personCRefunds = calc.getPerson(Constants.personCName).getRefundsFromOtherPeople();
		HashMap<String, Double> personDRefunds = calc.getPerson(Constants.personDName).getRefundsFromOtherPeople();
//		HashMap<String, Double> personBRefunds = calc.setPersonRefundsFromOthers(Constants.personBName);
//		HashMap<String, Double> personCRefunds = calc.setPersonRefundsFromOthers(Constants.personCName);
//		HashMap<String, Double> personDRefunds = calc.setPersonRefundsFromOthers(Constants.personDName);
		
		//personA
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personADebts.size() == 0);
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 9.0, personARefunds.get(Constants.personBName));
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 17.0, personARefunds.get(Constants.personCName));
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 17.0, personARefunds.get(Constants.personDName));
		
		
//		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 5.0, personBDebts.get(Constants.personAName));
//		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 5.0, personCDebts.get(Constants.personAName));
//		
//		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personADebts.get(Constants.personBName));
//		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 20.0, personCDebts.get(Constants.personBName));
//		
//		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personADebts.get(Constants.personCName));
//		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personBDebts.get(Constants.personCName));
	}
}
