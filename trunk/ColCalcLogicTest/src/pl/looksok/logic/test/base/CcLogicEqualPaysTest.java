package pl.looksok.logic.test.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.exceptions.DuplicatePersonNameException;
import pl.looksok.logic.exceptions.PaysNotCalculatedException;
import pl.looksok.logic.test.utils.Constants;
import pl.looksok.logic.test.utils.TestScenarioBuilder;

public class CcLogicEqualPaysTest extends TestCase {

	private CalculationLogic calc;
	private List<PersonData> inputPaysList;

	public CcLogicEqualPaysTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CalculationLogic();
		calc.setCalculationType(CalculationType.POTLUCK_PARTY_WITH_GIFT_V2);
		inputPaysList = new ArrayList<PersonData>();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		calc = null;
		inputPaysList = null;
		super.tearDown();
	}
	
	
	public void test_Constructor(){
		assertNotNull(calc);
	}
	
	public void test_RefundOfZeroPay_OnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCase_OnePerson(0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void test_RefundOfNonZeroPay_OnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCase_OnePerson(10.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void test_RefundOfZeroPay_FewPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(0.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void test_ReturnOfNonZeroPay_FewPeople_OnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(10.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personBName).getToReturn());
	}
	
	public void test_RefundOfNonZeroPay_FewPeople_OnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(10.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void test_RefundOfNonZeroPay_FewPeople_FewPaid(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(10.0, 5.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personCName).getTotalRefundForThisPerson());
		
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personBName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personCName).getToReturn());
	}
	
	public void test_ThrowBadPayException(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(-10.0, 0.0);
			
			calc.calculate(inputPaysList);
			fail(Constants.SHOULD_THROW_EXCEPTION + BadInputDataException.class.getSimpleName());
		}catch(BadInputDataException e){}
	}
	
	public void test_RefundOfZeroPay_TwoPeople_OnePaid_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(0.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void test_RefundOfNonZeroPay_TwoPeople_OnePaid_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(10.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void test_RefundOfNonZeroPay_TwoPeople_TwoPaid_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(10.0, 2.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 4.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void test_RefundOfNonZeroPay_ThreePeople_OnePaid_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(15.0, 0.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void test_RefundOfNonZeroPay_ThreePeople_OnePaid_WhoToWhom_V2(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(0.0, 0.0, 15.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void test_RefundOfNonZeroPay_ThreePeople_TwoPaid_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(12.0, 3.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void test_RefundOfNonZeroPay_ThreePeople_ThreePaid_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(11.0, 3.0, 4.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void test_RefundOfNonZeroPay_ThreePeople_TwoPaidEqually_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(9.0, 9.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void test_RefundOfNonZeroPay_ThreePeople_TwoPaid_Equally_WhoToWhom_V2(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(0.0, 9.0, 9.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void test_RefundOfNonZeroPay_ThreePeople_TwoPaid_NotEqually_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(18.0, 6.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 8.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
	}
	
	public void test_RefundOfZeroPay_TwoPeople_OnePaid_NotEqually_WhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople(18.9, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 9.45, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void test_Calculation_FourPeople_ThreePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCase_FourPeople(13, 0, 50, 50);
		
		calc.calculate(inputPaysList);
		
		HashMap<String, Double> personADebts = calc.getPersonDebts(Constants.personAName);
		HashMap<String, Double> personBDebts = calc.getPersonDebts(Constants.personBName);
		HashMap<String, Double> personCDebts = calc.getPersonDebts(Constants.personCName);
		HashMap<String, Double> personDDebts = calc.getPersonDebts(Constants.personDName);
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personBDebts.get(Constants.personAName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personCDebts.get(Constants.personAName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personDDebts.get(Constants.personAName));
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personADebts.get(Constants.personBName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personCDebts.get(Constants.personBName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personDDebts.get(Constants.personBName));
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 15.25, personADebts.get(Constants.personCName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 6.5, personBDebts.get(Constants.personCName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personDDebts.get(Constants.personCName));
		
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personADebts.get(Constants.personDName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, 21.75, personBDebts.get(Constants.personDName));
		assertEquals(Constants.INCORRECT_RETURN_OBJECT_VALUE, null, personCDebts.get(Constants.personDName));
		
	}
	
	public void test_ThrowException_IfNotCalculated(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople(9.0, 9.0, 0.0);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + PaysNotCalculatedException.class.getSimpleName());
		}catch (PaysNotCalculatedException e){}
	}
	
	public void test_ThrowException_IfDuplicatePersonName(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCase_ThreePeople_WithNames(Constants.personAName, 9.0, 
					Constants.personAName, 9.0, 
					Constants.personCName, 0.0);
			calc.calculate(inputPaysList);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + DuplicatePersonNameException.class.getSimpleName());
		}catch (DuplicatePersonNameException e){}
	}

}
