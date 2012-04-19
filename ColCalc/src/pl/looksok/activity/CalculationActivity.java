package pl.looksok.activity;

import pl.looksok.R;
import pl.looksok.logic.CcLogic;
import pl.looksok.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CalculationActivity extends Activity {
	private CcLogic calc = null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			calc = (CcLogic)extras.getSerializable(Constants.CALCULATION_OBJECT);
			TextView view = (TextView) findViewById(R.id.Calculation_text_debug);
			view.setText(calc.toString());
		}
    }

	@Override
	public void onBackPressed() {
    	Intent intent = new Intent(getApplicationContext(), EnterPaysActivity.class) ;
    	intent.putExtra(Constants.CALCULATION_OBJECT, calc);
    	startActivity(intent);
    	finish();
	}
    
    
}
