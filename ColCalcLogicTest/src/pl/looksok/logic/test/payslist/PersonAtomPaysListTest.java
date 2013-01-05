package pl.looksok.logic.test.payslist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.test.utils.Constants;
import pl.looksok.logic.test.utils.TestScenarioBuilder;

public class PersonAtomPaysListTest extends TestCase {

	private List<PersonData> inputPays;
	
	@Override
	protected void setUp() throws Exception {
		inputPays = new ArrayList<PersonData>();
		super.setUp();
	}
	

	public void test1PersonAtomPaysList(){
		AtomPayment apA = new AtomPayment("piwo", 25);
		inputPays = TestScenarioBuilder.buildTestCase_OnePerson_AtomPays(apA);
		PersonData pd = inputPays.get(0);
		
		assertEquals(Constants.INCORRECT_ATOM_PAYMENTS_SUM, 25.0, pd.getPayMadeByPerson());
	}
	
	public void testPerson2AtomPaysList(){
		AtomPayment apA = new AtomPayment("piwo", 25);
		AtomPayment apB = new AtomPayment("jedzenie", 37);
		inputPays = TestScenarioBuilder.buildTestCase_OnePerson_AtomPays(apA, apB);
		PersonData pd = inputPays.get(0);
		
		assertEquals(Constants.INCORRECT_ATOM_PAYMENTS_SUM, 62.0, pd.getPayMadeByPerson());
	}
	
	public void testPerson2AtomPaysList_Exception(){
		AtomPayment apA = new AtomPayment("piwo", 25);
		AtomPayment apB = new AtomPayment("jedzenie", -37);
		
		try{
			List<AtomPayment> atomPays = new ArrayList<AtomPayment>();
			atomPays.add(apA);
			atomPays.add(apB);
			
			new PersonData(Constants.personAName, atomPays, new HashSet<String>());
			
			fail(Constants.SHOULD_THROW_EXCEPTION + BadInputDataException.class);
		}catch (BadInputDataException e) {}
	}
}
