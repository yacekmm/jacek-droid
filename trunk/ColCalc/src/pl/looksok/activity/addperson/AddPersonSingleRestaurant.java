package pl.looksok.activity.addperson;


import java.util.HashSet;

import pl.looksok.R;
import pl.looksok.activity.addperson.utils.InputValidator;
import pl.looksok.activity.addperson.utils.OnTotalPayChangeListener;
import pl.looksok.activity.calcresult.CalcResultRestaurantActivity;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.CalcFormatterHelper;
import android.view.View;
import android.widget.EditText;

public class AddPersonSingleRestaurant extends AddPersonSingleBase implements OnTotalPayChangeListener {
	
	private EditText mHowMuchPaidEditText;

	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_person_single_restaurant;
	}

	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		mHowMuchPaidEditText = (EditText)findViewById(R.id.EnterPaysRest_howMuchPaid_value);
		mHowMuchPaidEditText.setOnKeyListener(hideKeyboardListener);
	}

	@Override
	protected void initButtonStyles() {
		findViewById(R.id.calc_addPerson_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
		findViewById(R.id.calc_addMultiPerson_button).setVisibility(View.GONE);
		findViewById(R.id.calc_saveCalculation_button).setBackgroundResource(R.drawable.button_bgnd_restaurant);
	}
	
	@Override
	protected void loadSpecificInputDataFromBundle(PersonData loadedPersonData) {
		super.loadSpecificInputDataFromBundle(loadedPersonData);
		mHowMuchPaidEditText.setText(CalcFormatterHelper.currencyFormat(loadedPersonData.getHowMuchPersonPaid(), 2));
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
		double shouldPayDouble = CalcFormatterHelper.readDoubleFromEditText(mHowMuchPaidEditText);

		if(!InputValidator.inputIsValid(getApplicationContext(), name, adapter.getTotalPay(), calc.isEqualPayments(), inputPaysList))
			throw new BadInputDataException();

		personDataSet.add(new PersonData(name, adapter.getItems(), shouldPayDouble, emails));

		return personDataSet;
	}
}