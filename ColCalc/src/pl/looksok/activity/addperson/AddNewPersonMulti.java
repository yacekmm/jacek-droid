package pl.looksok.activity.addperson;

import java.util.HashSet;

import pl.looksok.R;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.utils.FormatterHelper;
import android.widget.EditText;

public class AddNewPersonMulti extends AddNewPersonBase {

	private EditText mHowManyPersons;

	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_new_multi_person;
	}

	@Override
	protected int getPayInputResId() {
		return R.id.enterPaysMulti_EditText_Pay;
	}
	
	@Override
	protected void initActivityViews() {
		super.initActivityViews();
		mHowManyPersons = (EditText)findViewById(R.id.enterPaysMulti_EditText_peopleCount);
		mHowManyPersons.setOnFocusChangeListener(editTextFocusListener);
		
	}

	@Override
	protected HashSet<PersonData> getNewInputDataToAdd() throws BadInputDataException {
		HashSet<PersonData> personDataSet = new HashSet<PersonData>();
		double payDouble = FormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
		double howManyPersons = FormatterHelper.readDoubleFromEditText(mHowManyPersons);
		String namePrefix = getString(R.string.enterPaysMulti_personPrefix);
		
		int nameOffset = 0;
		for(int i = 1; i<=howManyPersons; i++){
			String name = namePrefix + " " + (i + nameOffset);
			while(inputListContainsPerson(name)){
				nameOffset++;
				name = namePrefix + " " + (i + nameOffset);
			}
			
			personDataSet.add(new PersonData(name, payDouble, emails));
		}
		return personDataSet;
	}

	private boolean inputListContainsPerson(String personName){
		for (PersonData pd : inputPaysList) {
			if(pd.getName().equals(personName))
					return true;
		}
		return false;
	}
}
