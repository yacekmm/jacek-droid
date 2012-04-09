package pl.looksok.logic;

import java.util.HashMap;


public class PeoplePays {

	private String personName;
	private double howMuchIPaid;
	private double toReturn;
	private double totalRefundForThisPerson;
	private HashMap<String, Double> otherPeoplePayments;
	private double howMuchPerPerson;
	private double alreadyReturned;
	
	public PeoplePays(String _personName, HashMap<String, Double> inputPays) {
		setPersonName(_personName);
		setPayMadeByPerson(inputPays.get(getPersonName()));
		
		otherPeoplePayments = inputPays;
		//otherPeoplePayments.remove(_personName);
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

	public void calculateToReturn(double _howMuchPerPerson) {
		howMuchPerPerson = _howMuchPerPerson;
		
		calculateHowMuchIShouldReturn();
		calculateHowMuchRefundIShouldHave();
	}

	private void calculateHowMuchIShouldReturn() {
		toReturn = howMuchPerPerson - howMuchIPaid;
		if(toReturn < 0.0)
			toReturn = 0.0;
	}

	private void calculateHowMuchRefundIShouldHave() {
		totalRefundForThisPerson = howMuchIPaid - howMuchPerPerson;
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
		double howMuchPersonBPaid = otherPeoplePayments.get(personB);
		if(howMuchPersonBPaid > howMuchIPaid){
			double tmpToReturn = howMuchPerPerson - howMuchIPaid;
			if(personBNeedsMoreThanIShouldGive(tmpToReturn)){
				tmpToReturn = splitMyReturnAmount();
			}else if(personBWantsLessThanIHaveToReturn(howMuchPersonBPaid, tmpToReturn)){
				tmpToReturn = givePersonBNoMoreThanHeWants(howMuchPersonBPaid);
			}
			else{
				alreadyReturned += tmpToReturn;
			}
			return tmpToReturn;
		}else
			return 0.0;
	}

	private double givePersonBNoMoreThanHeWants(double howMuchPersonBPaid) {
		double tmpToReturn;
		tmpToReturn = howMuchPersonBPaid - howMuchPerPerson;
		alreadyReturned += tmpToReturn;
		return tmpToReturn;
	}

	private boolean personBWantsLessThanIHaveToReturn(
			double howMuchPersonBPaid, double tmpToReturn) {
		return howMuchPersonBPaid - tmpToReturn < howMuchPerPerson;
	}

	private double splitMyReturnAmount() {
		double tmpToReturn;
		tmpToReturn = toReturn - alreadyReturned;
		alreadyReturned = toReturn;
		return tmpToReturn;
	}

	private boolean personBNeedsMoreThanIShouldGive(double tmpToReturn) {
		return alreadyReturned + tmpToReturn > toReturn;
	}

}
