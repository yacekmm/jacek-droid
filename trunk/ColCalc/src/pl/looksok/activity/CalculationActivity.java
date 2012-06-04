package pl.looksok.activity;

import pl.looksok.R;
import pl.looksok.logic.CcLogic;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CalculationActivity extends ColCalcActivity {
	private CcLogic calc = null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);
        
        readInputBundle();
    }

	OnClickListener saveButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
    	    CalcPersistence.saveCalculation(getApplicationContext(), "installedapplist.txt", calc);
        }
    };
    
	private void readInputBundle() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			calc = (CcLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
		}
	}

	@Override
	public void onBackPressed() {
    	Intent intent = new Intent(getApplicationContext(), EnterPaysActivity.class) ;
    	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
    	startActivity(intent);
    	finish();
	}
    
    
}
