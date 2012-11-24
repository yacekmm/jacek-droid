package pl.looksok.currencyedittext.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import android.util.Log;
import android.widget.EditText;

public class FormatterHelper {
	private static final String TAG = FormatterHelper.class.getSimpleName();

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
    		payDouble = Double.parseDouble(payString);
		return payDouble;
	}
	
	public static String currencyFormat(double value, int fractionDigits){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMaximumFractionDigits(fractionDigits);
		return(nf.format(value));
	}
	
	public static double decodeValueFromCurrency(String text){
		try {
			Number number = NumberFormat.getCurrencyInstance().parse(text);
			return Double.parseDouble(number.toString());
		} catch (ParseException e) {
			Log.e(TAG, "ParseException: " + e.getMessage());
			Log.e(TAG, "Text: " + text);
			return Double.parseDouble(text);
		}
	}
}
