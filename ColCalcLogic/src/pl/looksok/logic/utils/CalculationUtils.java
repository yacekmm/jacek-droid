package pl.looksok.logic.utils;

import java.util.HashMap;
import java.util.Iterator;

import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;

public class CalculationUtils {
	

	public static HashMap<String, PersonData> removeLoopRefunds(HashMap<String, PersonData> newCalculationResult) {

		Iterator<String> itMain = newCalculationResult.keySet().iterator();
		while(itMain.hasNext()){
			String personName = itMain.next();
			HashMap<String, Double> refundForOtherPeople = newCalculationResult.get(personName).getMyDebts();
			Iterator<String> itReturnList = refundForOtherPeople.keySet().iterator();
			while(itReturnList.hasNext()){
				String refundPersonName = itReturnList.next();
				double myRefundForHim = newCalculationResult.get(personName).getMyDebts().get(refundPersonName);
				if(myRefundForHim > 0){
					double hisRefundToMe = newCalculationResult.get(refundPersonName).getMyDebts().get(personName);
					if(hisRefundToMe > 0){
						newCalculationResult = CalculationUtils.correctLoopRefund(newCalculationResult, personName, refundPersonName, myRefundForHim, hisRefundToMe);
					}
				}
			}
		}
		return newCalculationResult;
	}
	
	public static HashMap<String, PersonData> correctLoopRefund(HashMap<String, PersonData> newCalculationResult,
			String personName, String refundPersonName, double myRefundForHim, double hisRefundToMe) {
		
		double correctedMyRefundForHim;
		double correctedHisRefundToMe;
		if(myRefundForHim > hisRefundToMe){
			correctedMyRefundForHim = myRefundForHim - hisRefundToMe;
			correctedHisRefundToMe = 0;
		}else{
			correctedMyRefundForHim = 0;
			correctedHisRefundToMe = hisRefundToMe - myRefundForHim;
		}
		newCalculationResult.get(personName).getMyDebts().put(refundPersonName, correctedMyRefundForHim);

		newCalculationResult.get(refundPersonName).getMyDebts().put(personName, correctedHisRefundToMe);
		return newCalculationResult;
	}
	
	public static HashMap<String, PersonData> includeGiftPaymentsInCalculation(HashMap<String, PersonData> newCalculationResult, CalculationLogic giftCalc) {
		
//		return giftIncluderV1(newCalculationResult, giftCalc);
		return giftIncluderV2(newCalculationResult, giftCalc);
	}

	private static HashMap<String, PersonData> giftIncluderV2(HashMap<String, PersonData> newCalculationResult, CalculationLogic giftCalc) {
		
		HashMap<String, PersonData> giftCalcResult = giftCalc.getCalculationResult();
		Iterator<String> giftCalcResultIt = giftCalcResult.keySet().iterator();
		while(giftCalcResultIt.hasNext()){
			PersonData giftGiverInGiftCalc = giftCalcResult.get(giftCalcResultIt.next());
			PersonData giftGiverInMainCalc = newCalculationResult.get(giftGiverInGiftCalc.getName());
			
			HashMap<String, Double> giftGiverDebts = giftGiverInGiftCalc.getMyDebts();
			Iterator<String> giftGiverDebtsIter = giftGiverDebts.keySet().iterator();
			while(giftGiverDebtsIter.hasNext()){
				String keyDebtPersonName = giftGiverDebtsIter.next();
				double debtValue = giftGiverDebts.get(keyDebtPersonName);
				
//				if(giftGiverInMainCalc.)
//					
//				else
					giftGiverInMainCalc.increaseRefund(keyDebtPersonName, debtValue);
			}
		}
		
		return newCalculationResult;
	}

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
			totalPay += itr.next().getPayMadeByPerson();
		}
		return totalPay;
	}

	public static HashMap<String, PersonData> forwardedRefundsDetector(HashMap<String, PersonData> newCalculationResult) {
		Iterator<String> it = newCalculationResult.keySet().iterator();
		while(it.hasNext()){
			PersonData pd = newCalculationResult.get(it.next());
			
			if(pd.getPersonDebts().size() > 0 && pd.getMyRefunds().size() > 0){
				System.out.println("ELIGIBLE FOR FORWARDED PAYMENTS!!! " + pd.getName());
//				throw new BadInputDataException("Forwarded payments");
			}
		}
		
		return newCalculationResult;
	}
}
