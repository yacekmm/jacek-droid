package pl.looksok.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.exceptions.BadPeopleCountException;
import pl.looksok.logic.exceptions.DuplicatePersonNameException;
import pl.looksok.logic.exceptions.PaysNotCalculatedException;
import pl.looksok.logic.utils.CalculationPrinter;
import pl.looksok.logic.utils.CalculationUtils;
import pl.looksok.logic.utils.FormatterHelper;
import pl.looksok.logic.utils.PersonDataUtils;

public class CalculationLogic implements Serializable {
	private static final long serialVersionUID = -1238265432953764569L;
	
	private long id = Calendar.getInstance().getTimeInMillis();
	private HashMap<String, PersonData> calculationResult;
	private List<PersonData> inputPaysList = null;
	private boolean equalPayments = true;
	private DateTime dateSaved;
	private CalculationType calculationType = CalculationType.DEFAULT;
	private String calcName = "";
	private CalculationLogic giftCalc = null;
	
	public HashMap<String, PersonData> getCalculationResult() {
		return calculationResult;
	}

	public CalculationLogic(){
		calculationResult = new HashMap<String, PersonData>();
		inputPaysList = new ArrayList<PersonData>();
	}

	private double howMuchPerPerson(double totalPay, int peopleCount) {
		if(totalPay < 0)
			throw new BadInputDataException();
		if(peopleCount < 0)
			throw new BadPeopleCountException();
		
		return totalPay / peopleCount;
	}

	public HashMap<String, PersonData> calculate(List<PersonData> inputPaysList) throws DuplicatePersonNameException{
		this.inputPaysList = inputPaysList;
		HashMap<String, PersonData> inputPays = convertAndValidateInput();
		
		if(calculationType.equals(CalculationType.POTLUCK_PARTY_WITH_GIFT)){
			calculateGiftsRefunds(inputPaysList);
		}
		
		HashMap<String, PersonData> result = calculate(inputPays);
		return result;
	}

	private void calculateGiftsRefunds(List<PersonData> inputPaysList) {
		giftCalc = new CalculationLogic();
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
				PersonData giverPersonData = new PersonData(pd.getName(), PersonDataUtils.getDefaultAtomPaymentsList(howMuchPersonPaidForGift), shouldPayForGift, null);
				giftGivers.add(giverPersonData);
			}
		}
		
		if(giftGivers.size()>0){
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
	

	public HashMap<String, PersonData> recalculate() {
		System.out.println("Recalculating");
		resetInputData();
		if(giftCalc != null){
			giftCalc.recalculate();
		}
		HashMap<String, PersonData> inputPays = convertAndValidateInput();
		return calculate(inputPays);
	}

	private void resetInputData() {
		setCalculationResult(new HashMap<String, PersonData>());
		for (PersonData data : getInputPaysList()) {
//			data = new PersonData(data.getName(), data.getAtomPayments(), data.getEmails(), data.receivesGift(), data.getHowMuchIPaidForGift());
			data.setAlreadyRefunded(0.0);
		}
	}
	
	private HashMap<String, PersonData> calculate(HashMap<String, PersonData> inputPays) {
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
		HashMap<String, PersonData> newCalculationResult = new HashMap<String, PersonData>();
		
		Iterator<String> it = calculationResult.keySet().iterator();
		while (it.hasNext()){
			PersonData pp = calculationResult.get(it.next());
			pp.calculateRefundToOthers(calculationResult);
			newCalculationResult.put(pp.getName(), pp);
		}
		
		if(getCalculationType().equals(CalculationType.POTLUCK_PARTY_WITH_GIFT)){
			newCalculationResult = CalculationUtils.includeGiftPaymentsInCalculation(newCalculationResult, giftCalc);
		}
		calculationResult = removeLoopRefunds(newCalculationResult);
		
		
		Iterator<String> itMain = calculationResult.keySet().iterator();
		while(itMain.hasNext()){
			String personName = itMain.next();
			setPersonRefundsFromOthers(personName);
		}
	}

	private HashMap<String, PersonData> removeLoopRefunds(HashMap<String, PersonData> newCalculationResult) {

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
						newCalculationResult = CalculationUtils.correctLoopRefund(newCalculationResult, personName, refundPersonName, myRefundForHim, hisRefundToMe);
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
			return calculationResult.get(personA).getCalculatedReturnForPersonB(personB);
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
	
	public void setInputPaysList(List<PersonData> list) {
		this.inputPaysList = list;
	}

	public void setCalculationResult(HashMap<String, PersonData> calculationResult) {
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
		return pd.getPersonDebts();
		
	}

	public HashMap<String, Double> setPersonRefundsFromOthers(String personName) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		Iterator<String> it = getCalculationResult().keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			if(key.equals(personName))
				continue;
			
			Double value = howMuchPersonAGivesBackToPersonB(key, personName);
			if(value > 0){
				result.put(key, FormatterHelper.roundDouble(value, 2));
			}
		}
		PersonData pd = calculationResult.get(personName);
		pd.setRefundsFromOtherPeople(result);
		return pd.getRefundsFromOtherPeople();
	}

	public long getId() {
		return id;
	}

	public PersonData getPerson(String personName) {
		return calculationResult.get(personName);
	}
}
