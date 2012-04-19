package pl.looksok.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import pl.looksok.R;
import pl.looksok.logic.CcLogic;
import pl.looksok.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends Activity {

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
        	Intent intent = new Intent(getApplicationContext(), EnterPaysActivity.class) ;
        	startActivity(intent);
        	finish();
        }
    };
    
	OnClickListener loadCalculationButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	String filename = "installedapplist.txt";
        	CcLogic calc = null;
        	
        	FileInputStream fis = null;
        	ObjectInputStream in = null;
        	try {
        		fis = openFileInput(filename);
        		in = new ObjectInputStream(fis);
        		calc = (CcLogic) in.readObject();
        		in.close();
        		fis.close();
        		
            	Intent intent = new Intent(getApplicationContext(), EnterPaysActivity.class) ;
            	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
            	startActivity(intent);
            	finish();
        		
        	}catch (ClassNotFoundException e) {
        		e.printStackTrace();
        		
        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (StreamCorruptedException e) {
        		
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	
        	Log.v("opened Calc", "success");
        }
    };
}
