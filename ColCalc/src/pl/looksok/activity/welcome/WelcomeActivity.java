package pl.looksok.activity.welcome;

import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.addperson.AddNewPerson;
import pl.looksok.activity.calcresult.CalculationResultActivity;
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
import android.widget.Toast;

public class WelcomeActivity extends ColCalcActivity {

	private static final String LOG_TAG = WelcomeActivity.class.getSimpleName();

	private List<CalculationLogic> storedCalcs;

	private StoredCalcsListAdapter adapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		((Button)findViewById(R.id.welcome_Button_newCalculation)).setOnClickListener(newCalculationButtonClickListener);
		((Button)findViewById(R.id.welcome_Button_loadCalculation)).setOnClickListener(loadCalculationButtonClickListener);
		
		storedCalcs = CalcPersistence.readStoredCalculationList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE);
		if(storedCalcs!=null){
			Log.d(LOG_TAG, "Stored calcs size: " + storedCalcs.size());
			adapter = new StoredCalcsListAdapter(WelcomeActivity.this, R.layout.stored_calcs_list_item, storedCalcs);
			((ListView)findViewById(R.id.welcome_savedCalcs_list)).setAdapter(adapter);
		} else{
			Log.d(LOG_TAG, "Stored calcs size: null");
		}
	}
	
	OnClickListener newCalculationButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getApplicationContext(), AddNewPerson.class) ;
        	startActivity(intent);
        	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
        	finish();
        }
    };
    
	OnClickListener loadCalculationButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	CalculationLogic calc = CalcPersistence.readStoredCalculation(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE);
        	if(calc==null){
        		Toast.makeText(getApplicationContext(), R.string.calculation_load_error_text, Toast.LENGTH_SHORT).show();
        		return;
        	}
        	
        	Intent intent = new Intent(getApplicationContext(), CalculationResultActivity.class) ;
        	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
        	startActivity(intent);
        	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
        	finish();
        }
    };
}
