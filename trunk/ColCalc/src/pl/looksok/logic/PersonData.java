package pl.looksok.logic;

import java.io.Serializable;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import android.util.Log;

import pl.looksok.utils.FormatterHelper;
import pl.looksok.utils.exceptions.PaysNotCalculatedException;


public class PersonData implements Serializable{

	private static final long serialVersionUID = 4909331903428866567L;
	private static final String LOG_TAG = PersonData.class.getSimpleName();
	private String name;
	private HashSet<String> emails = new HashSet<String>();
	private double howMuchIPaid;
	private double howMuchIShouldPay;
	private double toReturn;
	private double totalRefundForThisPerson;
	private HashMap<String, PersonData> otherPeoplePayments;
	private HashMap<String, Double> refundForOtherPeople;
	private double alreadyReturned;
	private double alreadyRefunded = 0.0;
	
	public PersonData(String _personName, HashMap<String, PersonData> inputPays) {
		PersonData pd = inputPays.get(_personName);
		setPersonName(pd.getName());
		setPayMadeByPerson(pd.getPayMadeByPerson());
		setEmails(pd.getEmails());
		
		otherPeoplePayments = inputPays;
		refundForOtherPeople = new HashMap<String, Double>();
	}

	public PersonData(String name, double payDouble, HashSet<String> emails) {
		this.name = name;
		this.setPayMadeByPerson(payDouble);
		this.emails = emails;
	}

	public PersonData(String name, double payDouble, double shouldPayDouble, HashSet<String> emails) {
		this(name, payDouble, emails);
		setHowMuchPersonShouldPay(shouldPayDouble);
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
	
	
	private boolean personBNeedsMoreThanIShouldGive(double tmpToReturn) {
		return alreadyReturned + tmpToReturn > toReturn;
	}
	
	public double getHowMuchPersonShouldPay() {
		return howMuchIShouldPay;
	}
	
	public void setHowMuchPersonShouldPay(double value) {
		this.howMuchIShouldPay = value;
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
	
	public double getAlreadyRefunded() {
		return alreadyRefunded;
	}
	
	public void setAlreadyRefunded(double alreadyRefunded) {
		this.alreadyRefunded = alreadyRefunded;
	}

	public void prepareCalculationData(double _howMuchPerPerson) {
		howMuchIShouldPay = FormatterHelper.roundDouble(_howMuchPerPerson, 2);
		
		calculateHowMuchIShouldReturn();
		calculateHowMuchRefundIShouldReceive();
	}

	public void calculateRefundToOthers(Hashtable<String, PersonData> calculationResult) {
		Set<String> c = calculationResult.keySet();
		Iterator<String> it = c.iterator();
		
		while(it.hasNext()) {
			PersonData pp2 = calculationResult.get(it.next());
			if(this.getName() != pp2.getName()){
				double returnValue = howMuchIGiveBackToPersonB(pp2);
				getRefundForOtherPeople().put(pp2.getName(), returnValue);
				Log.d(LOG_TAG, "----------Adding new person to calculated refunds: " + pp2.getName() + ", value: " + returnValue);
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

	private void calculateHowMuchRefundIShouldReceive() {
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
		PersonData personBData = otherPeoplePayments.get(personBName);
		double howMuchPersonBPaid = personBData.getPayMadeByPerson();
		double howMuchPersonBShouldPay = personBData.getHowMuchPersonShouldPay();
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

	private void addToAlreadyRefunded(double valueToAdd) {
		setAlreadyRefunded(getAlreadyRefunded() + valueToAdd);
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

	public String printPersonReturnsToOthers(String returnToText, String forText){
		StringBuilder sb = new StringBuilder(getName());
		sb.append(" ").append(returnToText).append(":\n");
		boolean isThereAnyPersonOnReturnList = false;
		
		Iterator<String> it = getRefundForOtherPeople().keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double result = getRefundForOtherPeople().get(key);
			if(result >0){
				isThereAnyPersonOnReturnList = true;
				sb.append(result).append(" ").append(Currency.getInstance(Locale.getDefault()).getSymbol()).append(" ");
				sb.append(forText).append(": ").append(key).append("\n");
			}
		}
		
		if(isThereAnyPersonOnReturnList)
			return sb.toString();
		else
			return "";
	}

	public String printPersonReturnsToOthersDetails(String howMuchPaidText, String howMuchShouldPayText, String returnToText, String forText, String endOfLine){
		String currency = Currency.getInstance(Locale.getDefault()).getSymbol();

		StringBuilder sb = new StringBuilder(getName()).append(" - ").append(howMuchPaidText).append(": ").append(getPayMadeByPerson()).append(currency).append(" ");
		sb.append("(").append(howMuchShouldPayText).append(": ").append(getHowMuchPersonShouldPay()).append(currency).append(")");
		
		String messageIfNoReturnNeeded = sb.toString() + endOfLine;
		
		sb.append(", ").append(returnToText).append(":").append(endOfLine);
		boolean isThereAnyPersonOnReturnList = false;
		
		Iterator<String> it = getRefundForOtherPeople().keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			double result = getRefundForOtherPeople().get(key);
			if(result >0){
				isThereAnyPersonOnReturnList = true;
				sb.append(result).append(" ").append(currency).append(" ");
				sb.append(forText).append(": ").append(key).append(endOfLine);
			}
		}
		
		if(isThereAnyPersonOnReturnList)
			return sb.toString();
		else
			return messageIfNoReturnNeeded;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName()).append(" paid: ").append(getPayMadeByPerson());
		
		return sb.toString();
	}

	public HashSet<String> getEmails() {
		return emails;
	}

	public void setEmails(HashSet<String> emails) {
		this.emails = emails;
	}

	public HashMap<String, PersonData> getOtherPeoplePayments() {
		return otherPeoplePayments;
	}

	public void setOtherPeoplePayments(
			HashMap<String, PersonData> otherPeoplePayments) {
		this.otherPeoplePayments = otherPeoplePayments;
	}
}
