package pl.looksok.utils;

import java.math.BigDecimal;

public class FormatterHelper {
	public static double roundDouble(double value, int decimalPlaces){
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
	    double result = bd.doubleValue();
	    return result;
	}
}
