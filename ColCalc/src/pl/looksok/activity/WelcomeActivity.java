package pl.looksok.activity;

import pl.looksok.R;
import pl.looksok.activity.addperson.AddNewPerson;
import pl.looksok.activity.calcresult.CalculationResultActivity;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends ColCalcActivity {

	private Button mNewButton;

	private Button mLoadButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		mNewButton = (Button)findViewById(R.id.welcome_Button_newCalculation);
		mNewButton.setOnClickListener(newCalculationButtonClickListener);
		
		mLoadButton = (Button)findViewById(R.id.welcome_Button_loadCalculation);
		mLoadButton.setOnClickListener(loadCalculationButtonClickListener);
	}
	
	OnClickListener newCalculationButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getApplicationContext(), AddNewPerson.class) ;
        	startActivity(intent);
        	finish();
        }
    };
    
	OnClickListener loadCalculationButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	CalculationLogic calc = CalcPersistence.readStoredCalculation(getApplicationContext(), "installedapplist.txt");
        	
        	Intent intent = new Intent(getApplicationContext(), CalculationResultActivity.class) ;
        	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
        	startActivity(intent);
        	finish();
        }
    };
}
