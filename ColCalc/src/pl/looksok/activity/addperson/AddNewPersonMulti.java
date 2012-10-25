package pl.looksok.activity.addperson;

import java.util.HashSet;
import java.util.List;

import pl.looksok.R;
import pl.looksok.currencyedittext.CurrencyEditText;
import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.utils.PersonDataUtils;
import pl.looksok.utils.Constants;
import pl.looksok.utils.FormatterHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class AddNewPersonMulti extends AddNewPersonBase {

	private LinearLayout mHowManyPersonsScroller;
	private Button mHowManyPersonsSelectedBtn;
	private CurrencyEditText mNewPersonPayInput;

	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_new_multi_person;
	}

	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		
		mNewPersonPayInput = (CurrencyEditText)findViewById(R.id.enterPaysMulti_EditText_Pay);
		mHowManyPersonsScroller = (LinearLayout)findViewById(R.id.enterPaysMulti_peopleCount_content);
		for(int i=Constants.MULTI_PERSON_MIN_COUNT; i<Constants.MULTI_PERSON_MAX_COUNT; i++){
			Button b = new Button(getApplicationContext());
			b.setLayoutParams(
					new LayoutParams(
							getResources().getDimensionPixelSize(R.dimen.height_button),
							LayoutParams.FILL_PARENT));
			//FIXME: set button pressed
//			b.setBackgroundResource(R.drawable.button_background_gray_active);
			if(i==Constants.MULTI_PERSON_DEFAULT_COUNT){
				b.setBackgroundColor(getResources().getColor(R.color.green_kotak));
				mHowManyPersonsSelectedBtn = b;
			}
			b.setOnClickListener(setPeopleCountOnClickListener);
			mHowManyPersonsScroller.addView(b);
			b.setText("" + i);
		}
	}

	private OnClickListener setPeopleCountOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//FIXME: set button pressed
//			mHowManyPersonsSelectedBtn.setBackgroundResource(R.drawable.button_background_gray_active);
			mHowManyPersonsSelectedBtn = (Button)v;
			mHowManyPersonsSelectedBtn.setBackgroundColor(getResources().getColor(R.color.green_kotak));
		}
	};


	@Override
	protected HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException {
		HashSet<PersonData> personDataSet = new HashSet<PersonData>();
		double payDouble = FormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
		double howManyPersons = Integer.parseInt(mHowManyPersonsSelectedBtn.getText().toString());
		String namePrefix = getString(R.string.enterPaysMulti_personPrefix);

		int nameOffset = 0;
		for(int i = 1; i<=howManyPersons; i++){
			String name = namePrefix + " " + (i + nameOffset);
			while(inputListContainsPerson(name)){
				nameOffset++;
				name = namePrefix + " " + (i + nameOffset);
			}

			String atomPayName = new StringBuilder(getString(R.string.EnterPays_atomPay_defaultName)).append(" ").append(i).toString();
			List<AtomPayment> atomPays = PersonDataUtils.getDefaultAtomPaymentsList(atomPayName, payDouble);
			personDataSet.add(new PersonData(name, atomPays, emails));
		}
		return personDataSet;
	}

	private boolean inputListContainsPerson(String personName){
		for (PersonData pd : inputPaysList) {
			if(pd.getName().equals(personName))
				return true;
		}
		return false;
	}
	

	@Override
	public void onBackPressed() {
		calculateAndShowResults();
	}
}
