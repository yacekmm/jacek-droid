package pl.looksok.logic.utils;

import java.util.Currency;
import java.util.HashMap;
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

	public static String printPersonReturnsToOthers(PersonData pd, String returnToText, String forText){
		StringBuilder sb = new StringBuilder(pd.getName());
		sb.append(" ").append(returnToText).append(":\n");
		HashMap<String, Double> returnMap = pd.getPersonDebts();

		if(returnMap.size()<=0)
			return "";

		return printCalculationList(forText, sb, returnMap);
	}
	
	public static String printPersonRefundsFromOthers(PersonData pd, String returnToText, String forText){
		StringBuilder sb = new StringBuilder(pd.getName());
		sb.append(" ").append(returnToText).append(":\n");
		HashMap<String, Double> refundMap = pd.getReturnsFromOtherPeople();
		
		if(refundMap.size()<=0)
			return "";
		
		return printCalculationList(forText, sb, refundMap);
	}

	public static String printPersonReturnsToOthersSimple(PersonData pd) {
		String prefix = "-";
		HashMap<String, Double> refundMap = pd.getRefundForOtherPeople();
		return simplePrinter(prefix, refundMap);
	}

	public static String printPersonRefundsFromOthersSimple(PersonData pd) {
		String prefix = "+";
		HashMap<String, Double> refundMap = pd.getReturnsFromOtherPeople();
		return simplePrinter(prefix, refundMap);
	}

	private static String simplePrinter(String prefix, HashMap<String, Double> payMap) {
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		Iterator<String> it = payMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double value = FormatterHelper.roundDouble(payMap.get(key), 2);
			if(value > 0){
				if(!first){
					first = false;
					sb.append(",   ");
				}
				sb.append(prefix).append(value).append(Currency.getInstance(Locale.getDefault()).getSymbol()).append(" ").append(key);
			}
		}
		
		return sb.toString();
	}

	private static String printCalculationList(String separator, StringBuilder sb, HashMap<String, Double> paysMap) {
		Iterator<String> it = paysMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double value = paysMap.get(key);
			sb.append(value).append(" ").append(Currency.getInstance(Locale.getDefault()).getSymbol()).append(" ");
			sb.append(separator).append(": ").append(key).append("\n");
		}

		return sb.toString();
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
