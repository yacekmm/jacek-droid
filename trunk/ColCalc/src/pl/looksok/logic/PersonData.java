package pl.looksok.logic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import pl.looksok.utils.FormatterHelper;
import pl.looksok.utils.exceptions.PaysNotCalculatedException;


public class PersonData implements Serializable{

	private static final long serialVersionUID = 4909331903428866567L;
	private String name;
	private double howMuchIPaid;
	private double toReturn;
	private double totalRefundForThisPerson;
	private HashMap<String, InputData> otherPeoplePayments;
	private HashMap<String, Double> refundForOtherPeople;
	private double howMuchIShouldPay;
	private double alreadyReturned;
	
	public PersonData(String _personName, HashMap<String, InputData> inputPays) {
		setPersonName(_personName);
		setPayMadeByPerson(inputPays.get(getName()).getPay());
		
		otherPeoplePayments = inputPays;
		refundForOtherPeople = new HashMap<String, Double>();
	}

	public String getName() {
		return name;
	}

	public void setPersonName(String personName) {
		this.name = personName;
	}

	public double getPayMadeByPerson() {
		return howMuchIPaid;
	}

	public void setPayMadeByPerson(Double payMadeByPerson) {
		this.howMuchIPaid = payMadeByPerson;
	}

	public void prepareCalculationData(double _howMuchPerPerson) {
		howMuchIShouldPay = FormatterHelper.roundDouble(_howMuchPerPerson, 2);
		
		calculateHowMuchIShouldReturn();
		calculateHowMuchRefundIShouldHave();
	}

	public void calculateRefundToOthers(Hashtable<String, PersonData> calculationResult) {
		Set<String> c = calculationResult.keySet();
		Iterator<String> it = c.iterator();
		
		while(it.hasNext()) {
			PersonData pp2 = calculationResult.get(it.next());
			if(this.getName() != pp2.getName()){
				double returnValue = howMuchIGiveBackToPersonB(pp2);
				getRefundForOtherPeople().put(pp2.getName(), returnValue);
			}
		}
	}
	
	private double howMuchIGiveBackToPersonB(PersonData personB) {
		try{
			double result = this.howMuchShouldReturnTo(personB.getName());
			return FormatterHelper.roundDouble(result, 2);
		}catch(NullPointerException e){
			throw new PaysNotCalculatedException("Call 'calculate' method before reading results");
		}
	}

	private void calculateHowMuchIShouldReturn() {
		toReturn = howMuchIShouldPay - howMuchIPaid;
		if(toReturn < 0.0)
			toReturn = 0.0;
	}

	private void calculateHowMuchRefundIShouldHave() {
		totalRefundForThisPerson = howMuchIPaid - howMuchIShouldPay;
		if(totalRefundForThisPerson < 0.0)
			totalRefundForThisPerson = 0.0;
	}

	public double getToReturn() {
		return toReturn;
	}

	public double getTotalRefundForThisPerson() {
		return totalRefundForThisPerson;
	}
	
	private double howMuchShouldReturnTo(String personBName){
		InputData personBData = otherPeoplePayments.get(personBName);
		double howMuchPersonBPaid = personBData.getPay();
		double howMuchPersonBShouldPay = personBData.getShouldPay();
		double howMuchRefundPersonBNeeds = howMuchPersonBPaid - howMuchPersonBShouldPay - personBData.getAlreadyRefunded();
		
		if(howMuchRefundPersonBNeeds>0){
			double tmpToReturn = howMuchIShouldPay - howMuchIPaid;
			
			if(personBNeedsMoreThanIShouldGive(tmpToReturn)){
				tmpToReturn = splitMyReturnAmount();
			}else if(personBWantsLessThanIHaveToReturn(howMuchPersonBPaid, howMuchPersonBShouldPay, tmpToReturn, howMuchRefundPersonBNeeds)){
				tmpToReturn = givePersonBNoMoreThanHeWants(howMuchPersonBPaid, howMuchPersonBShouldPay);
			}
			
			if(tmpToReturn > howMuchRefundPersonBNeeds){
				tmpToReturn = howMuchRefundPersonBNeeds;
			}
			
			if(tmpToReturn<0.0)
				tmpToReturn = 0.0;
			
			alreadyReturned += tmpToReturn;
			otherPeoplePayments.get(personBName).addToAlreadyRefunded(tmpToReturn);
			return tmpToReturn;
		}else
			return 0.0;
	}

	private double givePersonBNoMoreThanHeWants(double howMuchPersonBPaid, double howMuchPersonBShouldPay) {
		double tmpToReturn;
		double refundForPersonBNeeded = howMuchPersonBPaid - howMuchPersonBShouldPay;
		if(refundForPersonBNeeded<0)
			tmpToReturn = 0.0;
		
		tmpToReturn = howMuchPersonBPaid - howMuchIShouldPay;
		if(tmpToReturn<0)
			tmpToReturn = 0;
		return tmpToReturn;
	}

	private boolean personBWantsLessThanIHaveToReturn(
			double howMuchPersonBPaid, double howMuchPersonBShouldPay, double tmpToReturn, double howMuchRefundPersonBNeeds) {
		return howMuchRefundPersonBNeeds < howMuchIShouldPay - howMuchIPaid;
	}

	private double splitMyReturnAmount() {
		double tmpToReturn;
		tmpToReturn = toReturn - alreadyReturned;
		return tmpToReturn;
	}

	private boolean personBNeedsMoreThanIShouldGive(double tmpToReturn) {
		return alreadyReturned + tmpToReturn > toReturn;
	}

	public double getHowMuchPersonShouldPay() {
		return howMuchIShouldPay;
	}

	public HashMap<String, Double> getRefundForOtherPeople() {
		return refundForOtherPeople;
	}

	public void setRefundForOtherPeople(HashMap<String, Double> refundForOtherPeople) {
		this.refundForOtherPeople = refundForOtherPeople;
	}

	public double getCalculatedReturnForPersonB(String personB) {
		return getRefundForOtherPeople().get(personB);
	}

	public String printPersonReturnsToOthers(){
		StringBuilder sb = new StringBuilder(getName());
		sb.append(" should return to:\n");
		
		Set<String> c = getRefundForOtherPeople().keySet();
		Iterator<String> it = c.iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			double result = getRefundForOtherPeople().get(key);
			sb.append(key).append(": ").append(result).append("\n");
		}
		
		return sb.toString();
	}
	
}
