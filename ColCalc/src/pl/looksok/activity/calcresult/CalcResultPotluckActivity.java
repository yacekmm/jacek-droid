package pl.looksok.activity.calcresult;

import pl.looksok.R;
import pl.looksok.activity.addperson.AddPersonSinglePotluck;
import pl.looksok.logic.CalculationType;

import com.google.analytics.tracking.android.EasyTracker;


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
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
}
