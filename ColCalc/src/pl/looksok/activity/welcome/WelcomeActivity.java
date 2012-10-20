package pl.looksok.activity.welcome;

import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.calcresult.CalcResultUtils;
import pl.looksok.activity.calcresult.CalculationActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class WelcomeActivity extends ColCalcActivity {

	private static final String LOG_TAG = WelcomeActivity.class.getSimpleName();

	private List<CalculationLogic> storedCalcs;
	private StoredCalcsListAdapter adapter;
	private CalculationLogic calcItemToRemove;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		((Button)findViewById(R.id.welcome_mode_potluckParty)).setOnClickListener(modePotluckPartyClickListener);
		populateStoredCalcsList();
	}

	private void populateStoredCalcsList() {
		storedCalcs = CalcPersistence.readStoredCalculationList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE);
		if(storedCalcs!=null){
			adapter = new StoredCalcsListAdapter(WelcomeActivity.this, R.layout.stored_calcs_list_item, storedCalcs);
			((ListView)findViewById(R.id.welcome_savedCalcs_list)).setAdapter(adapter);
		} else{
			Log.d(LOG_TAG, "Stored calcs is null. no items were saved until now");
		}
	}
	
	OnClickListener modePotluckPartyClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getApplicationContext(), CalculationActivity.class) ;
        	startActivity(intent);
        	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
        	finish();
        }
    };

    
    public void editCalcOnClickHandler(View v){
    	CalculationLogic calcItem = (CalculationLogic)v.getTag();
    	Intent intent = new Intent(getApplicationContext(), CalculationActivity.class) ;
    	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calcItem);
    	startActivity(intent);
    	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
    	finish();
    }
    
	public void removeCalcOnClickHandler(View v){
		calcItemToRemove = (CalculationLogic)v.getTag();
		showDialog(DIALOG_REMOVE_CALC);
	}
	
	public void shareCalculation(View v){
		CalculationLogic calcItem = (CalculationLogic)v.getTag();
		CalcResultUtils utils = new CalcResultUtils();
		Intent emailIntent = utils.prepareEmailIntent(getApplicationContext(), calcItem);
		startActivity(Intent.createChooser(emailIntent, getString(R.string.email_utils_chooseEmailClient)));
	}

	@Override
	protected void handleRemoveConfirm(int dialogType) {
		CalcPersistence.removeCalculationFromList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE, calcItemToRemove);
		populateStoredCalcsList();
		calcItemToRemove = null;
	}
}
