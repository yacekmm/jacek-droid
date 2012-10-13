package pl.looksok.test.logic.gift.returnobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.test.utils.Constants;
import pl.looksok.test.utils.GiftTestScenarioBuilder;

public class CcLogicCalcResultFormatTest extends TestCase {

	private CalculationLogic calc;
	private List<PersonData> inputPaysList;
	private boolean equalPayments = true;
	private HashSet<String> giftReceivers;

	public CcLogicCalcResultFormatTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CalculationLogic("SampleTitle");
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
		giftReceivers.add(Constants.personAName);
		giftReceivers.add(Constants.personBName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 0.0, giftReceivers, 0.0, 20.0, 10.0);
		
		calc.calculate(inputPaysList);
		
		HashMap<String, Double> personADebts = calc.getPersonDebts(Constants.personAName);
		HashMap<String, Double> personBDebts = calc.getPersonDebts(Constants.personBName);
		HashMap<String, Double> personCDebts = calc.getPersonDebts(Constants.personCName);
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 5.0, personBDebts.get(Constants.personAName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 5.0, personCDebts.get(Constants.personAName));
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personADebts.get(Constants.personBName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 20.0, personCDebts.get(Constants.personBName));
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personADebts.get(Constants.personCName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personBDebts.get(Constants.personCName));
	}
	
	public void testRefundsObject(){
		giftReceivers.add(Constants.personAName);
		giftReceivers.add(Constants.personBName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 0.0, giftReceivers, 0.0, 20.0, 10.0);
		
		calc.calculate(inputPaysList);
		
		HashMap<String, Double> personARefunds = calc.getPersonRefunds(Constants.personAName);
		HashMap<String, Double> personBRefunds = calc.getPersonRefunds(Constants.personBName);
		HashMap<String, Double> personCRefunds = calc.getPersonRefunds(Constants.personCName);
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 5.0, personARefunds.get(Constants.personBName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 5.0, personARefunds.get(Constants.personCName));
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personBRefunds.get(Constants.personAName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 20.0, personBRefunds.get(Constants.personCName));
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personCRefunds.get(Constants.personAName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personCRefunds.get(Constants.personBName));
	}
}
