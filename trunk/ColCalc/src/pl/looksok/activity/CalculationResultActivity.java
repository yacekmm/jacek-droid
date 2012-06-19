package pl.looksok.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.looksok.R;
import pl.looksok.customviews.ResultsListAdapter;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;
import pl.looksok.utils.CalcPersistence;
import pl.looksok.utils.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CalculationResultActivity extends ColCalcActivity {
	private CalculationLogic calc = null;
	private ListView resultList;
	private List<PersonData> listArray;
	private Button saveCalculationButton;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);
        
        readInputBundle();
        populateListArray();
    }

	private void populateListArray() {
		listArray = new ArrayList<PersonData>();
		
		Set<String> c = calc.getCalculationResult().keySet();
		Iterator<String> it = c.iterator();
		while (it.hasNext()){
			listArray.add(calc.getCalculationResult().get(it.next()));
		}
		
		resultList = (ListView)findViewById(R.id.calc_listView_list);
		ResultsListAdapter adapter = new ResultsListAdapter(getApplicationContext(), R.layout.calculation_list_item, listArray);
		resultList.setAdapter(adapter);
		
		saveCalculationButton = (Button)findViewById(R.id.calc_button_saveCalculation);
		saveCalculationButton.setOnClickListener(saveCalculationButtonClickListener);
	}

	private void readInputBundle() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			calc = (CalculationLogic)extras.getSerializable(Constants.BUNDLE_CALCULATION_OBJECT);
		}
	}
	
	OnClickListener saveCalculationButtonClickListener = new OnClickListener() {
        public void onClick(View v) {
        	CalcPersistence.saveCalculation(getApplicationContext(), "installedapplist.txt", calc);
        	Toast.makeText(getApplicationContext(), R.string.calculation_saved_text, Toast.LENGTH_SHORT).show();
        }
    };

	@Override
	public void onBackPressed() {
    	Intent intent = new Intent(getApplicationContext(), EnterPaysActivity.class) ;
    	intent.putExtra(Constants.BUNDLE_CALCULATION_OBJECT, calc);
    	startActivity(intent);
    	finish();
	}
}
