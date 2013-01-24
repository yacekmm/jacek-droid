package pl.looksok.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.exceptions.PaysNotCalculatedException;
import pl.looksok.logic.utils.FormatterHelper;
import pl.looksok.logic.utils.PersonDataUtils;


public class PersonData implements Serializable, Comparable<PersonData>{
	private static final long serialVersionUID = 4909331903428866567L;
	
	private String name;
	private HashSet<String> emails = new HashSet<String>();
	private List<AtomPayment> atomPayments = new ArrayList<AtomPayment>();
	private double howMuchIPaid;
	private double howMuchIShouldPay =-1;
	private double toReturn;
	private double totalRefundForThisPerson;
	private HashMap<String, PersonData> otherPeoplePayments;
	private HashMap<String, Double> myDebts = new HashMap<String, Double>();
	private HashMap<String, Double> myRefunds = new HashMap<String, Double>();
	private double alreadyReturned;
	private double alreadyRefunded = 0.0;

	//gift
	private boolean receivesGift = false;
	private double howMuchIPaidForGift;
	private double howMuchIShouldPayForGift;

	private CalculationType calculationType = null;

	public PersonData(String _personName, HashMap<String, PersonData> inputPays) {
		PersonData pd = inputPays.get(_personName);
		setPersonName(pd.getName());
		setHowMuchIPaid(pd.getHowMuchPersonPaid());
		setEmails(pd.getEmails());
		setReceivesGift(pd.receivesGift);
		setHowMuchIPaidForGift(pd.getHowMuchIPaidForGift());
		setHowMuchIShouldPayForGift(pd.getHowMuchIShouldPayForGift());
		setCalculationType(pd.getCalculationType());
		setHowMuchPersonShouldPay(pd.getHowMuchPersonShouldPay());

		otherPeoplePayments = inputPays;
//		myDebts = new HashMap<String, Double>();
	}

	public PersonData(String name, List<AtomPayment> atomPays, HashSet<String> emails) {
		this.name = name;
		this.setAtomPayments(atomPays);
		this.setPayMadeByPerson(atomPays);
		this.emails = emails;
	}

	public PersonData(String name, List<AtomPayment> atomPays, HashSet<String> emails, boolean receivesGift, double giftPayment) {
		this(name, atomPays, emails);
		setReceivesGift(receivesGift);
		setHowMuchIPaidForGift(giftPayment);
	}
	
	public PersonData(String name, List<AtomPayment> atomPays_shouldPay, double howMuchPaid, HashSet<String> emails) {
		this(name, atomPays_shouldPay, howMuchPaid);
		this.emails = emails;
	}

	public PersonData(String name, List<AtomPayment> atomPays_shouldPay, double howMuchPaid) {
		this.name = name;
		this.setAtomPayments(atomPays_shouldPay);
		setHowMuchPersonShouldPay(atomPays_shouldPay);
		setHowMuchIPaid(howMuchPaid);
//		this(name, atomPays, shouldPayDouble, new HashSet<String>());
	}

	public void prepareCalculationData(double _howMuchPerPerson) {
		howMuchIShouldPay = _howMuchPerPerson;

		calculateHowMuchIShouldReturn();
		calculateHowMuchRefundIShouldReceive();
	}

	public void calculateRefundToOthers(HashMap<String, PersonData> calculationResult) {
		Set<String> c = calculationResult.keySet();
		Iterator<String> it = c.iterator();

		while(it.hasNext()) {
			PersonData pp2 = calculationResult.get(it.next());
			if(this.getName() != pp2.getName()){
				double returnValue = howMuchIGiveBackToPersonB(pp2);
				getMyDebts().put(pp2.getName(), returnValue);
			}
		}
	}

	private double howMuchIGiveBackToPersonB(PersonData personB) {
		try{
			double result = this.howMuchShouldReturnTo(personB.getName());
			return result;
		}catch(NullPointerException e){
			throw new PaysNotCalculatedException("Call 'calculate' method before reading results");
		}
	}

	private void calculateHowMuchIShouldReturn() {
		toReturn = getHowMuchPersonShouldPay() - getHowMuchPersonPaid();
		if(getCalculationType().equals(CalculationType.POTLUCK_PARTY_WITH_GIFT_V2) /*&& !receivesGift()*/)
			toReturn -= getHowMuchIPaidForGift();
		if(toReturn < 0.0)
			toReturn = 0.0;
	}

	private void calculateHowMuchRefundIShouldReceive() {
		totalRefundForThisPerson = getHowMuchPersonPaid() - getHowMuchPersonShouldPay();
		if(getCalculationType().equals(CalculationType.POTLUCK_PARTY_WITH_GIFT_V2) && !receivesGift()){
			totalRefundForThisPerson += getHowMuchIPaidForGift();
			totalRefundForThisPerson -= getHowMuchIShouldPayForGift();
		}
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
		double howMuchPersonBPaid = personBData.getPayMadeByPersonForCalculationAlgorithm();
		double howMuchPersonBShouldPay = personBData.getHowMuchPersonShouldPay();
		double howMuchRefundPersonBNeeds = howMuchPersonBPaid - howMuchPersonBShouldPay - personBData.getAlreadyRefunded();

		if(howMuchRefundPersonBNeeds>0){
			return PersonDataUtils.returnMoneyToPersonB(personBName, howMuchPersonBPaid,
					howMuchPersonBShouldPay, howMuchRefundPersonBNeeds, this);
		}else
			return 0.0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName()).append(" paid: ").append(getHowMuchPersonPaid());
		sb.append(", should pay: ").append(getHowMuchPersonShouldPay());
		sb.append(", paid for gift: ").append(getHowMuchIPaidForGift());
		sb.append(", should pay for gift: ").append(getHowMuchIShouldPayForGift());

