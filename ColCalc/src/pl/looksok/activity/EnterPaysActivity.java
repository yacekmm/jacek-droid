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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class EnterPaysActivity extends Activity {
	private List<InputData> inputPaysList = new ArrayList<InputData>();
	private ArrayAdapter<InputData> adapter;
	
	//FIXME: change it according to checkbox in GUI
	protected boolean equalPayments = true;
	
	private Button mAddPersonButton;
	private EditText mNewPersonNameInput;
	private EditText mNewPersonPayInput;
	private ListView mPeopleList;
	private Button mCalculateButton;
	
	private static final int MENU_EDIT = Menu.FIRST;
	private static final int MENU_DELETE = MENU_EDIT+1;
	
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
        registerForContextMenu(mPeopleList);
        mCalculateButton = (Button)findViewById(R.id.enterPays_Button_Calculate);
        mCalculateButton.setOnClickListener(calculateButtonClickListener);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  	super.onCreateContextMenu(menu, v, menuInfo);

	  	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(adapter.getItem(info.position).getName());
	  	menu.add(0, MENU_EDIT, 0, getResources().getString(R.string.EnterPays_Menu_Edit));
	  	menu.add(0, MENU_DELETE, 1, getResources().getString(R.string.EnterPays_Menu_Remove));
	}
    
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	 
	    switch (item.getItemId()) {
	    case MENU_EDIT:
	    	editPerson(info.position);
	    	return true;
	    case MENU_DELETE:
	    	removePerson(info.position);
	    	return true;
	    }
	    return super.onContextItemSelected(item);
	}
	
	private void removePerson(int position) {
		adapter.remove(adapter.getItem(position));
	}

	private void editPerson(int position) {
		InputData person = adapter.getItem(position);
		mNewPersonNameInput.setText(person.getName());
		mNewPersonPayInput.setText(String.valueOf(person.getPay()));
		removePerson(position);
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
            
    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_PersonAdded), Toast.LENGTH_SHORT).show();
        }

		private boolean inputIsValid(String name, double payDouble) {
			if(name.length()<1 || payDouble < 0){
        		Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_BadInputDataError), Toast.LENGTH_SHORT).show();
        		return false;
        	}else if(duplicatedName(name)){
        		Toast.makeText(getApplicationContext(), getResources().getString(R.string.EnterPays_Toast_DuplicatedNameError), Toast.LENGTH_SHORT).show();
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
        	calc.calculate(inputPaysList, equalPayments);
        	
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