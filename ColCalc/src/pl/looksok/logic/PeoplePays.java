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
		
		otherPeoplePayments = new HashMap<String, Double>();
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
		toReturn = howMuchPerPerson - howMuchIPaid;
		if(toReturn < 0.0)
			toReturn = 0.0;
		
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
			if(alreadyReturned + tmpToReturn > toReturn){
				tmpToReturn = toReturn - alreadyReturned;
				alreadyReturned = toReturn;
			}else{
				alreadyReturned += tmpToReturn;
			}
			return tmpToReturn;
			//return howMuchPersonBPaid - (howMuchPerPerson*(otherPeoplePayments.size()-1));
		}else
			return 0.0;
	}

}
