package pl.looksok.activity.addperson;

import java.util.HashSet;
import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.calcresult.CalcResultPotluckActivity;
import pl.looksok.currencyedittext.CurrencyEditText;
import pl.looksok.currencyedittext.utils.FormatterHelper;
import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.utils.PersonDataUtils;
import pl.looksok.utils.CalcFormatterHelper;
import pl.looksok.utils.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class AddPersonMultiPotluck extends AddPersonBase {

	private LinearLayout mHowManyPersonsScroller;
	private Button mHowManyPersonsSelectedBtn;
	private CurrencyEditText mNewPersonPayInput;
	
	private int buttonBgndNormal = R.drawable.button_bgnd_gray;
	private int buttonBgndPressed = R.drawable.button_pressed;

	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_person_multi_potluck;
	}
	
	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		
		hideAddPersonButton();
		
		mNewPersonPayInput = (CurrencyEditText)findViewById(R.id.enterPaysMulti_EditText_Pay);
		mNewPersonPayInput.setOnKeyListener(hideKeyboardListener);
		mNewPersonPayInput.setText(FormatterHelper.currencyFormat(0.0));
		
		mHowManyPersonsScroller = (LinearLayout)findViewById(R.id.enterPaysMulti_peopleCount_content);

		initPeopleCountSpinner();
	}

	protected void initPeopleCountSpinner() {
		for(int i=Constants.MULTI_PERSON_MIN_COUNT; i<Constants.MULTI_PERSON_MAX_COUNT; i++){
			Button b = new Button(getApplicationContext());
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					getResources().getDimensionPixelSize(R.dimen.width_button),
					getResources().getDimensionPixelSize(R.dimen.height_button));
			layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_normal);
			b.setLayoutParams(layoutParams);
			
			b.setBackgroundResource(buttonBgndNormal);
			if(i==Constants.MULTI_PERSON_DEFAULT_COUNT){
				b.setBackgroundResource(buttonBgndPressed);
				mHowManyPersonsSelectedBtn = b;
			}
			b.setOnClickListener(setPeopleCountOnClickListener);
			mHowManyPersonsScroller.addView(b);
			b.setText("" + i);
			b.setTextAppearance(getApplicationContext(), R.style.textMediumDarkBold);
		}
	}

	protected void hideAddPersonButton() {
		findViewById(R.id.calc_addPerson_button).setVisibility(View.GONE);
		Button addPersonButton = (Button)findViewById(R.id.calc_addMultiPerson_button);
		addPersonButton.setBackgroundResource(R.drawable.button_bgnd_potluck);
		addPersonButton.setText(getString(R.string.addPerson_saveAndAddNextMultiPerson_button));
	}

	private OnClickListener setPeopleCountOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mHowManyPersonsSelectedBtn.setBackgroundResource(buttonBgndNormal);
			mHowManyPersonsSelectedBtn = (Button)v;
			mHowManyPersonsSelectedBtn.setBackgroundResource(buttonBgndPressed);
		}
	};

	@Override
	protected HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException {
		HashSet<PersonData> personDataSet = new HashSet<PersonData>();
		double payDouble = CalcFormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
		double howManyPersons = Integer.parseInt(mHowManyPersonsSelectedBtn.getText().toString());
		String namePrefix = getString(R.string.addPerson_multi_personPrefix);

		int nameOffset = 0;
		for(int i = 1; i<=howManyPersons; i++){
			String name = namePrefix + " " + (i + nameOffset);
			while(inputListContainsPerson(name)){
				nameOffset++;
				name = namePrefix + " " + (i + nameOffset);
			}

			List<AtomPayment> atomPays = PersonDataUtils.getDefaultAtomPaymentsList("", payDouble);
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
	
	@Override
	protected Class<?> getCalcResultActivity()  {
		return CalcResultPotluckActivity.class;
	}

	@Override
	protected Class<?> getAddNewPersonSingleActivity()  {
		return AddPersonSinglePotluck.class;
	}
}
