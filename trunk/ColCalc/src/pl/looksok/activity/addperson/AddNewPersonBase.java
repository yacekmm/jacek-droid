package pl.looksok.activity.addperson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.addperson.utils.AddPersonUtils;
import pl.looksok.activity.calcresult.CalculationActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public abstract class AddNewPersonBase extends ColCalcActivity {
	protected static final String LOG_TAG = AddNewPerson.class.getSimpleName();

	CalculationLogic calc;
	List<PersonData> inputPaysList = new ArrayList<PersonData>();
	protected AddPersonUtils utils = new AddPersonUtils();
	HashSet<String> emails = new HashSet<String>();

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
	}

	private void initTopControlsBar() {
		((ImageButton)findViewById(R.id.addPersonHeader_addPerson_btn)).setOnClickListener(saveAndAddNextPersonClickListener);
		((ImageButton)findViewById(R.id.addPersonHeader_addMultiPerson_btn)).setOnClickListener(saveAndAddNextMultiPersonClickListener);
		((Button)findViewById(R.id.addPersonHeader_saveCalculation_btn)).setOnClickListener(saveAndShowResultsClickListener);
	}

	OnClickListener saveAndShowResultsClickListener = new OnClickListener() {
		public void onClick(View v) {
			try{
				saveAndShowResults(getNewInputDataToAdd());
			}catch(BadInputDataException e){
				Log.d(LOG_TAG, "Input data was not valid");
			}
		}
	};

	protected void saveAndShowResults(HashSet<PersonData> newInputData) {
		try{
			for (PersonData pd : newInputData) {
				inputPaysList.add(pd);
			}
			calculateAndShowResults();
		}catch(BadInputDataException e){
			Log.d(LOG_TAG, "Input data was not valid: " + e.getMessage());
		}
	}

	OnClickListener saveAndAddNextPersonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try{
				saveAndAddNext(getNewInputDataToAdd(), AddNewPerson.class);
			}catch(BadInputDataException e){
				Log.d(LOG_TAG, "Input data was not valid");
			}
		}
	};

	OnClickListener saveAndAddNextMultiPersonClickListener = new OnClickListener() {
		public void onClick(View v) {
			saveAndAddNext(getNewInputDataToAdd(), AddNewPersonMulti.class);
		}
	};
	
	protected void saveAndAddNext(HashSet<PersonData> newInputData, Class<?> nextActivityClass) {
		try{
			for (PersonData pd : newInputData) {
				inputPaysList.add(pd);
			}
			calc.calculate(inputPaysList);
			Intent intent = new Intent(getApplicationContext(), nextActivityClass) ;
			intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			finish();
		}catch(BadInputDataException e){
			Log.d(LOG_TAG, "Input data was not valid");
		}
	}

	protected abstract HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException;

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