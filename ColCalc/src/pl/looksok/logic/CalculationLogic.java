package pl.looksok.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pl.looksok.utils.FormatterHelper;
import pl.looksok.utils.exceptions.BadInputDataException;
import pl.looksok.utils.exceptions.BadPayException;
import pl.looksok.utils.exceptions.BadPeopleCountException;
import pl.looksok.utils.exceptions.DuplicatePersonNameException;
import pl.looksok.utils.exceptions.PaysNotCalculatedException;

public class CalculationLogic implements Serializable {
	private static final long serialVersionUID = -1238265432953764569L;
	private Hashtable<String, PersonData> calculationResult;
	private List<PersonData> inputPaysList = null;
	private boolean equalPayments = true;
	private Calendar dateSaved;
	private CalculationType calculationType = CalculationType.DEFAULT;
	private String calcTitle = "";
	private CalculationLogic giftCalc = null;
	
	public Hashtable<String, PersonData> getCalculationResult() {
		return calculationResult;
	}

	public CalculationLogic(String title){
		calculationResult = new Hashtable<String, PersonData>();
		setCalcTitle(title);
	}

	private double howMuchPerPerson(double totalPay, int peopleCount) {
		if(totalPay < 0)
			throw new BadPayException();
		if(peopleCount < 0)
			throw new BadPeopleCountException();
		
		return totalPay / peopleCount;
	}

	public Hashtable<String, PersonData> calculate(List<PersonData> inputPaysList) throws DuplicatePersonNameException{
		this.inputPaysList = inputPaysList;
		HashMap<String, PersonData> inputPays = convertAndValidateInput();
		
		if(calculationType.equals(CalculationType.POTLUCK_PARTY_WITH_GIFT)){
			giftCalc = new CalculationLogic("giftCalc");
			giftCalc.setCalculationType(CalculationType.EQUAL_PAYMENTS);
			List<PersonData> giftGivers = new ArrayList<PersonData>();
			
			double giftValue = 0.0;
			int giftGiversCount = 0;
			for(PersonData pd: inputPaysList){
				giftValue += pd.getHowMuchIPaidForGift();
				if(!pd.receivesGift())
					giftGiversCount++;
			}
			
			if(giftGiversCount == 0)
				giftValue = 0.0;
					
					
			for(PersonData pd :inputPaysList){
					
				double shouldPayForGift = giftValue/giftGiversCount;
				if(pd.receivesGift())
					shouldPayForGift = 0.0;
				double howMuchPersonPaidForGift = pd.getHowMuchIPaidForGift();
				if(howMuchPersonPaidForGift !=0 || shouldPayForGift !=0){
					PersonData giverPersonData = new PersonData(pd.getName(), howMuchPersonPaidForGift, shouldPayForGift, null);
					giftGivers.add(giverPersonData);
				}
			}
			
			if(giftGivers.size()>1){
				giftCalc.setEqualPayments(false);
				giftCalc.calculate(giftGivers);
			}
		}
		
		return calculate(inputPays);
	}

	private HashMap<String, PersonData> convertAndValidateInput() {
		HashMap<String, PersonData> inputPays = new HashMap<String, PersonData>();
		double sumOfAllPays = 0.0;
		double sumOfAllShouldPays = 0.0;
		
		for (PersonData in : this.inputPaysList) {
			if(inputPays.containsKey(in.getName()))
				throw new DuplicatePersonNameException();
			else{
				inputPays.put(in.getName(), in);
				sumOfAllPays += in.getPayMadeByPerson();
				sumOfAllShouldPays += in.getHowMuchPersonShouldPay();
			}
		}
		
		if(!equalPayments){
			if(sumOfAllPays != sumOfAllShouldPays){
				throw new BadInputDataException("Sum of all Pays made by persons ("+sumOfAllPays+") is not equal to sum of amount that they should pay(" +sumOfAllShouldPays+")");
			}
		}
		return inputPays;
	}
	

	public Hashtable<String, PersonData> recalculate() {
		resetInputData();
		giftCalc.resetInputData();
		HashMap<String, PersonData> inputPays = convertAndValidateInput();
		return calculate(inputPays);
	}

	private void resetInputData() {
		setCalculationResult(new Hashtable<String, PersonData>());
		for (PersonData data : getInputPaysList()) {
			data.setAlreadyRefunded(0.0);
		}
	}
	
	private Hashtable<String, PersonData> calculate(HashMap<String, PersonData> inputPays) {
		double totalPay = calculateTotalPayValue(inputPays);
		int peopleCount = inputPays.size();
		double howMuchPersonShouldPay = -1;
		
		Iterator<String> itr = inputPays.keySet().iterator();
		while (itr.hasNext()){
			String key = itr.next();
//			if(!equalPayments)
				howMuchPersonShouldPay = inputPays.get(key).getHowMuchPersonShouldPay();
//			else{
				if(howMuchPersonShouldPay<0)
					howMuchPersonShouldPay = howMuchPerPerson(totalPay, peopleCount);
				inputPays.get(key).setHowMuchPersonShouldPay(howMuchPersonShouldPay);
//			}
			
			PersonData p = new PersonData(key, inputPays);
			p.prepareCalculationData(howMuchPersonShouldPay);
			calculationResult.put(p.getName(), p);
		}
		
		performCalculation();
		
		return calculationResult;
	}

