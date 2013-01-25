package pl.looksok.activity.addperson.utils;

import java.util.List;

import pl.looksok.R;
import pl.looksok.logic.PersonData;
import android.content.Context;
import android.widget.Toast;

public class InputValidator {
	public static boolean inputIsValid(Context context, String name, double payDouble, double shouldPayDouble, boolean equalPayments, List<PersonData> inputPaysList) {
		if(name.length()<1 || payDouble < 0){
    		Toast.makeText(context, context.getResources().getString(R.string.addPerson_error_emptyNameField), Toast.LENGTH_SHORT).show();
    		return false;
    	}else if(duplicatedName(name, inputPaysList)){
    		Toast.makeText(context, context.getResources().getString(R.string.addPerson_error_duplicatedPersonName), Toast.LENGTH_SHORT).show();
    		return false;
    	}else if(shouldPayDouble < 0 && !equalPayments){
    		Toast.makeText(context, context.getResources().getString(R.string.addPerson_error_shouldPayValue), Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	
    	return true;
	}

	public static boolean inputIsValid(Context context, String name, double payDouble, List<PersonData> inputPaysList) {
		if(name.length()<1 || payDouble < 0){
			Toast.makeText(context, context.getResources().getString(R.string.addPerson_error_emptyNameField), Toast.LENGTH_SHORT).show();
			return false;
		}else if(duplicatedName(name, inputPaysList)){
			Toast.makeText(context, context.getResources().getString(R.string.addPerson_error_duplicatedPersonName), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}

	private static boolean duplicatedName(String name, List<PersonData> inputPaysList) {
		for (PersonData in : inputPaysList) {
			if (in.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	public static double validatePaysConsistency(List<PersonData> inputPaysList, boolean equalPayments, double editedPay, double editedShouldPay) {
		double totalPayMade = editedPay;
		double totalShouldPaysDeclared = editedShouldPay;
		double result = 0.0;
		
		if(!equalPayments){
			for (PersonData data : inputPaysList) {
				totalPayMade += data.getHowMuchPersonPaid();
				totalShouldPaysDeclared += data.getHowMuchPersonShouldPay();
			}
			result = totalPayMade - totalShouldPaysDeclared;
		}
		
		return result;
	}
}
