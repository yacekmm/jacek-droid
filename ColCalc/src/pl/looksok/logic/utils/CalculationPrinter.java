package pl.looksok.logic.utils;

import java.util.Currency;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import pl.looksok.logic.PersonData;

public class CalculationPrinter {
	/**
	 * returns String with calculation result like:
	 * [titleText]:
	 * [personName] [returnToText]:
	 * [calculatedReturnValue] [forText] [otherPersonName] 
	 * @param calculationResult 
	 * @param titleText
	 * @param returnToText
	 * @return
	 */
	public static String printCalcResultForResultsList(Hashtable<String, PersonData> calculationResult, String titleText, String returnToText, String forText){
		StringBuilder sb = new StringBuilder(titleText).append(":\n");

		if(calculationResult != null){
			Iterator<String> it = calculationResult.keySet().iterator();

			while (it.hasNext()){
				PersonData pp = calculationResult.get(it.next());
				sb.append(printPersonReturnsToOthers(pp, returnToText, forText)).append("\n");
			}
		}

		return sb.toString();
	}

	/**
	 * returns String with calculation result like:
	 * [titleText]:
	 * [personName] [howMuchPaidText]: [paidValue] ([howMuchShouldPayText]: [shouldPayValue]) [returnToText]:
	 * [calculatedReturnValue] [forText] [otherPersonName] 
	 * 
	 * example: 
	 * Calculation Result:
	 * person A paid: $20 (but should pay: $30) and has to return money to:
	 * $10 to personB
	 * @param calculationResult 
	 * @param titleText
	 * @param returnToText
	 * @param endOfLine 
	 * @return
	 */
	public static String printCalcResultForEmail(Hashtable<String, PersonData> calculationResult, String titleText, String howMuchPaidText, 
			String howMuchShouldPayText, String returnToText, String forText, String endOfLine){
		StringBuilder sb = new StringBuilder(titleText).append(":").append(endOfLine);

		if(calculationResult != null){
			Iterator<String> it = calculationResult.keySet().iterator();

			while (it.hasNext()){
				PersonData pp = calculationResult.get(it.next());
				sb.append(printPersonReturnsToOthersDetails(pp, howMuchPaidText, howMuchShouldPayText, returnToText, forText, endOfLine)).append(endOfLine);
			}
		}

		return sb.toString();
	}

	public static String printPersonReturnsToOthers(PersonData pp, String returnToText, String forText){
		StringBuilder sb = new StringBuilder(pp.getName());
		sb.append(" ").append(returnToText).append(":\n");
		boolean isThereAnyPersonOnReturnList = false;

		Iterator<String> it = pp.getRefundForOtherPeople().keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double result = pp.getRefundForOtherPeople().get(key);
			if(result >0){
				isThereAnyPersonOnReturnList = true;
				sb.append(result).append(" ").append(Currency.getInstance(Locale.getDefault()).getSymbol()).append(" ");
				sb.append(forText).append(": ").append(key).append("\n");
			}
		}

		if(isThereAnyPersonOnReturnList)
			return sb.toString();
		else
			return "";
	}

	public static String printPersonReturnsToOthersDetails(PersonData pp, String howMuchPaidText, String howMuchShouldPayText, String returnToText, String forText, String endOfLine){
		String currency = Currency.getInstance(Locale.getDefault()).getSymbol();

		StringBuilder sb = new StringBuilder(pp.getName()).append(" - ").append(howMuchPaidText).append(": ").append(pp.getPayMadeByPerson()).append(currency).append(" ");
		sb.append("(").append(howMuchShouldPayText).append(": ").append(pp.getHowMuchPersonShouldPay()).append(currency).append(")");

		String messageIfNoReturnNeeded = sb.toString() + endOfLine;

		sb.append(", ").append(returnToText).append(":").append(endOfLine);
		boolean isThereAnyPersonOnReturnList = false;

		Iterator<String> it = pp.getRefundForOtherPeople().keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double result = pp.getRefundForOtherPeople().get(key);
			if(result >0){
				isThereAnyPersonOnReturnList = true;
				sb.append(result).append(" ").append(currency).append(" ");
				sb.append(forText).append(": ").append(key).append(endOfLine);
			}
		}

		if(isThereAnyPersonOnReturnList)
			return sb.toString();
		else
			return messageIfNoReturnNeeded;
	}

}
