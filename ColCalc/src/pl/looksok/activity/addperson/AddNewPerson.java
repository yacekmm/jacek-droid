package pl.looksok.activity.addperson;


import java.util.HashSet;

import pl.looksok.R;
import pl.looksok.activity.addperson.utils.AtomPayListAdapter;
import pl.looksok.activity.addperson.utils.InputValidator;
import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.utils.PersonDataUtils;
import pl.looksok.utils.Constants;
import pl.looksok.utils.FormatterHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class AddNewPerson extends AddNewPersonBase {
	EditText mNewPersonNameInput;
	CheckBox mReceivesGiftCheckBox;
	CheckBox mBuysGiftCheckBox;
	EditText mGiftValueInput;
	private PersonData editPersonData = null;
	private AtomPayListAdapter adapter;
	private AtomPayment atomPaymentToRemove = null;

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
		mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
		mReceivesGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_gotGift_checkbox);
		mReceivesGiftCheckBox.setOnCheckedChangeListener(receivesGiftChangeListener);
		mBuysGiftCheckBox = (CheckBox) findViewById(R.id.EnterPays_buysGift_checkbox);
		mBuysGiftCheckBox.setOnCheckedChangeListener(buysGiftChangeListener);
		mGiftValueInput = (EditText)findViewById(R.id.EnterPays_EditText_giftValue);
	}

	@Override
	protected void loadInputDataFromBundle(Bundle extras) {
		super.loadInputDataFromBundle(extras);

		PersonData pd = (PersonData)extras.getSerializable(Constants.BUNDLE_PERSON_TO_EDIT);
		if(pd!=null){
			editPersonData = pd;
			mNewPersonNameInput.setText(pd.getName());
			mNewPersonPayInput.setText(String.valueOf(pd.getPayMadeByPerson()));
			mReceivesGiftCheckBox.setChecked(pd.receivesGift());
			mBuysGiftCheckBox.setChecked(pd.getHowMuchIPaidForGift() > 0);
			mGiftValueInput.setText(pd.getHowMuchIPaidForGift() > 0 ? pd.getHowMuchIPaidForGift() + "" : "");

			adapter = new AtomPayListAdapter(AddNewPerson.this, R.layout.atom_pay_list_item, editPersonData.getAtomPayments());
			((ListView)findViewById(R.id.EnterPays_atomPaysList)).setAdapter(adapter);
		}else
			editPersonData = null;
	};

	public void removeAtomPayOnClickHandler(View v) {
		atomPaymentToRemove = (AtomPayment)v.getTag();
		showDialog(DIALOG_REMOVE_PAY);
	}
	
	@Override
	protected void handleRemoveConfirm(int dialogType) {
		adapter.remove(atomPaymentToRemove);
		atomPaymentToRemove = null;
	}

	@Override
	protected HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException {
		HashSet<PersonData> personDataSet = new HashSet<PersonData>();
		double payDouble = FormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
		String name = mNewPersonNameInput.getText().toString();
		boolean receivesGift = mReceivesGiftCheckBox.isChecked();
		boolean buysGift = mBuysGiftCheckBox.isChecked();
		double giftPayment = FormatterHelper.readDoubleFromEditText(mGiftValueInput);

		if(!buysGift)
			giftPayment = 0;

		if(!InputValidator.inputIsValid(getApplicationContext(), name, payDouble, calc.isEqualPayments(), inputPaysList))
			throw new BadInputDataException();

		if(calc.isEqualPayments())
			//FIXME: zamienić na prawdziwe atom pays list
			personDataSet.add(new PersonData(name, PersonDataUtils.getDefaultAtomPaymentsList(payDouble), emails, receivesGift, giftPayment));
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