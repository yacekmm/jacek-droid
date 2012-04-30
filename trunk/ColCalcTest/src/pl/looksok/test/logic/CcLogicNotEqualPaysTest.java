package pl.looksok.test.logic;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CcLogic;
import pl.looksok.logic.InputData;

public class CcLogicNotEqualPaysTest extends TestCase {

	private CcLogic calc;
	private List<InputData> inputPaysList;

	protected void setUp() throws Exception {
		calc = new CcLogic();
		inputPaysList = new ArrayList<InputData>();
		super.setUp();
	}
	
	public CcLogicNotEqualPaysTest(String name) {
		super(name);
	}

}
