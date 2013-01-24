package pl.looksok.logic.test.base;

import java.util.ArrayList;
import java.util.HashMap;
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

	protected void setUp() throws Exception {
		calc = new CalculationLogic();
		calc.setCalculationType(CalculationType.RESTAURANT);
		inputPaysList = new ArrayList<PersonData>();
		super.setUp();
	}

	public CcLogicNotEqualPaysTest(String name) {
		super(name);
	}

	public void test_ZeroPay_TwoPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople_VariousPays(0.0, 0.0, 
				0.0, 0.0);

		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void test_EqualPay_TwoPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople_VariousPays(10.0, 5.0, 
				20.0, 25.0);

		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void test_EqualPay_TwoPeople_V2(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople_VariousPays(3.0, 8.0, 
				7.0, 2.0);

		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND_UNEQUAL, 5.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void test_NonZeroPay_TwoPeople_TwoPaid(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople_VariousPays(3.0, 8.0, 
				7.0, 2.0);

		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void test_NotEqualPays_TwoPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCase_TwoPeople_VariousPays(
				10.0, 25.0, 
				20.0, 5.0);

		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 15.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}

	public void test_NotEqualPays_FourPeople(){
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
