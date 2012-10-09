package pl.looksok.activity.addperson;

import java.util.HashSet;

import pl.looksok.R;
import pl.looksok.logic.PersonData;
import pl.looksok.utils.FormatterHelper;
import pl.looksok.utils.exceptions.BadInputDataException;

public class AddNewPersonMulti extends AddNewPersonBase {

	@Override
	protected int getAddPersonContentView() {
		return R.layout.add_new_multi_person;
	}

	@Override
	protected int getPayInputResId() {
		return R.id.enterPaysMulti_EditText_Pay;
	}

	@Override
	protected PersonData getNewInputDataToAdd() throws BadInputDataException {
		double payDouble = FormatterHelper.readDoubleFromEditText(mNewPersonPayInput);
		// TODO Auto-generated method stub
		return new PersonData("Osoba 1", payDouble, new HashSet<String>());
	}

}
