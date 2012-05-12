package pl.looksok.activity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import pl.looksok.R;
import pl.looksok.exception.BadInputDataException;
import pl.looksok.logic.CcLogic;
import pl.looksok.logic.InputData;
import pl.looksok.logic.PeoplePays;
import pl.looksok.utils.Constants;
import pl.looksok.utils.FormatterHelper;
import pl.looksok.utils.InputValidator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPaysActivity extends Activity {
	private List<InputData> inputPaysList = new ArrayList<InputData>();
	private ArrayAdapter<InputData> adapter;
	private CcLogic calc = new CcLogic();
	
	private Button mAddPersonButton;
	private CheckBox mEqualPaymentsBox;
	private EditText mNewPersonNameInput;
	private EditText mNewPersonPayInput;
	private EditText mNewPersonShouldPayInput;
	private TextView mNewPersonShouldPayText;
	private ListView mPeopleList;
	private Button mCalculateButton;
	
	private static final int MENU_EDIT = Menu.FIRST;
	private static final int MENU_DELETE = MENU_EDIT+1;
	protected static final String LOG_TAG = EnterPaysActivity.class.getSimpleName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pays);
        
        initActivityViews();
        
        adapter = new ArrayAdapter<InputData>(this, android.R.layout.simple_expandable_list_item_1,
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
		calc = (CcLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
		calc.setCalculationResult(new Hashtable<String, PeoplePays>());
		setHowMuchShouldPayFieldsVisibility();
		for (InputData data : calc.getInputPaysList()) {
			data.setAlreadyRefunded(0.0);
			adapter.add(data);
		}
	}

	private void initActivityViews() {
		mAddPersonButton = (Button)findViewById(R.id.EnterPays_Button_AddPerson);
        mAddPersonButton.setOnClickListener(addPersonClickListener);
        mEqualPaymentsBox = (CheckBox) findViewById(R.id.EnterPays_CheckBox_EverybodyPaysEqually);
        mEqualPaymentsBox.setOnCheckedChangeListener(equalPaysChangeListener);
        mEqualPaymentsBox.setChecked(calc.isEqualPayments());
        mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
        mNewPersonPayInput = (EditText)findViewById(R.id.EnterPays_EditText_Pay);
        mNewPersonPayInput.setOnFocusChangeListener(editTextFocusListener);
        mNewPersonShouldPayText = (TextView)findViewById(R.id.EnterPays_TextView_ShouldPay);
        mNewPersonShouldPayInput = (EditText)findViewById(R.id.EnterPays_EditText_ShouldPay);
        mNewPersonShouldPayInput.setOnFocusChangeListener(editTextFocusListener);
        mPeopleList = (ListView)findViewById(R.id.EnterPays_List_People);
        registerForContextMenu(mPeopleList);
        mCalculateButton = (Button)findViewById(R.id.enterPays_Button_Calculate);
        mCalculateButton.setOnClickListener(calculateButtonClickListener);
      
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
	}

	private void updateFieldsDependantOnPeopleListSizeVisibility() {
		if(inputPaysList.size()==0){
			mCalculateButton.setVisibility(View.GONE);
            mEqualPaymentsBox.setEnabled(true);
		}else{
			mCalculateButton.setVisibility(View.VISIBLE);
            mEqualPaymentsBox.setEnabled(false);
		}
	}

	private void editPerson(int position) {
		InputData person = adapter.getItem(position);
		mNewPersonNameInput.setText(person.getName());
		mNewPersonPayInput.setText(String.valueOf(person.getPay()));
		mNewPersonShouldPayInput.setText(String.valueOf(person.getShouldPay()));
		removePerson(position);
	}

	OnClickListener addPersonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	try{
        		addNewInputDataToList();
        		clearInputFieldsToDefaults();
        		Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_PersonAdded), Toast.LENGTH_SHORT).show();
        	}catch(BadInputDataException e){
        		Log.d(LOG_TAG, "Input data was not valid");
        	}
            
        }

		private void clearInputFieldsToDefaults() {
			mNewPersonNameInput.setText(getResources().getString(R.string.EnterPays_TextView_EmptyText));
            mNewPersonNameInput.requestFocus();
            mNewPersonPayInput.setText(getResources().getString(R.string.EnterPays_TextView_ZeroValue));
            mNewPersonShouldPayInput.setText(getResources().getString(R.string.EnterPays_TextView_ZeroValue));
            updateFieldsDependantOnPeopleListSizeVisibility();
		}

		private void addNewInputDataToList() throws BadInputDataException{
			String name = mNewPersonNameInput.getText().toString();
        	double payDouble = FormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
        	double shouldPayDouble = FormatterHelper.readDoubleFromEditText(mNewPersonShouldPayInput);
        	
        	if(!InputValidator.inputIsValid(getApplicationContext(), name, payDouble, shouldPayDouble, calc.isEqualPayments(), inputPaysList))
        		throw new BadInputDataException();
        	
        	if(calc.isEqualPayments())
        		adapter.add(new InputData(name, payDouble));
        	else
        		adapter.add(new InputData(name, payDouble, shouldPayDouble));
		}
    };
    
	OnClickListener calculateButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
    		try{
    			calc.calculate(inputPaysList);
    			
    			Intent intent = new Intent(getApplicationContext(), CalculationActivity.class) ;
            	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
            	startActivity(intent);
            	finish();
    		}catch(BadInputDataException e){
    			Log.d(LOG_TAG, "Bad input provided: " + e.getMessage());
    			Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_BadInputDataError), Toast.LENGTH_SHORT).show();
    		}
        }
    };
    
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
		Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class) ;
    	startActivity(intent);
    	finish();
	}
}