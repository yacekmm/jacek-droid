package pl.looksok.activity.addperson;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.addperson.utils.AtomPayListAdapter;
import pl.looksok.activity.addperson.utils.InputValidator;
import pl.looksok.activity.addperson.utils.OnTotalPayChangeListener;
import pl.looksok.currencyedittext.CurrencyEditText;
import pl.looksok.currencyedittext.utils.FormatterHelper;
import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.CalcFormatterHelper;
import pl.looksok.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;

public abstract class AddPersonSingleBase extends AddPersonBase implements OnTotalPayChangeListener {
	private EditText mNewPersonNameInput;
	private CheckBox mReceivesGiftCheckBox;
	private CheckBox mBuysGiftCheckBox;
	private CurrencyEditText mGiftValueInput;

	private PersonData editPersonData = null;
	private AtomPayListAdapter adapter;
	private AtomPayment atomPaymentToRemove = null;

	private Activity mActivity = this;
	protected static final int PICK_CONTACT = 0;

	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		findViewById(R.id.EnterPays_button_getPersonFromContacts).setOnClickListener(getContactClickListener);
		findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(addAtomPaymentClickListener);
		mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
		mNewPersonNameInput.setOnKeyListener(hideKeyboardListener);
		mReceivesGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_gotGift_checkbox);
		mReceivesGiftCheckBox.setOnCheckedChangeListener(receivesGiftChangeListener);
		mBuysGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_buysGift_checkbox);
		mBuysGiftCheckBox.setOnCheckedChangeListener(buysGiftChangeListener);
		mGiftValueInput = (CurrencyEditText)findViewById(R.id.EnterPays_EditText_giftValue);
		mGiftValueInput.setOnFocusChangeListener(giftValueFocusChangeListener);
		mGiftValueInput.setOnKeyListener(hideKeyboardListener);
		setGiftPaymentFieldsVisibile(false, false);

		setUpAtomPayAdapter(new ArrayList<AtomPayment>());
	}

	@Override
	protected void loadInputDataFromBundle(Bundle extras) {
		super.loadInputDataFromBundle(extras);

		editPersonData = (PersonData)extras.getSerializable(Constants.BUNDLE_PERSON_TO_EDIT);
		if(editPersonData!=null){
			mNewPersonNameInput.setText(editPersonData.getName());
			mBuysGiftCheckBox.setChecked(editPersonData.getHowMuchIPaidForGift() > 0);
			mReceivesGiftCheckBox.setChecked(editPersonData.receivesGift());
			setGiftPaymentFieldsVisibile(editPersonData.getHowMuchIPaidForGift() > 0, false);
			mGiftValueInput.setText(FormatterHelper.currencyFormat(editPersonData.getHowMuchIPaidForGift(), 2));
			setUpAtomPayAdapter(editPersonData.getAtomPayments());
			updateTotalPayValue(editPersonData.getPayMadeByPerson());
		}
	}

	private void setUpAtomPayAdapter(List<AtomPayment> atomPaymentsList) {
		if(adapter!=null)
			adapter.unregisterOnTotalChangeListener(this);

		if(atomPaymentsList.size()==0)
			atomPaymentsList.add(new AtomPayment());
		adapter = null;
		adapter = new AtomPayListAdapter(AddPersonSingleBase.this, R.layout.atom_pay_list_item, atomPaymentsList);
		adapter.setKeyboardHiderListener(hideKeyboardListener);
		((ListView)findViewById(R.id.EnterPays_atomPaysList)).setAdapter(adapter);
		adapter.registerOnTotalChangeListener(this);
	}

	public void removeAtomPayOnClickHandler(View v) {
		atomPaymentToRemove = (AtomPayment)v.getTag();
		showDialog(DIALOG_REMOVE_PAY);
	}

	@Override
	protected void handleRemoveConfirm(int dialogType) {
		adapter.remove(atomPaymentToRemove);
		setUpAtomPayAdapter(adapter.getItems());
		atomPaymentToRemove = null;
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

		if(calc.isEqualPayments())
			personDataSet.add(new PersonData(name, adapter.getItems(), emails, receivesGift, giftPayment));
		else{
			throw new BadInputDataException("should not reach here!");
		}

		return personDataSet;
	}

	OnClickListener getContactClickListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, PICK_CONTACT);
		}
	};

	OnClickListener addAtomPaymentClickListener = new OnClickListener() {
		public void onClick(View v) {
			adapter.insert(new AtomPayment(), adapter.getCount());
			setUpAtomPayAdapter(adapter.getItems());
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

					emails = utils.getPersonEmailsSet(id, AddPersonSingleBase.this);
				}
			}
		break;
		}
	}

	OnCheckedChangeListener equalPaysChangeListener = new OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
			calc.setEqualPayments(isChecked);
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

	OnCheckedChangeListener receivesGiftChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				mBuysGiftCheckBox.setChecked(false);
			}
		}
	};

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
	
	@Override
	public void onBackPressed() {
		if(editPersonData!=null){
			HashSet<PersonData> dataToAdd = new HashSet<PersonData>();
			dataToAdd.add(editPersonData);
			saveAndShowResults(dataToAdd);
		}else{
			calculateAndShowResults();
		}
		super.onBackPressed();
	}

	@Override
	public void notifyOnTotalPayChange(double totalPay) {
		updateTotalPayValue(totalPay);
	}

	private void updateTotalPayValue(final double payMadeByPerson) {
		StringBuilder sb = new StringBuilder();
		sb.append(getString(R.string.EnterPays_atomPay_headerText_left)).append(" ");
		sb.append(CalcFormatterHelper.currencyFormat(payMadeByPerson, 2));
		sb.append(getString(R.string.EnterPays_atomPay_headerText_right));
	}
}