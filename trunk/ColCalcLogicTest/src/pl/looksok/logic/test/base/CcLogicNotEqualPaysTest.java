package pl.looksok.logic.test.base;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.test.utils.Constants;
import pl.looksok.logic.test.utils.TestScenarioBuilder;

public class CcLogicNotEqualPaysTest extends TestCase {

	private CalculationLogic calc;
	private List<PersonData> inputPaysList;
	private boolean equalPayments = false;

	protected void setUp() throws Exception {
		calc = new CalculationLogic();
		calc.setEqualPayments(equalPayments);
		calc.setCalculationType(CalculationType.RESTAURANT);
		inputPaysList = new ArrayList<PersonData>();
		super.setUp();
	}
	
	public CcLogicNotEqualPaysTest(String name) {
		super(name);
	}
	
	public void testZeroPayTwoPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeopleVariousPays(0.0, 0.0, 
				0.0, 0.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void testEqualPayTwoPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeopleVariousPays(10.0, 5.0, 
				20.0, 25.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void testEqualPayTwoPeopleV2(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeopleVariousPays(3.0, 8.0, 
				7.0, 2.0);
		
		Hashtable<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 5.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void testCalculationOfNonZeroPayTwoPeopleTwoPaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeopleVariousPays(3.0, 8.0, 
				7.0, 2.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
}
