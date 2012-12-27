package pl.looksok.logic.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class FormatterHelper {
	public static double roundDouble(double value, int decimalPlaces){
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
	    double result = bd.doubleValue();
	    return result;
	}
	
	public static String currencyFormat(double value, int fractionDigits){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMaximumFractionDigits(fractionDigits);
		return(nf.format(value));
	}
}
