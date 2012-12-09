package pl.looksok.logic.utils;

import java.util.HashMap;
import java.util.Iterator;

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
	public static String printCalcResultForResultsList(HashMap<String, PersonData> calculationResult, String titleText, String returnToText, String forText){
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
	public static String printCalcResultForEmail(HashMap<String, PersonData> calculationResult, String titleText, String howMuchPaidText, 
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
		HashMap<String, Double> refundMap = pd.getMyRefunds();
		
		if(refundMap.size()<=0)
			return "";
		
		return printCalculationList(forText, sb, refundMap);
	}

	public static String printPersonDebtsSimple(PersonData pd) {
//		HashMap<String, Double> refundMap = pd.getRefundForOtherPeople();
		HashMap<String, Double> refundMap = pd.getPersonDebts();
		return simplePrinter("-", refundMap);
	}

	public static String printPersonRefundsFromOthersSimple(PersonData pd) {
		HashMap<String, Double> refundMap = pd.getMyRefunds();
		return simplePrinter("+", refundMap);
	}

	private static String simplePrinter(String prefix, HashMap<String, Double> payMap) {
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		Iterator<String> it = payMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double value = payMap.get(key);
			if(value > 0){
				if(!first){
					sb.append(",    ");
				}else
					first = false;
				sb.append(prefix).append(FormatterHelper.currencyFormat(value, 2)).append(" ").append(key);
			}
		}
		
		return sb.toString();
	}

	private static String printCalculationList(String separator, StringBuilder sb, HashMap<String, Double> paysMap) {
		Iterator<String> it = paysMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double value = paysMap.get(key);
			sb.append(FormatterHelper.currencyFormat(value, 2)).append(" ");
			sb.append(separator).append(": ").append(key).append("\n");
		}

		return sb.toString();
	}
	
	public static String printPersonReturnsToOthersDetails(PersonData pp, String howMuchPaidText, String howMuchShouldPayText, String returnToText, String forText, String endOfLine){
//		String currency = Currency.getInstance(Locale.getDefault()).getSymbol();

		StringBuilder sb = new StringBuilder(pp.getName()).append(" - ").append(howMuchPaidText).append(": ").append(FormatterHelper.currencyFormat(pp.getPayMadeByPerson(),2)).append(" ");
		sb.append("(").append(howMuchShouldPayText).append(": ").append(FormatterHelper.currencyFormat(pp.getHowMuchPersonShouldPay(),2)).append(")");

		String messageIfNoReturnNeeded = sb.toString() + endOfLine;

		sb.append(", ").append(returnToText).append(":").append(endOfLine);
		boolean isThereAnyPersonOnReturnList = false;

		Iterator<String> it = pp.getMyDebts().keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double result = pp.getMyDebts().get(key);
			if(result >0){
				isThereAnyPersonOnReturnList = true;
				sb.append(FormatterHelper.currencyFormat(result,2)).append(" ");
				sb.append(forText).append(": ").append(key).append(endOfLine);
			}
		}

		if(isThereAnyPersonOnReturnList)
			return sb.toString();
		else
			return messageIfNoReturnNeeded;
	}
}