	private void performCalculation() {
		Hashtable<String, PersonData> newCalculationResult = new Hashtable<String, PersonData>();
		
		Iterator<String> it = calculationResult.keySet().iterator();
		while (it.hasNext()){
			PersonData pp = calculationResult.get(it.next());
			pp.calculateRefundToOthers(calculationResult);
			newCalculationResult.put(pp.getName(), pp);
		}
		
		if(getCalculationType().equals(CalculationType.POTLUCK_PARTY_WITH_GIFT)){
			Hashtable<String, PersonData> giftCalcResult = giftCalc.getCalculationResult();
			Iterator<String> giftIt = giftCalcResult.keySet().iterator();
			while(giftIt.hasNext()){
				PersonData giftGiverPerson = giftCalcResult.get(giftIt.next());
				PersonData giverInMainCalc = newCalculationResult.get(giftGiverPerson.getName());
				
				HashMap<String, Double> giftGiverRefund = giftGiverPerson.getRefundForOtherPeople();
				Iterator<String> giftGiverIter = giftGiverRefund.keySet().iterator();
				while(giftGiverIter.hasNext()){
					String key = giftGiverIter.next();
					double value = giftGiverRefund.get(key);
					
					//increase main refund
					giverInMainCalc.increaseRefund(key, value);
				}
			}
		}

		calculationResult = newCalculationResult;
	}

	private Double calculateTotalPayValue(HashMap<String, PersonData> inputPays) {
		Double totalPay = 0.0;
		
		Iterator<PersonData> itr = inputPays.values().iterator();

		while (itr.hasNext()){
			totalPay += itr.next().getPayMadeByPerson();
		}
		return totalPay;
	}
	
	public double howMuchPersonAGivesBackToPersonB(String personA, String personB) {
		try{
			double result = calculationResult.get(personA).getCalculatedReturnForPersonB(personB);
			return FormatterHelper.roundDouble(result, 2);
		}catch(NullPointerException e){
			throw new PaysNotCalculatedException("Call 'calculate' method before reading results");
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(printCalcResultForResultsList("Calculation results", "should return", "for"));
		return sb.toString();
	}
	
	/**
	 * returns String with calculation result like:
	 * [titleText]:
	 * [personName] [returnToText]:
	 * [calculatedReturnValue] [forText] [otherPersonName] 
	 * @param titleText
	 * @param returnToText
	 * @return
	 */
	public String printCalcResultForResultsList(String titleText, String returnToText, String forText){
		StringBuilder sb = new StringBuilder(titleText).append(":\n");
		
		if(calculationResult != null){
			Iterator<String> it = calculationResult.keySet().iterator();
			
			while (it.hasNext()){
				PersonData pp = calculationResult.get(it.next());
				sb.append(pp.printPersonReturnsToOthers(returnToText, forText)).append("\n");
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
	 * @param titleText
	 * @param returnToText
	 * @param endOfLine 
	 * @return
	 */
	public String printCalcResultForEmail(String titleText, String howMuchPaidText, 
			String howMuchShouldPayText, String returnToText, String forText, String endOfLine){
		StringBuilder sb = new StringBuilder(titleText).append(":").append(endOfLine);
		
		if(calculationResult != null){
			Iterator<String> it = calculationResult.keySet().iterator();
			
			while (it.hasNext()){
				PersonData pp = calculationResult.get(it.next());
				sb.append(pp.printPersonReturnsToOthersDetails(howMuchPaidText, howMuchShouldPayText, returnToText, forText, endOfLine)).append(endOfLine);
			}
		}
		
		return sb.toString();
	}

	public List<PersonData> getInputPaysList() {
		return inputPaysList;
	}

	public void setCalculationResult(Hashtable<String, PersonData> calculationResult) {
		this.calculationResult = calculationResult;
	}

	public boolean isEqualPayments() {
		return equalPayments;
	}

	public void setEqualPayments(boolean equalPayments) {
		this.equalPayments = equalPayments;
	}

	public PersonData findPersonInList(PersonData pd) {
		for (PersonData data : getInputPaysList()) {
			if(data.getName().equals(pd.getName())){
				pd = data;
				break;
			}
		}
		return pd;
	}

	public void removePerson(PersonData pd) {
		inputPaysList.remove(pd);
		getCalculationResult().remove(pd.getName());
	}

	public Calendar getDateSaved() {
		return dateSaved;
	}

	public void setDateSaved(Calendar dateSaved) {
		this.dateSaved = dateSaved;
	}

	public CalculationType getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(CalculationType calculationType) {
		this.calculationType = calculationType;
	}

	public String getCalcTitle() {
		return calcTitle;
	}

	public void setCalcTitle(String calcTitle) {
		this.calcTitle = calcTitle;
	}

	public CalculationLogic getGiftCalc() {
		return giftCalc;
	}

	public void setGiftCalc(CalculationLogic giftCalc) {
		this.giftCalc = giftCalc;
	}
}
