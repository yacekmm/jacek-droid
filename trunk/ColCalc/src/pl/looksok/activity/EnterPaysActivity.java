package pl.looksok.activity;

import java.util.ArrayList;
import java.util.List;

import pl.looksok.R;
import pl.looksok.logic.InputData;
import android.app.Activity;
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
    }

	private void initActivityViews() {
		mAddPersonButton = (Button)findViewById(R.id.EnterPays_Button_AddPerson);
        mAddPersonButton.setOnClickListener(addPersonClickListener);
        mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
        mNewPersonPayInput = (EditText)findViewById(R.id.EnterPays_EditText_Pay);
        mNewPersonPayInput.setOnFocusChangeListener(payEditTextFocusListener);
        mPeopleList = (ListView)findViewById(R.id.EnterPays_List_People);
        mCalculateButton = (Button)findViewById(R.id.EnterPays_Button_Calculate);
        mCalculateButton.setOnClickListener(calculateButtonClickListener);
        mCalculateButton.setVisibility(View.GONE);
	}
    
	OnClickListener addPersonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	String name = mNewPersonNameInput.getText().toString();
        	double payDouble = readPayFromEditText();
        	
        	if(name.length()<1 || payDouble < 0){
        		Toast t = Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_BadInputDataError), Toast.LENGTH_SHORT);
        		t.show();
        		return;
        	}
        	
            adapter.add(new InputData(name, payDouble));
            
            mNewPersonNameInput.setText("");
            mNewPersonPayInput.setText(getResources().getString(R.string.EnterPays_TextView_DefaultPayValue));
            mCalculateButton.setVisibility(View.VISIBLE);
            
    		Toast t = Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_PersonAdded), Toast.LENGTH_SHORT);
    		t.show();
        }
    };
    
	OnClickListener calculateButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	
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
    
	private double readPayFromEditText() {
		String payString = mNewPersonPayInput.getText().toString();
    	
    	double payDouble = 0.0;
    	if(payString.length() > 0)
    		payDouble = Double.parseDouble(payString);
		return payDouble;
	}
}