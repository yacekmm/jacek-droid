package pl.looksok.activity.addperson;


import java.util.HashSet;

import pl.looksok.R;
import pl.looksok.activity.addperson.utils.InputValidator;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.Constants;
import pl.looksok.utils.FormatterHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddNewPerson extends AddNewPersonBase {
	CheckBox mEqualPaymentsBox;
	EditText mNewPersonNameInput;
	EditText mNewPersonShouldPayInput;
	TextView mNewPersonShouldPayText;
	CheckBox mReceivesGiftCheckBox;
	CheckBox mBuysGiftCheckBox;
	EditText mGiftValueInput;
	private PersonData editPersonData = null;

	protected static final int PICK_CONTACT = 0;
	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_new_person;
	}

	@Override
	protected int getPayInputResId() {
		return R.id.EnterPays_EditText_Pay;
	}
	
	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		((ImageButton)findViewById(R.id.EnterPays_button_getPersonFromContacts)).setOnClickListener(getContactClickListener);
		mEqualPaymentsBox = (CheckBox) findViewById(R.id.EnterPays_CheckBox_EverybodyPaysEqually);
		mEqualPaymentsBox.setOnCheckedChangeListener(equalPaysChangeListener);
		mEqualPaymentsBox.setChecked(calc.isEqualPayments());
		mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
		mNewPersonShouldPayText = (TextView)findViewById(R.id.EnterPays_TextView_ShouldPay);
		mNewPersonShouldPayInput = (EditText)findViewById(R.id.EnterPays_EditText_ShouldPay);
		mNewPersonShouldPayInput.setOnFocusChangeListener(editTextFocusListener);
		mNewPersonShouldPayInput.addTextChangedListener(payTextChangedListener);
		mReceivesGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_gotGift_checkbox);
		mReceivesGiftCheckBox.setOnCheckedChangeListener(receivesGiftChangeListener);
		mBuysGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_buysGift_checkbox);
		mBuysGiftCheckBox.setOnCheckedChangeListener(buysGiftChangeListener);
		mGiftValueInput = (EditText)findViewById(R.id.EnterPays_EditText_giftValue);

		setHowMuchShouldPayFieldsVisibility();
	}
	
	@Override
	protected void loadInputDataFromBundle(Bundle extras) {
		super.loadInputDataFromBundle(extras);
		setHowMuchShouldPayFieldsVisibility();
		
		PersonData pd = (PersonData)extras.getSerializable(Constants.BUNDLE_PERSON_TO_EDIT);
		if(pd!=null){
			editPersonData = pd;
			mNewPersonNameInput.setText(pd.getName());
			mNewPersonPayInput.setText(String.valueOf(pd.getPayMadeByPerson()));
			mNewPersonShouldPayInput.setText(String.valueOf(pd.getHowMuchPersonShouldPay()));
			mReceivesGiftCheckBox.setChecked(pd.receivesGift());
			mBuysGiftCheckBox.setChecked(pd.getHowMuchIPaidForGift() > 0);
			mGiftValueInput.setText(pd.getHowMuchIPaidForGift() > 0 ? pd.getHowMuchIPaidForGift() + "" : "");
		}else
			editPersonData = null;
	};
	
	@Override
	protected HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException {
		HashSet<PersonData> personDataSet = new HashSet<PersonData>();
		double payDouble = FormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
		String name = mNewPersonNameInput.getText().toString();
		double shouldPayDouble = FormatterHelper.readDoubleFromEditText(mNewPersonShouldPayInput);
		boolean receivesGift = mReceivesGiftCheckBox.isChecked();
		boolean buysGift = mBuysGiftCheckBox.isChecked();
		double giftPayment = FormatterHelper.readDoubleFromEditText(mGiftValueInput);

		if(!buysGift)
			giftPayment = 0;

		if(!InputValidator.inputIsValid(getApplicationContext(), name, payDouble, shouldPayDouble, calc.isEqualPayments(), inputPaysList))
			throw new BadInputDataException();

		if(calc.isEqualPayments())
			personDataSet.add(new PersonData(name, payDouble, emails, receivesGift, giftPayment));
		else{
			throw new BadInputDataException("should not reach here!");
		}
		
		return personDataSet;
	}


	void setHowMuchShouldPayFieldsVisibility() {
		try{
			if(!calc.isEqualPayments()){
				mNewPersonShouldPayInput.setVisibility(View.VISIBLE);
				mNewPersonShouldPayText.setVisibility(View.VISIBLE);
			}else{
				mNewPersonShouldPayInput.setVisibility(View.GONE);
				mNewPersonShouldPayText.setVisibility(View.GONE);
			}
		}catch(NullPointerException e){
			Log.d(LOG_TAG, "Not yet Initialized");
		}
	}

	void updateShouldPayTextFields(double editedPay, double editedShouldPay) {
		if(!calc.isEqualPayments()){
			double howMuchLeftToPay = InputValidator.validatePaysConsistency(inputPaysList, calc.isEqualPayments(), editedPay, editedShouldPay);

			if(howMuchLeftToPay > 0){
				mNewPersonShouldPayText.setText(getResources().getString(R.string.EnterPays_TextView_ShouldPay) +
						"\n" + getResources().getString(R.string.EnterPays_TextView_ShouldPay_HowMuchLeft_Start) +
						" " + howMuchLeftToPay +
						getResources().getString(R.string.EnterPays_TextView_ShouldPay_HowMuchLeft_End));
			}else if (howMuchLeftToPay <0){
				mNewPersonShouldPayText.setText(getResources().getString(R.string.EnterPays_TextView_ShouldPay) +
						"\n" + getResources().getString(R.string.EnterPays_TextView_ShouldPay_HowMuchLeft_TooMuch_Start) +
						" " + -1 * howMuchLeftToPay + " " +
						getResources().getString(R.string.EnterPays_TextView_ShouldPay_HowMuchLeft_End));
			}else {
				mNewPersonShouldPayText.setText(getResources().getString(R.string.EnterPays_TextView_ShouldPay));
			}

			mGiftValueInput.setVisibility(View.GONE);
			mBuysGiftCheckBox.setVisibility(View.GONE);
			mReceivesGiftCheckBox.setVisibility(View.GONE);
		}
	}

	OnClickListener getContactClickListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, PICK_CONTACT);
		}
	};

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT) :
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c =  managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
					mNewPersonNameInput.setText(name);

					emails = utils.getPersonEmailsSet(id, AddNewPerson.this);
				}
			}
		break;
		}
	}

	OnCheckedChangeListener equalPaysChangeListener = new OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
			calc.setEqualPayments(isChecked);
			setHowMuchShouldPayFieldsVisibility();
		}
	};

	OnCheckedChangeListener buysGiftChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mGiftValueInput.setEnabled(isChecked);
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

	private TextWatcher payTextChangedListener = new TextWatcher() {

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			updateShouldPayTextFields(FormatterHelper.readDoubleFromEditText(mNewPersonPayInput),
					FormatterHelper.readDoubleFromEditText(mNewPersonShouldPayInput));
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void afterTextChanged(Editable s) {}
	};
	
	@Override
	public void onBackPressed() {
		if(editPersonData!=null){
			HashSet<PersonData> dataToAdd = new HashSet<PersonData>();
			dataToAdd.add(editPersonData);
			saveAndShowResults(dataToAdd);
		}
		super.onBackPressed();
	}
}