		return sb.toString();
	}

	public void increaseRefund(String forWhichPerson, double payValueToAdd) {
		double currentValue = getMyDebts().remove(forWhichPerson);
		getMyDebts().put(forWhichPerson, currentValue + payValueToAdd);
	}

	@Override
	public int compareTo(PersonData another) {
		return this.getName().compareToIgnoreCase(another.getName());
	}

	public HashMap<String, Double> getPersonDebts() {
		HashMap<String, Double> result = new HashMap<String, Double>();

		Iterator<String> it = getMyDebts().keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Double value = getMyDebts().get(key);
			if(value > 0){
				result.put(key, FormatterHelper.roundDouble(value, 2));
			}
		}
		return result;
	}

	////////////////////////////////////
	//// GETTERS & SETTERS
	////////////////////////////////////
	public String getName() {
		return name;
	}

	public void setPersonName(String personName) {
		this.name = personName;
	}

	public double getHowMuchPersonPaid() {
		return howMuchIPaid;
	}

	public void setHowMuchIPaid(Double payMadeByPerson) {
		this.howMuchIPaid = payMadeByPerson;
	}

	private void setPayMadeByPerson(List<AtomPayment> atomPays) {
		double totalPay = 0;
		for (AtomPayment ap : atomPays) {
			if(ap.getValue() < 0)
				throw new BadInputDataException("AtomPay is less than zero: " + ap);
			totalPay += ap.getValue();
		}
		setHowMuchIPaid(totalPay);
	}

	public double getHowMuchPersonShouldPay() {
		return howMuchIShouldPay;
	}

	public void setHowMuchPersonShouldPay(double value) {
		if(value < 0)
			value = 0;
		this.howMuchIShouldPay = value;
	}
	
	private void setHowMuchPersonShouldPay(List<AtomPayment> atomPays_shouldPay) {
		double totalPay = 0;
		for (AtomPayment ap : atomPays_shouldPay) {
			if(ap.getValue() < 0)
				throw new BadInputDataException("AtomPay is less than zero: " + ap);
			totalPay += ap.getValue();
		}
		setHowMuchPersonShouldPay(totalPay);
	}

	public HashMap<String, Double> getMyDebts() {
		if(myDebts == null)
			myDebts = new HashMap<String, Double>();
		return myDebts;
	}

	public double getMyDebtForPersonB(String personB) {
		return getMyDebts().get(personB);
	}

	public double getAlreadyRefunded() {
		return alreadyRefunded;
	}

	public void setAlreadyRefunded(double alreadyRefunded) {
		this.alreadyRefunded = alreadyRefunded;
	}

	public HashMap<String, Double> getMyRefunds() {
		if(myRefunds == null)
			myRefunds = new HashMap<String, Double>();
		return myRefunds;
	}

	public void setMyRefunds(HashMap<String, Double> refundsFromOtherPeople) {
		this.myRefunds = refundsFromOtherPeople;
	}

	public double getAlreadyReturned() {
		return alreadyReturned;
	}

	public void setAlreadyReturned(double alreadyReturned) {
		this.alreadyReturned = alreadyReturned;
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

	public boolean receivesGift() {
		return receivesGift;
	}

	public void setReceivesGift(boolean receivesGift) {
		this.receivesGift = receivesGift;
	}

	public double getHowMuchIPaidForGift() {
		return howMuchIPaidForGift;
	}

	public void setHowMuchIPaidForGift(double howMuchIPaidForGift) {
		this.howMuchIPaidForGift = howMuchIPaidForGift;
	}

	public double getHowMuchIShouldPayForGift() {
		return howMuchIShouldPayForGift;
	}

	public void setHowMuchIShouldPayForGift(double howMuchIShouldPayForGift) {
		this.howMuchIShouldPayForGift = howMuchIShouldPayForGift;
	}

	public List<AtomPayment> getAtomPayments() {
		return atomPayments;
	}

	public void setAtomPayments(List<AtomPayment> atomPayments) {
		this.atomPayments = atomPayments;
	}

	public double getPayMadeByPersonForCalculationAlgorithm() {
		if(getCalculationType().equals(CalculationType.POTLUCK_PARTY_WITH_GIFT_V2))
			return getHowMuchPersonPaid() + getHowMuchIPaidForGift();
		else
			return getHowMuchPersonPaid();
	}

	public CalculationType getCalculationType() {
		if(calculationType == null){
			calculationType = CalculationType.DEFAULT;
		}
		return calculationType;
	}

	public void setCalculationType(CalculationType calculationType) {
		this.calculationType = calculationType;
	}
}
