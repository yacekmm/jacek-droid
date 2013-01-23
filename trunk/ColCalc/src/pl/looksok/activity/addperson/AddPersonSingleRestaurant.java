package pl.looksok.activity.addperson;


import java.util.HashSet;

import pl.looksok.R;
import pl.looksok.activity.addperson.utils.InputValidator;
import pl.looksok.activity.addperson.utils.OnTotalPayChangeListener;
import pl.looksok.activity.calcresult.CalcResultRestaurantActivity;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
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

	@Override
	protected HashSet<PersonData> getNewInputDataToAdd()
			throws BadInputDataException {

		HashSet<PersonData> personDataSet = new HashSet<PersonData>();
		String name = mNewPersonNameInput.getText().toString();
		boolean receivesGift = false;
		boolean buysGift = false;
		double giftPayment = 0.0;

		if(!buysGift)
			giftPayment = 0;

		if(!InputValidator.inputIsValid(getApplicationContext(), name, adapter.getTotalPay(), calc.isEqualPayments(), inputPaysList))
			throw new BadInputDataException();

		calc.setCalculationType(CalculationType.RESTAURANT);
		personDataSet.add(new PersonData(name, adapter.getItems(), emails, receivesGift, giftPayment));

		return personDataSet;
	}
	
}