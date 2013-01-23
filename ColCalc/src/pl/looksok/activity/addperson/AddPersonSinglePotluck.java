package pl.looksok.activity.addperson;


import pl.looksok.R;
import pl.looksok.activity.addperson.utils.OnTotalPayChangeListener;
import pl.looksok.activity.calcresult.CalcResultPotluckActivity;

public class AddPersonSinglePotluck extends AddPersonSingleBase implements OnTotalPayChangeListener {
	
	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_person_single_potluck;
	}
	
	@Override
	protected Class<?> getCalcResultActivity()  {
		return CalcResultPotluckActivity.class;
	}

	@Override
	protected Class<?> getAddNewPersonSingleActivity()  {
		return AddPersonSinglePotluck.class;
	}
}