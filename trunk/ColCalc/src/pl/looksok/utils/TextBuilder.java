package pl.looksok.utils;

import pl.looksok.R;
import pl.looksok.currencyedittext.utils.FormatterHelper;
import pl.looksok.logic.CalculationLogic;
import android.content.Context;

public class TextBuilder {

	public static String buildBadInputDataErrorMessage(Context context, CalculationLogic calc) {
		StringBuilder sb = new StringBuilder();
		sb.append(context.getString(R.string.calcResult_badInputDataError_part1)).append(" ");
		sb.append(FormatterHelper.currencyFormat(calc.getTotalPay()));
		
		sb.append(context.getString(R.string.calcResult_badInputDataError_part2)).append(" ");
		sb.append(FormatterHelper.currencyFormat(calc.getTotalShouldPay()));
		
		sb.append(context.getString(R.string.calcResult_badInputDataError_part3));
		
		return sb.toString();
	}
}
