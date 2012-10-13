package pl.looksok.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import pl.looksok.logic.utils.CalculationPrinter;
import pl.looksok.utils.FormatterHelper;
import pl.looksok.utils.exceptions.BadInputDataException;
import pl.looksok.utils.exceptions.BadPayException;
import pl.looksok.utils.exceptions.BadPeopleCountException;
import pl.looksok.utils.exceptions.DuplicatePersonNameException;
import pl.looksok.utils.exceptions.PaysNotCalculatedException;

public class CalculationLogic implements Serializable {
	private static final long serialVersionUID = -1238265432953764569L;
	@SuppressWarnings("unused")
	private static final String LOG_TAG = CalculationLogic.class.getSimpleName();
	private Hashtable<String, PersonData> calculationResult;
	private List<PersonData> inputPaysList = null;
	private boolean equalPayments = true;
	private DateTime dateSaved;
	private CalculationType calculationType = CalculationType.DEFAULT;
	private String calcName = "";
	private CalculationLogic giftCalc = null;
	private double giftValue = 0;
	
	public Hashtable<String, PersonData> getCalculationResult() {
		return calculationResult;
	}

	public CalculationLogic(){
		calculationResult = new Hashtable<String, PersonData>();
		inputPaysList = new ArrayList<PersonData>();
	}
	
	public CalculationLogic(String title){
		this();
		setCalcName(title);
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
			calculateGiftsRefunds(inputPaysList);
		}
		
		return calculate(inputPays);
	}

	private void calculateGiftsRefunds(List<PersonData> inputPaysList) {
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

	private HashMap<String, PersonData> convertAndValidateInput() {
		HashMap<String, PersonData> inputPays = new HashMap<String, PersonData>();
		double sumOfAllPays = 0.0;
		double sumOfAllShouldPays = 0.0;
		
		for (PersonData in : this.inputPaysList) {
			if(inputPays.containsKey(in.getName()))
				throw new DuplicatePersonNameException(in.getName());
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
		if(giftCalc != null)
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
			if(!equalPayments)
				howMuchPersonShouldPay = inputPays.get(key).getHowMuchPersonShouldPay();
			else{
//				if(howMuchPersonShouldPay<0)
					howMuchPersonShouldPay = howMuchPerPerson(totalPay, peopleCount);
				inputPays.get(key).setHowMuchPersonShouldPay(howMuchPersonShouldPay);
			}
			
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
		calculationResult = removeLoopRefunds(newCalculationResult);
	}

	private Hashtable<String, PersonData> removeLoopRefunds(Hashtable<String, PersonData> newCalculationResult) {

		Iterator<String> itMain = newCalculationResult.keySet().iterator();
		while(itMain.hasNext()){
			String personName = itMain.next();
			HashMap<String, Double> refundForOtherPeople = newCalculationResult.get(personName).getRefundForOtherPeople();
			Iterator<String> itReturnList = refundForOtherPeople.keySet().iterator();
			while(itReturnList.hasNext()){
				String refundPersonName = itReturnList.next();
				double myRefundForHim = newCalculationResult.get(personName).getRefundForOtherPeople().get(refundPersonName);
				if(myRefundForHim > 0){
					double hisRefundToMe = newCalculationResult.get(refundPersonName).getRefundForOtherPeople().get(personName);
					if(hisRefundToMe > 0){
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
					}
				}
			}
		}
		return newCalculationResult;
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
		sb.append(CalculationPrinter.printCalcResultForResultsList(calculationResult, "Calculation results", "should return", "for"));
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
		return findPersonInList(pd.getName());
	}

	public PersonData findPersonInList(String personName) {
		for (PersonData data : getInputPaysList()) {
			if(data.getName().equals(personName)){
				return data;
			}
		}
		return null;
	}

	public void removePerson(PersonData pd) {
		inputPaysList.remove(pd);
		getCalculationResult().remove(pd.getName());
	}

	public DateTime getDateSaved() {
		if(dateSaved == null)
			dateSaved = DateTime.now();
		return dateSaved;
	}

	public void setDateSaved(DateTime dateSaved) {
		this.dateSaved = dateSaved;
	}

	public CalculationType getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(CalculationType calculationType) {
		this.calculationType = calculationType;
	}

	public String getCalcName() {
		return calcName;
	}

	public void setCalcName(String calcName) {
		this.calcName = calcName;
	}

	public CalculationLogic getGiftCalc() {
		return giftCalc;
	}

	public void setGiftCalc(CalculationLogic giftCalc) {
		this.giftCalc = giftCalc;
	}

	public double getGiftValue() {
		return giftValue;
	}

	public void setGiftValue(double giftValue) {
		this.giftValue = giftValue;
	}

	public int getTotalPay() {
		double total = 0;
		for (PersonData data : getInputPaysList()) {
			total += data.getPayMadeByPerson();
		}
		return (int)Math.round(total);
	}

	public int getTotalPersons() {
		return getInputPaysList().size();
	}

	public HashMap<String, Double> getPersonDebts(String personName) {
		PersonData pd = calculationResult.get(personName);
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		Iterator<String> it = pd.getRefundForOtherPeople().keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Double value = pd.getRefundForOtherPeople().get(key);
			if(value > 0){
				result.put(key, value);
			}
		}
		return result;
	}

	public HashMap<String, Double> getPersonRefunds(String personName) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		Iterator<String> it = getCalculationResult().keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			if(key.equals(personName))
				continue;
			
			Double value = howMuchPersonAGivesBackToPersonB(key, personName);
			if(value > 0){
				result.put(key, value);
			}
		}
		return result;
	}
}
