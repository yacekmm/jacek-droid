package pl.looksok.activity.addperson;


import pl.looksok.R;
import pl.looksok.activity.addperson.utils.OnTotalPayChangeListener;
import pl.looksok.activity.calcresult.CalcResultRestaurantActivity;
import android.view.View;

public class AddPersonSingleRestaurant extends AddPersonSingleBase implements OnTotalPayChangeListener {
	
	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_person_single_restaurant;
	}

	@Override
	protected void initButtonStyles() {
		findViewById(R.id.calc_addPerson_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
		findViewById(R.id.calc_addMultiPerson_button).setVisibility(View.GONE);
		findViewById(R.id.calc_saveCalculation_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
	}

	@Override
	protected Class<?> getCalcResultActivity()  {
		return CalcResultRestaurantActivity.class;
	}

	@Override
	protected Class<?> getAddNewPersonSingleActivity()  {
		return AddPersonSingleRestaurant.class;
	}
	
}