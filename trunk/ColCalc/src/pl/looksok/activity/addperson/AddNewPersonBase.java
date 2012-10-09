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
import pl.looksok.utils.exceptions.BadInputDataException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public abstract class AddNewPersonBase extends ColCalcActivity {

	CalculationLogic calc;
	List<PersonData> inputPaysList = new ArrayList<PersonData>();
	protected EditText mNewPersonPayInput;
	protected static final String LOG_TAG = AddNewPerson.class.getSimpleName();
	protected AddPersonUtils utils = new AddPersonUtils();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getAddPersonContentView());

		calc = new CalculationLogic();
		initActivityViews();

		readInputBundleIfNotEmpty();
	}

	protected abstract int getAddPersonContentView();

	protected void readInputBundleIfNotEmpty() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			loadInputDataFromBundle(bundle);
		}
	}

	protected void loadInputDataFromBundle(Bundle extras) {
		calc = (CalculationLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
		calc.setCalculationResult(new Hashtable<String, PersonData>());
		
		for (PersonData data : calc.getInputPaysList()) {
			data.setAlreadyRefunded(0.0);
			inputPaysList.add(data);
		}
	}

	protected void initActivityViews() {
		initTopControlsBar();
		mNewPersonPayInput = (EditText)findViewById(getPayInputResId());
		mNewPersonPayInput.setOnFocusChangeListener(editTextFocusListener);
		//	    mNewPersonPayInput.addTextChangedListener(payTextChangedListener);
	}

	protected abstract int getPayInputResId();

	private void initTopControlsBar() {
		((ImageButton)findViewById(R.id.addPersonHeader_addPerson_btn)).setOnClickListener(saveAndAddNextPersonClickListener);
		((ImageButton)findViewById(R.id.addPersonHeader_addMultiPerson_btn)).setOnClickListener(saveAndAddNextMultiPersonClickListener);
		((Button)findViewById(R.id.addPersonHeader_saveCalculation_btn)).setOnClickListener(saveAndShowResultsClickListener);
	}

	OnClickListener saveAndShowResultsClickListener = new OnClickListener() {
		public void onClick(View v) {
			try{
				for (PersonData pd : getNewInputDataToAdd()) {
					inputPaysList.add(pd);
				}
				//        		clearInputFieldsToDefaults();
				calculateAndShowResults();
			}catch(BadInputDataException e){
				Log.d(LOG_TAG, "Input data was not valid");
			}
		}
	};
	OnClickListener saveAndAddNextPersonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try{
				for (PersonData pd : getNewInputDataToAdd()) {
					inputPaysList.add(pd);
				}
				calc.calculate(inputPaysList);
				//        		clearInputFieldsToDefaults();
				Intent intent = new Intent(getApplicationContext(), AddNewPerson.class) ;
				intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				finish();
			}catch(BadInputDataException e){
				Log.d(LOG_TAG, "Input data was not valid");
			}
		}
	};
	OnClickListener saveAndAddNextMultiPersonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try{
				for (PersonData pd : getNewInputDataToAdd()) {
					inputPaysList.add(pd);
				}
				calc.calculate(inputPaysList);
				Intent intent = new Intent(getApplicationContext(), AddNewPersonMulti.class) ;
				intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				finish();
			}catch(BadInputDataException e){
				Log.d(LOG_TAG, "Input data was not valid");
			}
		}
	};
//	
//	OnClickListener calculateButtonClickListener = new OnClickListener() {
//		public void onClick(View v) {
//			calculateAndShowResults();
//		}
//
//	};

	protected abstract HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException;

	OnFocusChangeListener editTextFocusListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
//			if(v.getId() == mNewPersonPayInput.getId()){
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
//			}
		}
	};
	HashSet<String> emails = new HashSet<String>();



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

	public AddNewPersonBase() {
		super();
	}

	@Override
	public void onBackPressed() {
		calculateAndShowResults();
	}

}