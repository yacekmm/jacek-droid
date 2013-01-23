package pl.looksok.activity.addperson;


import java.util.HashSet;

import pl.looksok.R;
import pl.looksok.activity.addperson.utils.InputValidator;
import pl.looksok.activity.addperson.utils.OnTotalPayChangeListener;
import pl.looksok.activity.calcresult.CalcResultPotluckActivity;
import pl.looksok.currencyedittext.CurrencyEditText;
import pl.looksok.currencyedittext.utils.FormatterHelper;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.CalcFormatterHelper;
import android.app.Activity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddPersonSinglePotluck extends AddPersonSingleBase implements OnTotalPayChangeListener {
	
	private CheckBox mReceivesGiftCheckBox;
	private CheckBox mBuysGiftCheckBox;
	private CurrencyEditText mGiftValueInput;
	
	private Activity mActivity = this;
	
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
	
	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		mReceivesGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_gotGift_checkbox);
		mReceivesGiftCheckBox.setOnCheckedChangeListener(receivesGiftChangeListener);
		mBuysGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_buysGift_checkbox);
		mBuysGiftCheckBox.setOnCheckedChangeListener(buysGiftChangeListener);
		mGiftValueInput = (CurrencyEditText)findViewById(R.id.EnterPays_EditText_giftValue);
		mGiftValueInput.setOnFocusChangeListener(giftValueFocusChangeListener);
		mGiftValueInput.setOnKeyListener(hideKeyboardListener);
		setGiftPaymentFieldsVisibile(false, false);
	}
	
	private OnFocusChangeListener giftValueFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}else{
				mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			}
		}
	};
	
	OnCheckedChangeListener buysGiftChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			setGiftPaymentFieldsVisibile(isChecked, true);
			if(isChecked){
				mReceivesGiftCheckBox.setChecked(false);
			}
		}
	};
	
	OnCheckedChangeListener receivesGiftChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				mBuysGiftCheckBox.setChecked(false);
			}
		}
	};
	
	private void setGiftPaymentFieldsVisibile(boolean visible, boolean withAnimation) {
		View giftValueLabel = findViewById(R.id.EnterPays_TextView_giftValue);
		int viewVisibility;
		Animation animation;
		
		if(visible){
			animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_in);
			viewVisibility = View.VISIBLE;
		}else{
			animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_out);
			viewVisibility = View.INVISIBLE;
			hideKeyboard(mGiftValueInput);
		}
		
		mGiftValueInput.setVisibility(viewVisibility);
		giftValueLabel.setVisibility(viewVisibility);
		if(withAnimation){
			mGiftValueInput.startAnimation(animation);
			giftValueLabel.startAnimation(animation);
		}
	}

	@Override
	protected void loadSpecificInputDataFromBundle(PersonData editPersonData) {
		super.loadSpecificInputDataFromBundle(editPersonData);
		mBuysGiftCheckBox.setChecked(editPersonData.getHowMuchIPaidForGift() > 0);
		mReceivesGiftCheckBox.setChecked(editPersonData.receivesGift());
		setGiftPaymentFieldsVisibile(editPersonData.getHowMuchIPaidForGift() > 0, false);
		mGiftValueInput.setText(FormatterHelper.currencyFormat(editPersonData.getHowMuchIPaidForGift(), 2));
	}
	
	@Override
	protected HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException {
		HashSet<PersonData> personDataSet = new HashSet<PersonData>();
		String name = mNewPersonNameInput.getText().toString();
		boolean receivesGift = mReceivesGiftCheckBox.isChecked();
		boolean buysGift = mBuysGiftCheckBox.isChecked();
		double giftPayment = CalcFormatterHelper.readDoubleFromEditText(mGiftValueInput);

		if(!buysGift)
			giftPayment = 0;

		if(!InputValidator.inputIsValid(getApplicationContext(), name, adapter.getTotalPay(), calc.isEqualPayments(), inputPaysList))
			throw new BadInputDataException();

		personDataSet.add(new PersonData(name, adapter.getItems(), emails, receivesGift, giftPayment));

		return personDataSet;
	}
}