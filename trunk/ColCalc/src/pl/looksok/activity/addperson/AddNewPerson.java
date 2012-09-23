package pl.looksok.activity.addperson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.calcresult.CalculationActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;
import pl.looksok.utils.Constants;
import pl.looksok.utils.FormatterHelper;
import pl.looksok.utils.InputValidator;
import pl.looksok.utils.exceptions.BadInputDataException;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewPerson extends ColCalcActivity {
	private List<PersonData> inputPaysList = new ArrayList<PersonData>();
	private ArrayAdapter<PersonData> adapter;
	private CalculationLogic calc;
	
	private CheckBox mEqualPaymentsBox;
	private EditText mNewPersonNameInput;
	private EditText mNewPersonPayInput;
	private EditText mNewPersonShouldPayInput;
	private TextView mNewPersonShouldPayText;
	private ListView mPeopleList;
	
	private static final int MENU_EDIT = Menu.FIRST;
	private static final int MENU_DELETE = MENU_EDIT+1;
	protected static final String LOG_TAG = AddNewPerson.class.getSimpleName();
	protected static final int PICK_CONTACT = 0;
	private HashSet<String> emails = new HashSet<String>();
	
	private AddPersonUtils utils = new AddPersonUtils();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pays);
        
        calc = new CalculationLogic();
        initActivityViews();
        
        adapter = new ArrayAdapter<PersonData>(this, android.R.layout.simple_expandable_list_item_1,
        		android.R.id.text1, inputPaysList);
        mPeopleList.setAdapter(adapter);
        
        readInputBundleIfNotEmpty();
    }

	private void readInputBundleIfNotEmpty() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			loadInputDataFromBundle(bundle);
		}
		updateFieldsDependantOnPeopleListSizeVisibility();
	}

	private void loadInputDataFromBundle(Bundle extras) {
		calc = (CalculationLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
		calc.setCalculationResult(new Hashtable<String, PersonData>());
		setHowMuchShouldPayFieldsVisibility();

		PersonData pd = (PersonData)extras.getSerializable(Constants.BUNDLE_PERSON_TO_EDIT);
		if(pd!=null){
			mNewPersonNameInput.setText(pd.getName());
			mNewPersonPayInput.setText(String.valueOf(pd.getPayMadeByPerson()));
			mNewPersonShouldPayInput.setText(String.valueOf(pd.getHowMuchPersonShouldPay()));
		}
		
		for (PersonData data : calc.getInputPaysList()) {
			data.setAlreadyRefunded(0.0);
			adapter.add(data);
		}
		
	}

	private void initActivityViews() {
		((Button)findViewById(R.id.addPersonHeader_saveCalculation_btn)).setOnClickListener(addPersonClickListener);
		((ImageButton)findViewById(R.id.addPersonHeader_addPerson_btn)).setOnClickListener(saveAndAddNextPersonClickListener);
        ((ImageButton)findViewById(R.id.EnterPays_button_getPersonFromContacts)).setOnClickListener(getContactClickListener);
        mEqualPaymentsBox = (CheckBox) findViewById(R.id.EnterPays_CheckBox_EverybodyPaysEqually);
        mEqualPaymentsBox.setOnCheckedChangeListener(equalPaysChangeListener);
        mEqualPaymentsBox.setChecked(calc.isEqualPayments());
        mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
        mNewPersonPayInput = (EditText)findViewById(R.id.EnterPays_EditText_Pay);
        mNewPersonPayInput.setOnFocusChangeListener(editTextFocusListener);
        mNewPersonPayInput.addTextChangedListener(payTextChangedListener);
        mNewPersonShouldPayText = (TextView)findViewById(R.id.EnterPays_TextView_ShouldPay);
        mNewPersonShouldPayInput = (EditText)findViewById(R.id.EnterPays_EditText_ShouldPay);
        mNewPersonShouldPayInput.setOnFocusChangeListener(editTextFocusListener);
        mNewPersonShouldPayInput.addTextChangedListener(payTextChangedListener);
        mPeopleList = (ListView)findViewById(R.id.EnterPays_List_People);
        registerForContextMenu(mPeopleList);
      
        setHowMuchShouldPayFieldsVisibility();
	}

	private void setHowMuchShouldPayFieldsVisibility() {
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  	super.onCreateContextMenu(menu, v, menuInfo);

	  	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(adapter.getItem(info.position).getName());
	  	menu.add(0, MENU_EDIT, 0, getResources().getString(R.string.EnterPays_Menu_Edit));
	  	menu.add(0, MENU_DELETE, 1, getResources().getString(R.string.EnterPays_Menu_Remove));
	}
    
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	 
	    switch (item.getItemId()) {
	    case MENU_EDIT:
	    	editPerson(info.position);
	    	return true;
	    case MENU_DELETE:
	    	removePerson(info.position);
	    	return true;
	    }
	    return super.onContextItemSelected(item);
	}
	
	private void removePerson(int position) {
		adapter.remove(adapter.getItem(position));
		updateFieldsDependantOnPeopleListSizeVisibility();
		updateShouldPayTextFields(FormatterHelper.readDoubleFromEditText(mNewPersonPayInput), 
				FormatterHelper.readDoubleFromEditText(mNewPersonShouldPayInput));
	}

	private void updateFieldsDependantOnPeopleListSizeVisibility() {
		if(inputPaysList.size()==0){
            mEqualPaymentsBox.setEnabled(true);
		}else{
            mEqualPaymentsBox.setEnabled(false);
		}
	}

	private void editPerson(int position) {
		PersonData person = adapter.getItem(position);
		mNewPersonNameInput.setText(person.getName());
		mNewPersonPayInput.setText(String.valueOf(person.getPayMadeByPerson()));
		mNewPersonShouldPayInput.setText(String.valueOf(person.getHowMuchPersonShouldPay()));
		removePerson(position);
	}

	private TextWatcher payTextChangedListener = new TextWatcher() {
		
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			updateShouldPayTextFields(FormatterHelper.readDoubleFromEditText(mNewPersonPayInput),
					FormatterHelper.readDoubleFromEditText(mNewPersonShouldPayInput));
		}
		
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void afterTextChanged(Editable s) {}
	};
	
	private void updateShouldPayTextFields(double editedPay, double editedShouldPay) {
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
	
	OnClickListener addPersonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	try{
        		adapter.add(getNewInputDataToAdd());
        		clearInputFieldsToDefaults();
        		calculateAndShowResults();
        	}catch(BadInputDataException e){
        		Log.d(LOG_TAG, "Input data was not valid");
        	}
        }
    };
    
    OnClickListener saveAndAddNextPersonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	try{
        		adapter.add(getNewInputDataToAdd());
        		clearInputFieldsToDefaults();
        		calc.calculate(inputPaysList);
        	}catch(BadInputDataException e){
        		Log.d(LOG_TAG, "Input data was not valid");
        	}
        }
    };
    
	private void clearInputFieldsToDefaults() {
		mNewPersonNameInput.setText(getResources().getString(R.string.EnterPays_TextView_EmptyText));
        mNewPersonNameInput.requestFocus();
        mNewPersonPayInput.setText(getResources().getString(R.string.EnterPays_TextView_ZeroValue));
        mNewPersonShouldPayInput.setText(getResources().getString(R.string.EnterPays_TextView_ZeroValue));
        updateFieldsDependantOnPeopleListSizeVisibility();
	}

	private PersonData getNewInputDataToAdd() throws BadInputDataException{
		String name = mNewPersonNameInput.getText().toString();
    	double payDouble = FormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
    	double shouldPayDouble = FormatterHelper.readDoubleFromEditText(mNewPersonShouldPayInput);
    	
    	if(!InputValidator.inputIsValid(getApplicationContext(), name, payDouble, shouldPayDouble, calc.isEqualPayments(), inputPaysList))
    		throw new BadInputDataException();
    	
    	if(calc.isEqualPayments())
    		return new PersonData(name, payDouble, emails);
    	else
    		return new PersonData(name, payDouble, shouldPayDouble, emails);
	}
    
	OnClickListener calculateButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
    		calculateAndShowResults();
        }

    };

    private void calculateAndShowResults() {
    	try{
    		calc.calculate(inputPaysList);
    		
    		Intent intent = new Intent(this.getApplicationContext(), CalculationActivity.class) ;
    		intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
    		startActivity(intent);
    		finish();
    		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    	}catch(BadInputDataException e){
    		Log.d(LOG_TAG, "Bad input provided: " + e.getMessage());
    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_BadInputDataError), Toast.LENGTH_SHORT).show();
    	}
    }
    
    OnFocusChangeListener editTextFocusListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if(v.getId() == mNewPersonPayInput.getId() || v.getId() == mNewPersonShouldPayInput.getId() ){
				EditText editTextView = (EditText)v;
				if(hasFocus){
					double payDouble = FormatterHelper.readDoubleFromEditText(editTextView);
					if(payDouble == 0.0){
		        		editTextView.setText(getResources().getString(R.string.EnterPays_TextView_EmptyText));
		        	}
				}else{
					if(editTextView.getText().length() == 0)
						editTextView.setText(getResources().getString(R.string.EnterPays_TextView_ZeroValue));
				}
			}
		}
	};
	
	OnCheckedChangeListener equalPaysChangeListener = new OnCheckedChangeListener(){
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
	    	calc.setEqualPayments(isChecked);
	        setHowMuchShouldPayFieldsVisibility();
	    }
	};
	
	@Override
	public void onBackPressed() {
		calculateAndShowResults();
	}
}