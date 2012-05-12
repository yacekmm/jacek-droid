package pl.looksok.logic;

import java.io.Serializable;
import java.util.HashMap;

import pl.looksok.utils.FormatterHelper;


public class PeoplePays implements Serializable{

	private static final long serialVersionUID = 4909331903428866567L;
	private String personName;
	private double howMuchIPaid;
	private double toReturn;
	private double totalRefundForThisPerson;
	private HashMap<String, InputData> otherPeoplePayments;
	private double howMuchIShouldPay;
	private double alreadyReturned;
	
	public PeoplePays(String _personName, HashMap<String, InputData> inputPays) {
		setPersonName(_personName);
		setPayMadeByPerson(inputPays.get(getPersonName()).getPay());
		
		otherPeoplePayments = inputPays;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public double getPayMadeByPerson() {
		return howMuchIPaid;
	}

	public void setPayMadeByPerson(Double payMadeByPerson) {
		this.howMuchIPaid = payMadeByPerson;
	}

	public void calculateToReturnAndRefund(double _howMuchPerPerson) {
		howMuchIShouldPay = FormatterHelper.roundDouble(_howMuchPerPerson, 2);
		
		calculateHowMuchIShouldReturn();
		calculateHowMuchRefundIShouldHave();
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
	
	public double howMuchShouldReturnTo(String personB){
		InputData personBData = otherPeoplePayments.get(personB);
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
			otherPeoplePayments.get(personB).addToAlreadyRefunded(tmpToReturn);
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

	
}
