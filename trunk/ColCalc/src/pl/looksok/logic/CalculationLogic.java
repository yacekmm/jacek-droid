package pl.looksok.logic;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	
	public Hashtable<String, PersonData> getCalculationResult() {
		return calculationResult;
	}

	public CalculationLogic(){
		calculationResult = new Hashtable<String, PersonData>();
	}

	private double howMuchPerPerson(double totalPay, int peopleCount) {
		if(totalPay < 0)
			throw new BadPayException();
		if(peopleCount < 0)
			throw new BadPeopleCountException();
		
		return totalPay / peopleCount;
	}

	public Hashtable<String, PersonData> calculate(List<PersonData> inputPaysList){
		this.inputPaysList = inputPaysList;
		HashMap<String, PersonData> inputPays = new HashMap<String, PersonData>();
		double sumOfAllPays = 0.0;
		double sumOfAllShouldPays = 0.0;
		
		for (PersonData in : inputPaysList) {
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
				throw new BadInputDataException("Sum of all Pays made by persons is not equal to sum of amount that they should pay");
			}
		}
		
		return calculate(inputPays);
	}
	
	private Hashtable<String, PersonData> calculate(HashMap<String, PersonData> inputPays) {
		Double totalPay = calculateTotalPayValue(inputPays);
		int peopleCount = inputPays.size();
		double howMuchPersonShouldPay = -1;
		if(equalPayments)
			howMuchPersonShouldPay = howMuchPerPerson(totalPay, peopleCount);
		
		prepareCalculationObject(inputPays, howMuchPersonShouldPay);
		
		performCalculation();
		
		return calculationResult;
	}

	private void prepareCalculationObject(HashMap<String, PersonData> inputPays,
			double howMuchPersonShouldPay) {
		Collection<String> c = inputPays.keySet();
		Iterator<String> itr = c.iterator();
		while (itr.hasNext()){
			String key = itr.next();
			if(!equalPayments)
				howMuchPersonShouldPay = inputPays.get(key).getHowMuchPersonShouldPay();
			inputPays.get(key).setHowMuchPersonShouldPay(howMuchPersonShouldPay);
			
			PersonData p = new PersonData(key, inputPays);
			p.prepareCalculationData(howMuchPersonShouldPay);
			calculationResult.put(p.getName(), p);
		}
	}

	private void performCalculation() {
		Set<String> c2 = calculationResult.keySet();
		Iterator<String> it = c2.iterator();
		
		Hashtable<String, PersonData> newCalculationResult = new Hashtable<String, PersonData>();
		
		while (it.hasNext()){
			PersonData pp = calculationResult.get(it.next());
			pp.calculateRefundToOthers(calculationResult);
			newCalculationResult.put(pp.getName(), pp);
		}
		
		calculationResult = newCalculationResult;
	}

	private Double calculateTotalPayValue(HashMap<String, PersonData> inputPays) {
		Double totalPay = 0.0;
		
		Collection<PersonData> c = inputPays.values();
		Iterator<PersonData> itr = c.iterator();

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
		StringBuilder sb = new StringBuilder("Calculation results: \n");
		
		if(calculationResult != null){
			Set<String> c = calculationResult.keySet();
			Iterator<String> it = c.iterator();
			
			while (it.hasNext()){
				PersonData pp = calculationResult.get(it.next());
				sb.append(pp.printPersonReturnsToOthers()).append("\n");
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
}
