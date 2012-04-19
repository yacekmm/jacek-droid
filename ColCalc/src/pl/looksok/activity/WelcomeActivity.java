package pl.looksok.activity;

import pl.looksok.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends Activity {

	private Button mNewButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		mNewButton = (Button)findViewById(R.id.welcome_Button_newCalculation);
		mNewButton.setOnClickListener(newCalculationButtonClickListener);
	}
	
	OnClickListener newCalculationButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(getApplicationContext(), EnterPaysActivity.class) ;
        	startActivity(intent);
        	finish();
        }
    };

	
}
