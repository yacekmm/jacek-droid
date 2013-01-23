package pl.looksok.activity.calcresult;

import pl.looksok.R;
import pl.looksok.activity.addperson.AddPersonSinglePotluck;
import pl.looksok.logic.CalculationType;


public class CalcResultPotluckActivity extends CalcResultBaseActivity {
	protected static final String LOG_TAG = CalcResultPotluckActivity.class.getSimpleName();

	@Override
	protected int getXmlLayout() {
		return R.layout.calc_result_potluck;
	}
	
	@Override
	protected CalculationType getCalculationType()  {
		return CalculationType.POTLUCK_PARTY_WITH_GIFT_V2;
	}
	
	@Override
	protected Class<?> getAddPersonSingleActivity()  {
		return AddPersonSinglePotluck.class;
	}
}
