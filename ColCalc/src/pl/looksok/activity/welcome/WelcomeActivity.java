package pl.looksok.activity.welcome;

import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.calcresult.CalculationActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class WelcomeActivity extends ColCalcActivity {

	private static final String LOG_TAG = WelcomeActivity.class.getSimpleName();

	private List<CalculationLogic> storedCalcs;
	private StoredCalcsListAdapter adapter;
	
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
			((ListView)findViewById(R.id.welcome_savedCalcs_list)).setOnItemClickListener(storedCalcClickListener);
		} else{
			Log.d(LOG_TAG, "Stored calcs is null. no items were saved until now");
		}
	}
	
	OnItemClickListener storedCalcClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			CalculationLogic calc = adapter.getItem(arg2);
        	Intent intent = new Intent(getApplicationContext(), CalculationActivity.class) ;
        	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
        	startActivity(intent);
        	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
        	finish();
		}
	};
	
	OnClickListener modePotluckPartyClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getApplicationContext(), CalculationActivity.class) ;
        	startActivity(intent);
        	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
        	finish();
        }
    };
}
