package pl.looksok.activity;

import java.util.ArrayList;
import java.util.List;

import pl.looksok.R;
import pl.looksok.logic.CcLogic;
import pl.looksok.logic.InputData;
import pl.looksok.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class EnterPaysActivity extends Activity {
	private List<InputData> inputPaysList = new ArrayList<InputData>();
	private ArrayAdapter<InputData> adapter;
	
	private Button mAddPersonButton;
	private EditText mNewPersonNameInput;
	private EditText mNewPersonPayInput;
	private ListView mPeopleList;
	private Button mCalculateButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pays);
        
        initActivityViews();
        
        adapter = new ArrayAdapter<InputData>(this, android.R.layout.simple_expandable_list_item_1,
        		android.R.id.text1, inputPaysList);
        mPeopleList.setAdapter(adapter);
        
        readInputBundleIfNotEmpty();
    }

	private void readInputBundleIfNotEmpty() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			CcLogic calc = (CcLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
			for (InputData data : calc.getInputPaysList()) {
				adapter.add(data);
			}
			if(inputPaysList.size() < 1)
	        	mCalculateButton.setVisibility(View.GONE);
		} else
			mCalculateButton.setVisibility(View.GONE);
	}

	private void initActivityViews() {
		mAddPersonButton = (Button)findViewById(R.id.EnterPays_Button_AddPerson);
        mAddPersonButton.setOnClickListener(addPersonClickListener);
        mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
        mNewPersonPayInput = (EditText)findViewById(R.id.EnterPays_EditText_Pay);
        mNewPersonPayInput.setOnFocusChangeListener(payEditTextFocusListener);
        mPeopleList = (ListView)findViewById(R.id.EnterPays_List_People);
        mCalculateButton = (Button)findViewById(R.id.enterPays_Button_Calculate);
        mCalculateButton.setOnClickListener(calculateButtonClickListener);
	}
    
	OnClickListener addPersonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	String name = mNewPersonNameInput.getText().toString();
        	double payDouble = readPayFromEditText();
        	
        	if(!inputIsValid(name, payDouble))
        		return;
        	
            adapter.add(new InputData(name, payDouble));
            
            mNewPersonNameInput.setText("");
            mNewPersonNameInput.requestFocus();
            mNewPersonPayInput.setText(getResources().getString(R.string.EnterPays_TextView_DefaultPayValue));
            mCalculateButton.setVisibility(View.VISIBLE);
            
    		Toast t = Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_PersonAdded), Toast.LENGTH_SHORT);
    		t.show();
        }

		private boolean inputIsValid(String name, double payDouble) {
			if(name.length()<1 || payDouble < 0){
        		Toast t = Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_BadInputDataError), Toast.LENGTH_SHORT);
        		t.show();
        		return false;
        	}else if(duplicatedName(name)){
        		Toast t = Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_DuplicatedNameError), Toast.LENGTH_SHORT);
        		t.show();
        		return false;
        	}
        	
        	return true;
		}

		private boolean duplicatedName(String name) {
			for (InputData in : inputPaysList) {
				if (in.getName().equals(name)){
					return true;
				}
			}
			return false;
		}
    };
    
	OnClickListener calculateButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	CcLogic calc = new CcLogic();
        	calc.calculate(inputPaysList);
        	
        	Intent intent = new Intent(getApplicationContext(), CalculationActivity.class) ;
        	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
        	startActivity(intent);
        	finish();
        }
    };
    
    OnFocusChangeListener payEditTextFocusListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				double payDouble = readPayFromEditText();
	        	if(payDouble == 0.0){
	        		mNewPersonPayInput.setText("");
	        	}
			}else{
				if(mNewPersonPayInput.getText().length() == 0)
					mNewPersonPayInput.setText("0");
			}
		}
	};
	
	
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class) ;
    	startActivity(intent);
    	finish();
	}

	private double readPayFromEditText() {
		String payString = mNewPersonPayInput.getText().toString();
    	
    	double payDouble = 0.0;
    	if(payString.length() > 0)
    		payDouble = Double.parseDouble(payString);
		return payDouble;
	}
}