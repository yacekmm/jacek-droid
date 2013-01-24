package pl.looksok.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

import pl.looksok.currencyedittext.utils.FormatterHelper;

import android.widget.EditText;

public class CalcFormatterHelper {
	public static double roundDouble(double value, int decimalPlaces){
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
	    double result = bd.doubleValue();
	    return result;
	}
	
	public static double readDoubleFromEditText(EditText mEditText) {
		String payString = mEditText.getText().toString();
    	
    	double payDouble = 0.0;
    	if(payString.length() > 0)
    		try{
    			payDouble = Double.parseDouble(payString);
    		}catch (NumberFormatException e) {
				payDouble = FormatterHelper.decodeValueFromCurrency(payString);
			}
		return payDouble;
	}
	
	public static String currencyFormat(double value, int fractionDigits){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMaximumFractionDigits(fractionDigits);
		return(nf.format(value));
	}
	
	public static String currencyFormat(double value){
		return currencyFormat(value, 2);
	}
}
