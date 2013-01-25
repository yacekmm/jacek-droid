package pl.looksok.activity.welcome;

import java.util.List;

import pl.looksok.R;
import pl.looksok.activity.ColCalcActivity;
import pl.looksok.activity.calcresult.CalcResultRestaurantActivity;
import pl.looksok.activity.calcresult.CalcResultUtils;
import pl.looksok.activity.calcresult.CalcResultPotluckActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
		((Button)findViewById(R.id.welcome_mode_restaurant)).setOnClickListener(modeRestaurantClickListener);
	}
	
	@Override
	protected void onResume() {
		ReadStoredCalcs readCalcsTask = new ReadStoredCalcs();
		readCalcsTask.execute();
		findViewById(R.id.welcome_savedCalcs_list).setVisibility(View.INVISIBLE);
		super.onResume();
	}

	private void populateStoredCalcsList() {
		if(storedCalcs!=null){
			adapter = new StoredCalcsListAdapter(WelcomeActivity.this, R.layout.stored_calcs_list_item, storedCalcs);
			ListView listView = (ListView)findViewById(R.id.welcome_savedCalcs_list);
			listView.setAdapter(adapter);
			listView.setSelection(0);
		} else{
			Log.d(LOG_TAG, "Stored calcs is null. no items were saved until now");
			findViewById(R.id.welcome_noStoredCalcs).setVisibility(View.VISIBLE);
		}
	}
	
	OnClickListener modePotluckPartyClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getApplicationContext(), CalcResultPotluckActivity.class) ;
        	startActivity(intent);
        	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
        	finish();
        }
    };
	
	OnClickListener modeRestaurantClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getApplicationContext(), CalcResultRestaurantActivity.class) ;
        	startActivity(intent);
        	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
        	finish();
        }
    };

    public void editCalcOnClickHandler(View v){
    	CalculationLogic calcItem = (CalculationLogic)v.getTag();
    	Intent intent = new Intent(getApplicationContext(), getCalcResultActivityForCalcType(calcItem.getCalculationType())) ;
    	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calcItem);
    	startActivity(intent);
    	overridePendingTransition(DEFAULT_TRANSITION_ANIMATION_ENTER, DEFAULT_TRANSITION_ANIMATION_EXIT);
    	finish();
    }

	protected Class<?> getCalcResultActivityForCalcType(CalculationType calculationType) {
		if(calculationType.equals(CalculationType.RESTAURANT))
			return CalcResultRestaurantActivity.class;
		else
			return CalcResultPotluckActivity.class;
	}
    
	public void removeCalcOnClickHandler(View v){
		calcItemToRemove = (CalculationLogic)v.getTag();
		showDialog(DIALOG_REMOVE_CALC);
	}
	
	public void shareCalcOnClickHandler(View v){
		CalculationLogic calcItem = (CalculationLogic)v.getTag();
		try{
			calcItem.recalculate();
		}catch(BadInputDataException e){
			//TODO: handle BadInputDataException on recalculate maybe simply open calculation?
			Log.e(LOG_TAG, "BadInputData exception: " + e.getMessage());
			Toast.makeText(getApplicationContext(), getString(R.string.calcResult_badInputDataError_whileSharingCalc), Toast.LENGTH_LONG).show();
			return;
		}
		CalcResultUtils utils = new CalcResultUtils();
		Intent emailIntent = utils.prepareEmailIntent(getApplicationContext(), calcItem);
		startActivity(Intent.createChooser(emailIntent, getString(R.string.email_utils_chooseEmailClient)));
	}

	@Override
	protected void handleRemoveConfirm(int dialogType) {
		CalcPersistence.removeCalculationFromList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE, calcItemToRemove);
		ReadStoredCalcs readCalcsTask = new ReadStoredCalcs();
		readCalcsTask.execute();
		calcItemToRemove = null;
	}

	private void updateHelperVisibility() {
		if(adapter.getItems().size() > 0)
			findViewById(R.id.welcome_noStoredCalcs).setVisibility(View.GONE);
		else
			findViewById(R.id.welcome_noStoredCalcs).setVisibility(View.VISIBLE);
		findViewById(R.id.welcome_progressBar).setVisibility(View.INVISIBLE);
	}
	
	private class ReadStoredCalcs extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					findViewById(R.id.welcome_progressBar).setVisibility(View.VISIBLE);
				}
			});
			storedCalcs = CalcPersistence.readStoredCalculationList(getApplicationContext(), Constants.PERSISTENCE_SAVED_CALCS_FILE);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try{
				populateStoredCalcsList();
				Animation pushUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_in_slow);
		        findViewById(R.id.welcome_savedCalcs_list).startAnimation(pushUp);
		        findViewById(R.id.welcome_savedCalcs_list).setVisibility(View.VISIBLE);
		        
		        updateHelperVisibility();
			}catch(Exception e){
				Log.i(LOG_TAG, "Exception while reading stored calcs: " + e.getMessage());
				findViewById(R.id.welcome_progressBar).setVisibility(View.INVISIBLE);
			}
			super.onPostExecute(result);
		}
	}
}
