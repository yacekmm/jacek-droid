package pl.looksok.logic.utils;

import java.util.HashMap;
import java.util.Iterator;

import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;

public class CalculationUtils {
	

	protected static HashMap<String, PersonData> giftIncluderV1(
			HashMap<String, PersonData> newCalculationResult,
			CalculationLogic giftCalc) {
		HashMap<String, PersonData> giftCalcResult = giftCalc.getCalculationResult();
		Iterator<String> giftIt = giftCalcResult.keySet().iterator();
		while(giftIt.hasNext()){
			PersonData giftGiverPerson = giftCalcResult.get(giftIt.next());
			PersonData giverInMainCalc = newCalculationResult.get(giftGiverPerson.getName());
			
			HashMap<String, Double> giftGiverRefund = giftGiverPerson.getMyDebts();
			Iterator<String> giftGiverIter = giftGiverRefund.keySet().iterator();
			while(giftGiverIter.hasNext()){
				String key = giftGiverIter.next();
				double value = giftGiverRefund.get(key);
				
				giverInMainCalc.increaseRefund(key, value);
			}
		}
		return newCalculationResult;
	}

	public static Double calculateTotalPayValue(HashMap<String, PersonData> inputPays) {
		Double totalPay = 0.0;
		
		Iterator<PersonData> itr = inputPays.values().iterator();

		while (itr.hasNext()){
			totalPay += itr.next().getHowMuchPersonPaid();
		}
		return totalPay;
	}
}
