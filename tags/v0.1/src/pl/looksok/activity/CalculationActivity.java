package pl.looksok.activity;

import pl.looksok.R;
import pl.looksok.logic.CcLogic;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CalculationActivity extends Activity {
	private CcLogic calc = null;
	private Button mSaveButton;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);
        
        readInputBundle();
        
        mSaveButton = (Button)findViewById(R.id.calculation_button_save);
        mSaveButton.setOnClickListener(saveButtonClickListener);
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
			TextView view = (TextView) findViewById(R.id.Calculation_text_debug);
			view.setText(calc.toString());
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
