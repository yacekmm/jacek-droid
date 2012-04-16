package pl.looksok.test.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.exception.BadPayException;
import pl.looksok.exception.DuplicatePersonNameException;
import pl.looksok.exception.PaysNotCalculatedException;
import pl.looksok.logic.CcLogic;
import pl.looksok.logic.InputData;
import pl.looksok.logic.PeoplePays;

public class CcLogicTest extends TestCase {

	private static final String INCORRECT_CALCULATION_BETWEEN_TWO = "Incorrect calculation between two people.";
	private static final String INCORRECT_TO_RETURN = "Incorrect amount to return for this person.";
	private static final String INCORRECT_REFUND = "Incorrect refund for person.";
	private static final String SHOULD_THROW_EXCEPTION = "Should throw Exception: ";
//	private static final String INCORRECT_PAYMENT_PER_PERSON = "Resulted in incorrect payment per person.";
	private static final String INCORRECT_CALCULATION_BETWEEN_THREE = "Incorrect calculation between three people.";
	
	private CcLogic calc;
	private HashMap<String, Double> inputPays;
	private List<InputData> inputPaysList;
	private String personAName = "personA";
	private String personBName = "personB";
	private String personCName = "personC";

	public CcLogicTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CcLogic();
		inputPays = new HashMap<String, Double>();
		inputPaysList = new ArrayList<InputData>();
		super.setUp();
	}
	
	public void testConstructor(){
		assertNotNull(calc);
	}
//	
//	public void testZeroPayForOnePerson(){
//		double onePersonZeroPay = calc.howMuchPerPerson(0, 1);
//		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 0.0, onePersonZeroPay);
//	}
//	
//	public void testNonZeroPayForOnePerson(){
//		double onePersonNonZeroPay = calc.howMuchPerPerson(10, 1);
//		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 10.0, onePersonNonZeroPay);
//	}
//	
//	public void testZeroPayForFewPeople(){
//		double twoPeopleZeroPay = calc.howMuchPerPerson(0, 1);
//		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 0.0, twoPeopleZeroPay);
//	}
//	
//	public void testNonZeroEqualPayFewPeople(){
//		double twoPeopleNonZeroPay = calc.howMuchPerPerson(10, 2);
//		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 5.0, twoPeopleNonZeroPay);
//	}
//	
//	public void testExceptionOnErrorPay(){
//		try{
//			calc.howMuchPerPerson(-1, 1);
//			fail(SHOULD_THROW_EXCEPTION + BadPayException.class.getSimpleName());
//		}catch(BadPayException e){}
//	}
//
//	public void testExceptionOnErrorPeopleCount(){
//		try{
//			calc.howMuchPerPerson(1, -1);
//			fail(SHOULD_THROW_EXCEPTION + BadPeopleCountException.class.getSimpleName());
//		}catch(BadPeopleCountException e){}
//	}
	
	
	public void testRefundOfZeroPayOnePerson(){
		buildTestCaseOnePerson(0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePerson(){
		buildTestCaseOnePerson(10.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfZeroPayFewPeople(){
		buildTestCaseTwoPeople(0.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personAName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personBName).getTotalRefundForThisPerson());
	}
	
	public void testReturnOfNonZeroPayFewPeopleOnePaid(){
		buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(INCORRECT_TO_RETURN, 0.0, result.get(personAName).getToReturn());
		assertEquals(INCORRECT_TO_RETURN, 5.0, result.get(personBName).getToReturn());
	}
	
	public void testRefundOfNonZeroPayFewPeopleOnePaid(){
		buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(INCORRECT_REFUND, 5.0, result.get(personAName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personBName).getTotalRefundForThisPerson());
	}


	
	public void testRefundOfNonZeroPayFewPeopleFewPaid(){
		buildTestCaseThreePeople(10.0, 5.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(INCORRECT_REFUND, 5.0, result.get(personAName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personBName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personCName).getTotalRefundForThisPerson());
		
		assertEquals(INCORRECT_TO_RETURN, 0.0, result.get(personAName).getToReturn());
		assertEquals(INCORRECT_TO_RETURN, 0.0, result.get(personBName).getToReturn());
		assertEquals(INCORRECT_TO_RETURN, 5.0, result.get(personCName).getToReturn());
	}
	
	public void testThrowBadPayException(){
		try{
			buildTestCaseTwoPeople(-10.0, 0.0);
			
			calc.calculate(inputPaysList);
			fail(SHOULD_THROW_EXCEPTION + BadPayException.class.getSimpleName());
		}catch(BadPayException e){}
	}
	
	public void testRefundOfZeroPayTwoPeopleOnePaidWhoToWhom(){
		buildTestCaseTwoPeople(0.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleOnePaidWhoToWhom(){
		buildTestCaseTwoPeople(10.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO, 5.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleTwoPaidWhoToWhom(){
		buildTestCaseTwoPeople(10.0, 2.0);
		
		calc.calculate(inputPaysList);
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO, 4.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhom(){
		buildTestCaseThreePeople(15.0, 0.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personBName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personCName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhomV2(){
		buildTestCaseThreePeople(0.0, 0.0, 15.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personBName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personCName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidWhoToWhom(){
		buildTestCaseThreePeople(12.0, 3.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personBName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personCName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleThreePaidWhoToWhom(){
		buildTestCaseThreePeople(11.0, 3.0, 4.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personBName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personCName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhom(){
		buildTestCaseThreePeople(9.0, 9.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personBName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personCName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhomV2(){
		buildTestCaseThreePeople(0.0, 9.0, 9.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personBName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personCName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidNotEquallyWhoToWhom(){
		buildTestCaseThreePeople(18.0, 6.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personCName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 8.0, calc.howMuchPersonAGivesBackToPersonB(personCName, personAName));
		
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personCName));
	}
	
	public void testThrowExceptionIfNotCalculated(){
		try{
			buildTestCaseThreePeople(9.0, 9.0, 0.0);
			calc.howMuchPersonAGivesBackToPersonB(personBName, personAName);
			fail(SHOULD_THROW_EXCEPTION + PaysNotCalculatedException.class.getSimpleName());
		}catch (PaysNotCalculatedException e){}
	}
	
	public void testThrowExceptionIfDuplicatePersonName(){
		try{
			personBName = personAName;
			buildTestCaseThreePeople(9.0, 9.0, 0.0);
			calc.calculate(inputPaysList);
			calc.howMuchPersonAGivesBackToPersonB(personBName, personAName);
			fail(SHOULD_THROW_EXCEPTION + DuplicatePersonNameException.class.getSimpleName());
		}catch (DuplicatePersonNameException e){}
	}
	
	private void buildTestCaseOnePerson(double paymentA) {
		inputPays.put(personAName, paymentA);
		
		inputPaysList.add(new InputData(personAName, paymentA));
	}
	
	private void buildTestCaseTwoPeople(double paymentA, double paymentB) {
		inputPays.put(personAName, paymentA);
		inputPays.put(personBName, paymentB);
		
		inputPaysList.add(new InputData(personAName, paymentA));
		inputPaysList.add(new InputData(personBName, paymentB));
	}
	
	private void buildTestCaseThreePeople(double paymentA, double paymentB, double paymentC) {
		inputPays.put(personAName, paymentA);
		inputPays.put(personBName, paymentB);
		inputPays.put(personCName, paymentC);
		
		inputPaysList.add(new InputData(personAName, paymentA));
		inputPaysList.add(new InputData(personBName, paymentB));
		inputPaysList.add(new InputData(personCName, paymentC));
	}
}
