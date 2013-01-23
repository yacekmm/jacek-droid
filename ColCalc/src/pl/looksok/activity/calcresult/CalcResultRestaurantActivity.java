package pl.looksok.activity.calcresult;

import pl.looksok.R;
import pl.looksok.logic.CalculationType;
import android.view.View;


public class CalcResultRestaurantActivity extends CalcResultBaseActivity {

	@Override
	protected int getXmlLayout() {
		return R.layout.calc_result_restaurant;
	}
	
	@Override
	protected CalculationType getCalculationType()  {
		return CalculationType.NOT_EQUAL_PAYMENTS;
	}

	@Override
	protected void initButtonsStyles() {
		findViewById(R.id.calc_addPerson_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
		findViewById(R.id.calc_addMultiPerson_button).setVisibility(View.GONE);
		findViewById(R.id.calc_saveCalculation_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
	}
}
