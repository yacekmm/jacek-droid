package pl.looksok.test.logic.gift.returnobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
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
	
//	public void testRefundOfZeroPayOnePersonNoGift(){
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(0.0, giftReceivers, 0.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//	}
//
//	public void testRefundOfZeroPayOnePersonGetsGiftNoValue(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(0.0, giftReceivers, 0.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//	}
//
//	public void testRefundOfZeroPayOnePersonGetsGift(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(0.0, giftReceivers, 10.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//	}
//	
//	public void testRefundOfNonZeroPayOnePersonNoGift(){
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(10.0, giftReceivers, 0.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//	}
//	
//	public void testRefundOfNonZeroPayOnePersonGetsGiftNoValue(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(10.0, giftReceivers, 0.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//	}
//	
//	public void testRefundOfNonZeroPayOnePersonGetsGift(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(10.0, giftReceivers, 20.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//	}
//	
//	public void testRefundOfZeroPayTwoPeopleNoGift(){
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseTwoPeopleGift(0.0, 0.0, giftReceivers, 0.0, 0.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
//	}
//	
//	public void testRefundOfZeroPayTwoPeopleGetsGift(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseTwoPeopleGift(0.0, 0.0, giftReceivers, 0.0, 10.0);
//		
//		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
//		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
//	}
//	
//	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhom(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 0.0, giftReceivers, 0.0, 30.0, 0.0);
//		
//		calc.calculate(inputPaysList);
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 15.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
//	}
//	
//	public void testRefundOfNonZeroPayThreePeopleTwoPaidForGift(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 0.0, giftReceivers, 0.0, 20.0, 10.0);
//		
//		calc.calculate(inputPaysList);
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
//	}
//
//	public void testRefundOfNonZeroPayThreePeopleTwoPaidForGiftTwoPotluck(){
//		giftReceivers.add(Constants.personAName);
//		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 30.0, giftReceivers, 0.0, 20.0, 10.0);
//		
//		calc.calculate(inputPaysList);
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
//		
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
//		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 10.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
//	}
	
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
