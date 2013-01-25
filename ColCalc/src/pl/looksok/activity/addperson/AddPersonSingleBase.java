package pl.looksok.activity.addperson;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.addperson.utils.AtomPayListAdapter;
import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;
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
import android.widget.EditText;
import android.widget.ListView;

public abstract class AddPersonSingleBase extends AddPersonBase {
	protected EditText mNewPersonNameInput;

	private PersonData editPersonData = null;
	protected AtomPayListAdapter adapter;
	private AtomPayment atomPaymentToRemove = null;

	protected static final int PICK_CONTACT = 0;
	
	protected Activity mActivity = this;

	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		findViewById(R.id.EnterPays_button_getPersonFromContacts).setOnClickListener(getContactClickListener);
		findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(addAtomPaymentClickListener);
		mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
		mNewPersonNameInput.setOnKeyListener(hideKeyboardListener);

		setUpAtomPayAdapter(new ArrayList<AtomPayment>());
	}

	@Override
	protected void loadInputDataFromBundle(Bundle extras) {
		super.loadInputDataFromBundle(extras);

		editPersonData = (PersonData)extras.getSerializable(Constants.BUNDLE_PERSON_TO_EDIT);
		if(editPersonData!=null){
			mNewPersonNameInput.setText(editPersonData.getName());
			loadSpecificInputDataFromBundle(editPersonData);
			setUpAtomPayAdapter(editPersonData.getAtomPayments());
		}
	}

	@SuppressWarnings("unused")
	protected void loadSpecificInputDataFromBundle(PersonData loadedPersonData) {}

	private void setUpAtomPayAdapter(List<AtomPayment> atomPaymentsList) {

		if(atomPaymentsList.size()==0)
			atomPaymentsList.add(new AtomPayment());
		adapter = null;
		adapter = new AtomPayListAdapter(AddPersonSingleBase.this, R.layout.atom_pay_list_item, atomPaymentsList);
		adapter.setKeyboardHiderListener(hideKeyboardListener);
		((ListView)findViewById(R.id.EnterPays_atomPaysList)).setAdapter(adapter);
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
	
	protected OnFocusChangeListener softInputModeSwitcherListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				mActivity .getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}else{
				mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			}
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
}