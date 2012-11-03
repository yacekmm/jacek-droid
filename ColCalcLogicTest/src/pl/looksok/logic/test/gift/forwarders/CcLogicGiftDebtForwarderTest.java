package pl.looksok.logic.test.gift.forwarders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;

public class CcLogicGiftDebtForwarderTest extends TestCase {

	private CalculationLogic calc;
	@SuppressWarnings("unused")
	private List<PersonData> inputPaysList;
	private boolean equalPayments = true;
	@SuppressWarnings("unused")
	private HashSet<String> giftReceivers;

	public CcLogicGiftDebtForwarderTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CalculationLogic();
		calc.setEqualPayments(equalPayments);
		calc.setCalculationType(CalculationType.POTLUCK_PARTY_WITH_GIFT);
		inputPaysList = new ArrayList<PersonData>();
		giftReceivers = new HashSet<String>();
		super.setUp();
	}
	
	public void testConstructor(){
		assertNotNull(calc);
	}
}
