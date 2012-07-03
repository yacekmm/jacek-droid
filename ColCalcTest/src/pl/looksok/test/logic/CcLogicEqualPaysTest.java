package pl.looksok.test.logic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;
import pl.looksok.test.utils.Constants;
import pl.looksok.test.utils.TestScenarioBuilder;
import pl.looksok.utils.exceptions.BadPayException;
import pl.looksok.utils.exceptions.DuplicatePersonNameException;
import pl.looksok.utils.exceptions.PaysNotCalculatedException;

public class CcLogicEqualPaysTest extends TestCase {

	private CalculationLogic calc;
	private List<PersonData> inputPaysList;
	private boolean equalPayments = true;

	public CcLogicEqualPaysTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CalculationLogic("SampleTitle");
		calc.setEqualPayments(equalPayments);
		inputPaysList = new ArrayList<PersonData>();
		super.setUp();
	}
	
	public void testConstructor(){
		assertNotNull(calc);
	}
	
	public void testRefundOfZeroPayOnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCaseOnePerson(0.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCaseOnePerson(10.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfZeroPayFewPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(0.0, 0.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void testReturnOfNonZeroPayFewPeopleOnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personBName).getToReturn());
	}
	
	public void testRefundOfNonZeroPayFewPeopleOnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void testRefundOfNonZeroPayFewPeopleFewPaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(10.0, 5.0, 0.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personCName).getTotalRefundForThisPerson());
		
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personBName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personCName).getToReturn());
	}
	
	public void testThrowBadPayException(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(-10.0, 0.0);
			
			calc.calculate(inputPaysList);
			fail(Constants.SHOULD_THROW_EXCEPTION + BadPayException.class.getSimpleName());
		}catch(BadPayException e){}
	}
	
	public void testRefundOfZeroPayTwoPeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(0.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleTwoPaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 2.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 4.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(15.0, 0.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhomV2(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(0.0, 0.0, 15.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(12.0, 3.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleThreePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(11.0, 3.0, 4.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(9.0, 9.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhomV2(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(0.0, 9.0, 9.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidNotEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(18.0, 6.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 8.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayFourPeopleThreePaidNotEquallyWhoToWhom(){
		//each should pay 116/4 = 29.0
		inputPaysList = TestScenarioBuilder.buildTestCaseFourPeople(55.0, 36.0, 0.0, 25.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personDName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 26.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personDName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personDName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 4.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personCName));
	}
	
	public void testNonZeroPaySixPeoplePaidNotEquallyWhoToWhom(){
		//each should pay 126/6 = 21.0
		inputPaysList = TestScenarioBuilder.buildTestCaseSixPeople(26.0, 0.0, 15.0, 85.0, 0.0, 0.0);
		
		calc.calculate(inputPaysList);
		
//		System.out.println("B to C: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
//		System.out.println("B to A: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
//		System.out.println("B to D: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personDName));
//		System.out.println("B to E: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personEName));
//		System.out.println("B to F: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personFName));
//		
//		System.out.println("C to B: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
//		System.out.println("C to A: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
//		System.out.println("C to D: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personDName));
//		System.out.println("C to E: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personEName));
//		System.out.println("C to F: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personFName));
//		
//		System.out.println("A to B: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
//		System.out.println("A to C: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
//		System.out.println("A to D: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personDName));
//		System.out.println("A to E: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personEName));
//		System.out.println("A to F: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personFName));
//		
//		System.out.println("D to A: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personAName));
//		System.out.println("D to B: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personBName));
//		System.out.println("D to C: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personCName));
//		System.out.println("D to E: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personEName));
//		System.out.println("D to F: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personFName));
//		
//		System.out.println("E to A: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personAName));
//		System.out.println("E to B: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personBName));
//		System.out.println("E to C: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personCName));
//		System.out.println("E to D: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personDName));
//		System.out.println("E to F: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personFName));
//		
//		System.out.println("F to A: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personAName));
//		System.out.println("F to B: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personBName));
//		System.out.println("F to C: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personCName));
//		System.out.println("F to D: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personDName));
//		System.out.println("F to E: " + calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personEName));
//		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 21.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personDName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personEName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personFName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 6.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personDName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personEName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personFName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personDName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personEName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personFName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personEName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personFName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 21.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personDName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personEName, Constants.personFName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 16.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personDName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_SIX, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personFName, Constants.personEName));
	}
	
	public void testThrowExceptionIfNotCalculated(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(9.0, 9.0, 0.0);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + PaysNotCalculatedException.class.getSimpleName());
		}catch (PaysNotCalculatedException e){}
	}
	
	public void testThrowExceptionIfDuplicatePersonName(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(Constants.personAName, 9.0, 
					Constants.personAName, 9.0, 
					Constants.personCName, 0.0);
			calc.calculate(inputPaysList);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + DuplicatePersonNameException.class.getSimpleName());
		}catch (DuplicatePersonNameException e){}
	}
}
