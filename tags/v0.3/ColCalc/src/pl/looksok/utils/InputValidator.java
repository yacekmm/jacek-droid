package pl.looksok.utils;

import java.util.List;

import pl.looksok.R;
import pl.looksok.logic.InputData;
import android.content.Context;
import android.widget.Toast;

public class InputValidator {
	public static boolean inputIsValid(Context context, String name, double payDouble, double shouldPayDouble, boolean equalPayments, List<InputData> inputPaysList) {
		if(name.length()<1 || payDouble < 0){
    		Toast.makeText(context, context.getResources().getString(R.string.EnterPays_Toast_BadNameAndPayError), Toast.LENGTH_SHORT).show();
    		return false;
    	}else if(duplicatedName(name, inputPaysList)){
    		Toast.makeText(context, context.getResources().getString(R.string.EnterPays_Toast_DuplicatedNameError), Toast.LENGTH_SHORT).show();
    		return false;
    	}else if(shouldPayDouble < 0 && !equalPayments){
    		Toast.makeText(context, context.getResources().getString(R.string.EnterPays_Toast_ShouldPayError), Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	
    	return true;
	}

	private static boolean duplicatedName(String name, List<InputData> inputPaysList) {
		for (InputData in : inputPaysList) {
			if (in.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	public static double validatePaysConsistency(List<InputData> inputPaysList, boolean equalPayments, double editedPay, double editedShouldPay) {
		double totalPayMade = editedPay;
		double totalShouldPaysDeclared = editedShouldPay;
		double result = 0.0;
		
		if(!equalPayments){
			for (InputData data : inputPaysList) {
				totalPayMade += data.getPay();
				totalShouldPaysDeclared += data.getShouldPay();
			}
			result = totalPayMade - totalShouldPaysDeclared;
		}
		
		return result;
	}
}
