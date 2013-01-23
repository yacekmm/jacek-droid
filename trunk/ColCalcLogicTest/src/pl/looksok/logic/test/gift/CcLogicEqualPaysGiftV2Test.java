package pl.looksok.logic.test.gift;

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
import pl.looksok.logic.test.utils.TestScenarioBuilder;

public class CcLogicEqualPaysGiftV2Test extends TestCase {

	private CalculationLogic calc;
	private List<PersonData> inputPaysList;
	private boolean equalPayments = true;
	private HashSet<String> giftReceivers;

	public CcLogicEqualPaysGiftV2Test() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CalculationLogic();
		calc.setEqualPayments(equalPayments);
		calc.setCalculationType(CalculationType.POTLUCK_PARTY_WITH_GIFT_V2);
		inputPaysList = new ArrayList<PersonData>();
		giftReceivers = new HashSet<String>();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		calc = null;
		inputPaysList = null;
		giftReceivers = null;
	}
	
	public void testConstructor(){
		assertNotNull(calc);
	}
	
	public void testRefundOfZeroPayOnePersonNoGift(){
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(0.0, giftReceivers, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}

	public void testRefundOfZeroPayOnePersonGetsGiftNoValue(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(0.0, giftReceivers, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}

	public void testRefundOfZeroPayOnePersonGetsGift(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(0.0, giftReceivers, 10.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePersonNoGift(){
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(10.0, giftReceivers, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePersonGetsGiftNoValue(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(10.0, giftReceivers, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePersonGetsGift(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseOnePersonGift(10.0, giftReceivers, 20.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfZeroPayTwoPeopleNoGift(){
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseTwoPeopleGift(0.0, 0.0, giftReceivers, 0.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfZeroPayTwoPeopleGetsGift(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseTwoPeopleGift(0.0, 0.0, giftReceivers, 0.0, 10.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhom(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 0.0, giftReceivers, 0.0, 30.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 10.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 10.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidForGift(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 0.0, giftReceivers, 0.0, 20.0, 10.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 10.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}

	public void testRefundOfNonZeroPayThreePeopleTwoPaidForGiftTwoPotluck(){
		giftReceivers.add(Constants.personAName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 30.0, giftReceivers, 0.0, 20.0, 10.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 10.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidForGiftTwoReceived(){
		giftReceivers.add(Constants.personAName);
		giftReceivers.add(Constants.personBName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseThreePeopleGift(15.0, 0.0, 0.0, giftReceivers, 0.0, 0.0, 10.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testLoopedRefunds(){
		giftReceivers.add(Constants.personCName);
		inputPaysList = GiftTestScenarioBuilder.buildTestCaseFourPeopleGift(68, 0, 0, 0, giftReceivers, 0, 24, 0, 0);
		
		calc.calculate(inputPaysList);

		HashMap<String, Double> personADebts = calc.getPersonDebts(Constants.personAName);
		HashMap<String, Double> personBDebts = calc.getPersonDebts(Constants.personBName);
		HashMap<String, Double> personCDebts = calc.getPersonDebts(Constants.personCName);
		HashMap<String, Double> personDDebts = calc.getPersonDebts(Constants.personDName);
		
		HashMap<String, Double> personARefunds = calc.getPersonRefunds(Constants.personAName);
		HashMap<String, Double> personBRefunds = calc.getPersonRefunds(Constants.personBName);
		HashMap<String, Double> personCRefunds = calc.getPersonRefunds(Constants.personCName);
		HashMap<String, Double> personDRefunds = calc.getPersonRefunds(Constants.personDName);
		
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
		
		//personC
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personCDebts.size() == 1);
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personCRefunds.size() == 0);
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 17.0, personCDebts.get(Constants.personAName));
		
		//personD
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personDDebts.size() == 1);
		assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personDRefunds.size() == 0);
		assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 25.0, personDDebts.get(Constants.personAName));
	}
	
	public void testNotEqualPaysInsteadOfForwardedPaymentsFighting(){
		calc.setEqualPayments(false);
		calc.setCalculationType(CalculationType.NOT_EQUAL_PAYMENTS);
		
			inputPaysList = TestScenarioBuilder.buildTestCase_FourPeople_VariousPays(
					68.0, 25.0, 
					24.0, 25.0,
					0.0, 17.0,
					0.0, 25.0 );
			
			calc.calculate(inputPaysList);
			
			HashMap<String, Double> personADebts = calc.getPersonDebts(Constants.personAName);
			HashMap<String, Double> personBDebts = calc.getPersonDebts(Constants.personBName);
			HashMap<String, Double> personCDebts = calc.getPersonDebts(Constants.personCName);
			HashMap<String, Double> personDDebts = calc.getPersonDebts(Constants.personDName);
			
			HashMap<String, Double> personARefunds = calc.getPersonRefunds(Constants.personAName);
			HashMap<String, Double> personBRefunds = calc.getPersonRefunds(Constants.personBName);
			HashMap<String, Double> personCRefunds = calc.getPersonRefunds(Constants.personCName);
			HashMap<String, Double> personDRefunds = calc.getPersonRefunds(Constants.personDName);
			
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
			
			//personC
			assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personCDebts.size() == 1);
			assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personCRefunds.size() == 0);
			assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 17.0, personCDebts.get(Constants.personAName));
			
			//personD
			assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personDDebts.size() == 1);
			assertTrue(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, personDRefunds.size() == 0);
			assertEquals(Constants.INCORRECT_FORWARD_PAYMENT_VALUE, 25.0, personDDebts.get(Constants.personAName));
	}
}
