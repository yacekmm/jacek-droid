package pl.looksok.activity.calcresult;

import java.util.List;

import org.joda.time.DateTime;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.addperson.AddPersonMultiPotluck;
import pl.looksok.activity.welcome.WelcomeActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.utils.CalcFormatterHelper;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

public abstract class CalcResultBaseActivity extends ColCalcActivity {
	protected static final String LOG_TAG = CalcResultBaseActivity.class.getSimpleName();

	private static final int SAVE_NEW_CALC = 0;

	private CalculationLogic calc = null;
	private ListView resultList;
	private List<PersonData> listArray;
	private ResultsListAdapter adapter;

	private PersonData personDataHolder = null;

	private CalcResultUtils utils = new CalcResultUtils();

	private EditText calcNameEditText;

	private static boolean calcWasEdited = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getXmlLayout());

		calcNameEditText = (EditText)findViewById(R.id.calc_calcName_edit);
		calcNameEditText.setOnKeyListener(hideKeyboardListener);
		readInputBundle();
		resultList = (ListView)findViewById(R.id.calc_listView_list);
		initButtonsActions();
		initButtonsStyles();
		populateListArray();
		initCalculationDetailsBar();
	}

	protected void initButtonsStyles() {}

	protected abstract int getXmlLayout();

	private void initButtonsActions() {
		boolean isAnyPersonOnList = calc.getTotalPersons() > 0;

		Button saveCalcBtn = (Button)findViewById(R.id.calc_saveCalculation_button);
		saveCalcBtn.setOnClickListener(saveCalculationButtonClickListener);
		saveCalcBtn.setEnabled(isAnyPersonOnList);

		ImageButton shareCalcBtn = (ImageButton)findViewById(R.id.calc_sendCalculation_button);
		shareCalcBtn.setOnClickListener(shareCalculationButtonClickListener);
		shareCalcBtn.setEnabled(isAnyPersonOnList);

		((Button)findViewById(R.id.calc_addPerson_button)).setOnClickListener(addPersonButtonClickListener);
		((Button)findViewById(R.id.calc_addMultiPerson_button)).setOnClickListener(addMultiPersonButtonClickListener);
		((ImageButton)findViewById(R.id.calc_removeCalc_button)).setOnClickListener(removeCalcButtonClickListener);
	}

	private void initCalculationDetailsBar() {
		((TextView)findViewById(R.id.calcDetailsHeader_calcDate)).setText(calc.getDateSaved().toString(Constants.SIMPLE_DATE_FORMAT));
		((TextView)findViewById(R.id.calcDetailsHeader_calcTotal)).setText(CalcFormatterHelper.currencyFormat(calc.getTotalPay(), 0));
		((TextView)findViewById(R.id.calcDetailsHeader_calcPersons)).setText(String.valueOf(calc.getTotalPersons()));
	}

	private void populateListArray() {
		listArray = utils.readCalcPeopleToListArray(calc);

		adapter = new ResultsListAdapter(CalcResultBaseActivity.this, R.layout.calculation_list_item, listArray);
		resultList.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		if(adapter.getItems().size() > 0)
			findViewById(R.id.calc_emptyPeopleListText).setVisibility(View.GONE);
		else
			findViewById(R.id.calc_emptyPeopleListText).setVisibility(View.VISIBLE);

	}

	private void readInputBundle() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			calc = (CalculationLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
			calcNameEditText.setText(calc.getCalcName());
			calc.recalculate();
		}else{
			calc = new CalculationLogic();
			calc.setCalculationType(getCalculationType());
		}
	}

	protected abstract CalculationType getCalculationType();

	OnClickListener saveCalculationButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			saveCalculation();
			goToWelcomeScreen();
		}
	};

	protected void saveCalculation() {
		String calcName = calcNameEditText.getText().toString();
		if(calcName.length() == 0)
			calcName = getString(R.string.calculation_default_name_text) + " " + DateTime.now().toString(Constants.SIMPLE_DATE_FORMAT_WITH_HOUR);

		calc.setCalcName( calcName );
		calc.setDateSaved(DateTime.now());
		CalcPersistence.addCalculationToList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE, calc);
		Toast.makeText(getApplicationContext(), R.string.calculation_saved_text, Toast.LENGTH_SHORT).show();
	}

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
			calcWasEdited = true;
			calc.setCalcName(calcNameEditText.getText().toString());
			Intent intent = new Intent(getApplicationContext(), getAddPersonSingleActivity()) ;
			intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			finish();
		}

	};

	protected abstract Class<?> getAddPersonSingleActivity();

	OnClickListener addMultiPersonButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			calcWasEdited = true;
			calc.setCalcName(calcNameEditText.getText().toString());
			Intent intent = new Intent(getApplicationContext(), AddPersonMultiPotluck.class) ;
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
		calcWasEdited = false;
		Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class) ;
		startActivity(intent);
		overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
		finish();
	}

	public void editPerson(View v) {
		calcWasEdited = true;

		PersonData pd = calc.findPersonInList(((PersonData)v.getTag()).getName());
		calc.getInputPaysList().remove(pd);

		Intent intent = new Intent(getApplicationContext(), getAddPersonSingleActivity()) ;
		intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
		intent.putExtra(Constants.BUNDLE_PERSON_TO_EDIT, pd);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
		finish();
	}

	public void removePerson(View v){
		personDataHolder = calc.findPersonInList(((PersonData)v.getTag()).getName());
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
		if(CalcPersistence.isCalcOnSavedList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE, calc)){
			if(calcWasEdited)
				saveCalculation();
			goToWelcomeScreen();
		}else if(calc.getInputPaysList().size()>0)
			showDialog(SAVE_NEW_CALC);
		else 
			goToWelcomeScreen();
	}

	protected Dialog onCreateDialog(int id) {
		Dialog result = super.onCreateDialog(id);
		switch (id) {
		case SAVE_NEW_CALC:
			result = createDialogSaveNewCalc();
		}
		return result;
	}

	private Dialog createDialogSaveNewCalc() {
		return new AlertDialog.Builder(this)
		.setIcon(R.drawable.save)
		.setTitle(R.string.calculation_dialog_saveCalc)
		.setPositiveButton(R.string.calculation_dialog_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				saveCalculation();
				goToWelcomeScreen();
			}
		})
		.setNegativeButton(R.string.calculation_dialog_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToWelcomeScreen();
			}
		})
		.create();
	}
}
