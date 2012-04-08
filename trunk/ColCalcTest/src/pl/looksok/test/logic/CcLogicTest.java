package pl.looksok.test.logic;

import java.util.HashMap;
import java.util.Hashtable;

import junit.framework.TestCase;
import pl.looksok.exception.BadPayException;
import pl.looksok.exception.BadPeopleCountException;
import pl.looksok.logic.CcLogic;
import pl.looksok.logic.PeoplePays;

public class CcLogicTest extends TestCase {

	private static final String INCORRECT_CALCULATION_BETWEEN_TWO_PEOPLE = "Incorrect calculation between two people.";
	private static final String INCORRECT_TO_RETURN = "Incorrect amount to return for this person.";
	private static final String INCORRECT_REFUND = "Incorrect refund for person.";
	private static final String SHOULD_THROW_EXCEPTION = "Should throw Exception: ";
	private static final String INCORRECT_PAYMENT_PER_PERSON = "Resulted in incorrect payment per person.";
	
	private CcLogic calc;
	private HashMap<String, Double> inputPays;
	private String personAName = "personA";
	private String personBName = "personB";
	private String personCName = "personC";

	public CcLogicTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CcLogic();
		inputPays = new HashMap<String, Double>();
		super.setUp();
	}
	
	public void testConstructor(){
		assertNotNull(calc);
	}
	
	public void testZeroPayForOnePerson(){
		double onePersonZeroPay = calc.howMuchPerPerson(0, 1);
		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 0.0, onePersonZeroPay);
	}
	
	public void testNonZeroPayForOnePerson(){
		double onePersonNonZeroPay = calc.howMuchPerPerson(10, 1);
		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 10.0, onePersonNonZeroPay);
	}
	
	public void testZeroPayForFewPeople(){
		double twoPeopleZeroPay = calc.howMuchPerPerson(0, 1);
		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 0.0, twoPeopleZeroPay);
	}
	
	public void testNonZeroEqualPayFewPeople(){
		double twoPeopleNonZeroPay = calc.howMuchPerPerson(10, 2);
		assertEquals(INCORRECT_PAYMENT_PER_PERSON, 5.0, twoPeopleNonZeroPay);
	}
	
	public void testExceptionOnErrorPay(){
		try{
			calc.howMuchPerPerson(-1, 1);
			fail(SHOULD_THROW_EXCEPTION + BadPayException.class.getSimpleName());
		}catch(BadPayException e){}
	}

	public void testExceptionOnErrorPeopleCount(){
		try{
			calc.howMuchPerPerson(1, -1);
			fail(SHOULD_THROW_EXCEPTION + BadPeopleCountException.class.getSimpleName());
		}catch(BadPeopleCountException e){}
	}
	
	
	public void testRefundOfZeroPayOnePerson(){
		inputPays.put(personAName, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPays);
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePerson(){
		inputPays.put(personAName, 10.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPays);
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfZeroPayFewPeople(){
		buildTestCaseTwoPeople(0.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPays);
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personAName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personBName).getTotalRefundForThisPerson());
	}
	
	public void testReturnOfNonZeroPayFewPeopleOnePaid(){
		buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPays);
		assertEquals(INCORRECT_TO_RETURN, 0.0, result.get(personAName).getToReturn());
		assertEquals(INCORRECT_TO_RETURN, 5.0, result.get(personBName).getToReturn());
	}
	
	public void testRefundOfNonZeroPayFewPeopleOnePaid(){
		buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPays);
		assertEquals(INCORRECT_REFUND, 5.0, result.get(personAName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personBName).getTotalRefundForThisPerson());
	}


	
	public void testRefundOfNonZeroPayFewPeopleFewPaid(){
		buildTestCaseThreePeople(10.0, 5.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPays);
		assertEquals(INCORRECT_REFUND, 5.0, result.get(personAName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personBName).getTotalRefundForThisPerson());
		assertEquals(INCORRECT_REFUND, 0.0, result.get(personCName).getTotalRefundForThisPerson());
		
		assertEquals(INCORRECT_TO_RETURN, 0.0, result.get(personAName).getToReturn());
		assertEquals(INCORRECT_TO_RETURN, 0.0, result.get(personBName).getToReturn());
		assertEquals(INCORRECT_TO_RETURN, 5.0, result.get(personCName).getToReturn());
	}
	
	public void testThrowBadPayException(){
		try{
			inputPays.put(personAName, -10.0);
			inputPays.put(personBName, 0.0);
			
			calc.calculate(inputPays);
			fail(SHOULD_THROW_EXCEPTION + BadPayException.class.getSimpleName());
		}catch(BadPayException e){}
	}
	
	public void testRefundOfZeroPayTwoPeopleOnePaidWhoToWhom(){
		buildTestCaseTwoPeople(0.0, 0.0);
		
		calc.calculate(inputPays);
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO_PEOPLE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO_PEOPLE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleOnePaidWhoToWhom(){
		buildTestCaseTwoPeople(10.0, 0.0);
		
		calc.calculate(inputPays);
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO_PEOPLE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO_PEOPLE, 5.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleOnePaidWhoToWhomV2(){
		buildTestCaseTwoPeople(10.0, 2.0);
		
		calc.calculate(inputPays);
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO_PEOPLE, 0.0, calc.howMuchPersonAGivesBackToPersonB(personAName, personBName));
		assertEquals(INCORRECT_CALCULATION_BETWEEN_TWO_PEOPLE, 4.0, calc.howMuchPersonAGivesBackToPersonB(personBName, personAName));
	}
	
	//howMuchOneGivesBackToAnother(personAName, personBName) should throw exception if not yet calculated
	
	private void buildTestCaseTwoPeople(double paymentA, double paymentB) {
		inputPays.put(personAName, paymentA);
		inputPays.put(personBName, paymentB);
	}
	
	private void buildTestCaseThreePeople(double paymentA, double paymentB, double paymentC) {
		inputPays.put(personAName, paymentA);
		inputPays.put(personBName, paymentB);
		inputPays.put(personCName, paymentC);
	}
}
