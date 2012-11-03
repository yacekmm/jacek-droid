package pl.looksok.logic.utils;

import java.util.HashMap;
import java.util.Iterator;

import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;

public class CalculationUtils {
	public static HashMap<String, PersonData> correctLoopRefund(
			HashMap<String, PersonData> newCalculationResult,
			String personName, String refundPersonName, double myRefundForHim,
			double hisRefundToMe) {
		double correctedMyRefundForHim;
		double correctedHisRefundToMe;
		if(myRefundForHim > hisRefundToMe){
			correctedMyRefundForHim = myRefundForHim - hisRefundToMe;
			correctedHisRefundToMe = 0;
		}else{
			correctedMyRefundForHim = 0;
			correctedHisRefundToMe = hisRefundToMe = myRefundForHim;
		}
		newCalculationResult.get(personName).getRefundForOtherPeople().put(refundPersonName, correctedMyRefundForHim);

		newCalculationResult.get(refundPersonName).getRefundForOtherPeople().put(personName, correctedHisRefundToMe);
		return newCalculationResult;
	}
	
	public static HashMap<String, PersonData> includeGiftPaymentsInCalculation(
			HashMap<String, PersonData> newCalculationResult, CalculationLogic giftCalc) {
		HashMap<String, PersonData> giftCalcResult = giftCalc.getCalculationResult();
		Iterator<String> giftIt = giftCalcResult.keySet().iterator();
		while(giftIt.hasNext()){
			PersonData giftGiverPerson = giftCalcResult.get(giftIt.next());
			PersonData giverInMainCalc = newCalculationResult.get(giftGiverPerson.getName());
			
			HashMap<String, Double> giftGiverRefund = giftGiverPerson.getRefundForOtherPeople();
			Iterator<String> giftGiverIter = giftGiverRefund.keySet().iterator();
			while(giftGiverIter.hasNext()){
				String key = giftGiverIter.next();
				double value = giftGiverRefund.get(key);
				
				giverInMainCalc.increaseRefund(key, value);
			}
		}
		return newCalculationResult;
	}
}
