package pl.looksok.activity.calcresult;

import pl.looksok.R;
import pl.looksok.activity.addperson.AddPersonSingleRestaurant;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.TextBuilder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class CalcResultRestaurantActivity extends CalcResultBaseActivity {

	private TextView mErrorBoxTextView;

	@Override
	protected int getXmlLayout() {
		return R.layout.calc_result_restaurant;
	}
	
	@Override
	protected CalculationType getCalculationType()  {
		return CalculationType.RESTAURANT;
	}

	@Override
	protected void initButtonsStyles() {
		findViewById(R.id.calc_addPerson_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
		findViewById(R.id.calc_addMultiPerson_button).setVisibility(View.GONE);
		findViewById(R.id.calc_saveCalculation_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
	}

	@Override
	protected Class<?> getAddPersonSingleActivity()  {
		return AddPersonSingleRestaurant.class;
	}

	@Override
	protected void initViews() {
		super.initViews();
		mErrorBoxTextView = (TextView)findViewById(R.id.calc_badInputData_info);
	}

	@Override
	protected void handleException(Exception exception) {
		super.handleException(exception);

		if(exception instanceof BadInputDataException){
			mErrorBoxTextView = (TextView)findViewById(R.id.calc_badInputData_info);
			mErrorBoxTextView.setText(TextBuilder.buildBadInputDataErrorMessage(getApplicationContext(), calc));
			mErrorBoxTextView.setVisibility(View.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_in_slow);
			mErrorBoxTextView.startAnimation(animation);
		}
	}
}
