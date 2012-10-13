package pl.looksok.activity.calcresult;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.addperson.AddNewPerson;
import pl.looksok.activity.addperson.AddNewPersonMulti;
import pl.looksok.activity.welcome.WelcomeActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CalculationActivity extends ColCalcActivity {
	protected static final String LOG_TAG = CalculationActivity.class.getSimpleName();

	private CalculationLogic calc = null;
	private ListView resultList;
	private List<PersonData> listArray;
	private ResultsListAdapter adapter;

	private PersonData personDataHolder = null;

	private CalcResultUtils utils = new CalcResultUtils();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculation);

		readInputBundle();
		resultList = (ListView)findViewById(R.id.calc_listView_list);
		populateListArray();
		initButtons();
		initCalculationDetails();
	}

	private void initButtons() {
		boolean isAnyPersonOnList = calc.getTotalPersons() > 0;

		Button saveCalcBtn = (Button)findViewById(R.id.calc_saveCalculation_button);
		saveCalcBtn.setOnClickListener(saveCalculationButtonClickListener);
		saveCalcBtn.setEnabled(isAnyPersonOnList);

		ImageButton shareCalcBtn = (ImageButton)findViewById(R.id.calc_sendCalculation_button);
		shareCalcBtn.setOnClickListener(shareCalculationButtonClickListener);
		shareCalcBtn.setEnabled(isAnyPersonOnList);

		((ImageButton)findViewById(R.id.calc_addPerson_button)).setOnClickListener(addPersonButtonClickListener);
		((ImageButton)findViewById(R.id.calc_addMultiPerson_button)).setOnClickListener(addMultiPersonButtonClickListener);
		((ImageButton)findViewById(R.id.calc_removeCalc_button)).setOnClickListener(removeCalcButtonClickListener);
	}

	private void initCalculationDetails() {
		((TextView)findViewById(R.id.calcDetailsHeader_calcDate)).setText(calc.getDateSaved().toString(Constants.SIMPLE_DATE_FORMAT));
		((TextView)findViewById(R.id.calcDetailsHeader_calcTotal)).setText(String.valueOf(calc.getTotalPay()) + " " + Currency.getInstance(Locale.getDefault()).getSymbol());
		((TextView)findViewById(R.id.calcDetailsHeader_calcPersons)).setText(String.valueOf(calc.getTotalPersons()));
	}

	private void populateListArray() {
		listArray = utils.readCalcPeopleToListArray(calc);

		adapter = new ResultsListAdapter(CalculationActivity.this, R.layout.calculation_list_item, listArray);
		resultList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void readInputBundle() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			calc = (CalculationLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
			((EditText)findViewById(R.id.calc_calcName_edit)).setText(calc.getCalcName());
		}else{
			calc = new CalculationLogic();
			calc.setCalculationType(CalculationType.POTLUCK_PARTY_WITH_GIFT);
		}
	}

	OnClickListener saveCalculationButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			String calcName = ((EditText)findViewById(R.id.calc_calcName_edit)).getText().toString();
			if(calcName.length() == 0)
				calcName = getString(R.string.calculation_default_name_text) + " " + DateTime.now().toString(Constants.SIMPLE_DATE_FORMAT_WITH_HOUR);

			calc.setCalcName( calcName );
			calc.setDateSaved(DateTime.now());
			CalcPersistence.addCalculationToList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE, calc);
			Toast.makeText(getApplicationContext(), R.string.calculation_saved_text, Toast.LENGTH_SHORT).show();

			goToWelcomeScreen();
		}
	};

	OnClickListener shareCalculationButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try{
				Intent emailIntent = utils.prepareEmailIntent(getApplicationContext(), calc);
				startActivity(Intent.createChooser(emailIntent, getString(R.string.email_utils_chooseEmailClient)));
			}catch(NullPointerException e){
				Log.e(LOG_TAG, "Error while preparing email. there is no email Addresses probably: " + e.getMessage());
			}
		}
	};

	OnClickListener addPersonButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			calc.setCalcName(((TextView)findViewById(R.id.calc_calcName_edit)).getText().toString());
			Intent intent = new Intent(getApplicationContext(), AddNewPerson.class) ;
			intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			finish();
		}
	};

	OnClickListener addMultiPersonButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			calc.setCalcName(((TextView)findViewById(R.id.calc_calcName_edit)).getText().toString());
			Intent intent = new Intent(getApplicationContext(), AddNewPersonMulti.class) ;
			intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			finish();
		}
	};

	OnClickListener removeCalcButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			showDialog(DIALOG_REMOVE_CALC);
		}
	};

	private void goToWelcomeScreen() {
		Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class) ;
		startActivity(intent);
		overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
		finish();
	}

	public void editPerson(View v) {
		PersonData pd = calc.findPersonInList((PersonData)v.getTag());
		calc.getInputPaysList().remove(pd);

		Intent intent = new Intent(getApplicationContext(), AddNewPerson.class) ;
		intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
		intent.putExtra(Constants.BUNDLE_PERSON_TO_EDIT, pd);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
		finish();
	}

	public void removePerson(View v){
		personDataHolder = calc.findPersonInList((PersonData)v.getTag());
		showDialog(DIALOG_REMOVE_PERSON);
	}

	@Override
	protected void handleRemoveConfirm(int dialogType) {
		if(dialogType == DIALOG_REMOVE_PERSON){
			calc.removePerson(personDataHolder);
			calc.recalculate();
			populateListArray();
		}else if(dialogType == DIALOG_REMOVE_CALC){
			CalcPersistence.removeCalculationFromList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE, calc);
			goToWelcomeScreen();
		}
	}


	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class) ;
		startActivity(intent);
		overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
		finish();
	}

}
