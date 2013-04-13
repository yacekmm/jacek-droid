package pl.looksok.currencyedittext.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;

import android.widget.EditText;

public class FormatterHelper{
	@SuppressWarnings("unused")
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
		nf.setCurrency(Currency.getInstance("USD"));
		nf.setMaximumFractionDigits(fractionDigits);
		return(nf.format(value));
	}

	public static String currencyFormat(double value){
		return currencyFormat(value, 2);
	}

	public static double decodeValueFromCurrency(String text){
		try {
			Number number = NumberFormat.getCurrencyInstance().parse(text);
			return Double.parseDouble(number.toString());
		} catch (ParseException e) {
			if(text.length() == 0 || text.equals("0"))
				return 0.0;
			else
				return Double.parseDouble(text);
		}
	}
}
