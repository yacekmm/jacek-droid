package pl.looksok.activity;

import java.util.ArrayList;
import java.util.List;

import pl.looksok.R;
import pl.looksok.exception.BadInputDataException;
import pl.looksok.logic.InputData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EnterPaysActivity extends Activity {
	private List<InputData> inputPaysList = new ArrayList<InputData>();
	
	private Button mAddPersonButton;
	private EditText mNewPersonNameInput;
	private EditText mNewPersonPayInput;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pays);
        
        initActivityViews();
    }

	private void initActivityViews() {
		mAddPersonButton = (Button)findViewById(R.id.EnterPays_Button_AddPerson);
        mAddPersonButton.setOnClickListener(addPersonClickListener);
        mNewPersonNameInput = (EditText)findViewById(R.id.EnterPays_EditText_Name);
        mNewPersonPayInput = (EditText)findViewById(R.id.EnterPays_EditText_Pay);
	}
    
    OnClickListener addPersonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	String name = mNewPersonNameInput.getText().toString();
        	String payString = mNewPersonPayInput.getText().toString();
        	
        	double payDouble = 0.0;
        	if(payString.length() > 0)
        		payDouble = Double.parseDouble(payString);
        	
        	if(name.length()<1 || payDouble < 0){
        		//TODO: handle bad data 
        		throw new BadInputDataException();
        	}
        	
            inputPaysList.add(new InputData(name, payDouble));
            
            mNewPersonNameInput.setText("");
            mNewPersonPayInput.setText("");
        }
    };
}