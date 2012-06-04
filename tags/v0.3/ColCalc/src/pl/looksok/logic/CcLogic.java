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

public class CcLogic implements Serializable {
	private static final long serialVersionUID = -1238265432953764569L;
	private Hashtable<String, PeoplePays> calculationResult;
	private List<InputData> inputPaysList = null;
	private boolean equalPayments = true;
	
	public Hashtable<String, PeoplePays> getCalculationResult() {
		return calculationResult;
	}

	public CcLogic(){
		calculationResult = new Hashtable<String, PeoplePays>();
	}

	private double howMuchPerPerson(double totalPay, int peopleCount) {
		if(totalPay < 0)
			throw new BadPayException();
		if(peopleCount < 0)
			throw new BadPeopleCountException();
		
		return totalPay / peopleCount;
	}

	public Hashtable<String, PeoplePays> calculate(List<InputData> inputPaysList){
		this.inputPaysList = inputPaysList;
		HashMap<String, InputData> inputPays = new HashMap<String, InputData>();
		double sumOfAllPays = 0.0;
		double sumOfAllShouldPays = 0.0;
		
		for (InputData in : inputPaysList) {
			if(inputPays.containsKey(in.getName()))
				throw new DuplicatePersonNameException();
			else{
				inputPays.put(in.getName(), in);
				sumOfAllPays += in.getPay();
				sumOfAllShouldPays += in.getShouldPay();
			}
		}
		
		if(!equalPayments){
			if(sumOfAllPays != sumOfAllShouldPays){
				throw new BadInputDataException("Sum of all Pays made by persons is not equal to sum of amount that they should pay");
			}
		}
		
		return calculate(inputPays);
	}
	
	private Hashtable<String, PeoplePays> calculate(HashMap<String, InputData> inputPays) {
		Double totalPay = calculateTotalPayValue(inputPays);
		int peopleCount = inputPays.size();
		double howMuchPersonShouldPay = -1;
		if(equalPayments)
			howMuchPersonShouldPay = howMuchPerPerson(totalPay, peopleCount);
		
		Collection<String> c = inputPays.keySet();
		Iterator<String> itr = c.iterator();
		while (itr.hasNext()){
			String key = itr.next();
			if(!equalPayments)
				howMuchPersonShouldPay = inputPays.get(key).getShouldPay();
			inputPays.get(key).setShouldPay(howMuchPersonShouldPay);
			
			PeoplePays p = new PeoplePays(key, inputPays);
			p.calculateToReturnAndRefund(howMuchPersonShouldPay);
			calculationResult.put(p.getPersonName(), p);
		}
		
		return calculationResult;
	}

	private Double calculateTotalPayValue(HashMap<String, InputData> inputPays) {
		Double totalPay = 0.0;
		
		Collection<InputData> c = inputPays.values();
		Iterator<InputData> itr = c.iterator();

		while (itr.hasNext()){
			totalPay += itr.next().getPay();
		}
		return totalPay;
	}

	public double howMuchPersonAGivesBackToPersonB(String personA, String personB) {
		try{
			double result = calculationResult.get(personA).howMuchShouldReturnTo(personB);
			return FormatterHelper.roundDouble(result, 2);
		}catch(NullPointerException e){
			throw new PaysNotCalculatedException("Call 'calculate' method before reading results");
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		
		if(calculationResult != null){
			Hashtable<String, PeoplePays> tmpTable = new Hashtable<String, PeoplePays>();
			tmpTable = calculationResult;
			
			Set<String> c = calculationResult.keySet();
			Set<String> c2 = tmpTable.keySet();
			Iterator<String> it = c.iterator();
			Iterator<String> it2 = c2.iterator();
			
			while (it.hasNext()){
				PeoplePays pp = calculationResult.get(it.next());
				
				sb.append(pp.getPersonName());
				sb.append(" - paid: ");
				sb.append(pp.getPayMadeByPerson());
				sb.append("\n\tshould pay: ");
				sb.append(pp.getHowMuchPersonShouldPay());
				while(it2.hasNext()) {
					PeoplePays pp2 = calculationResult.get(it2.next());
					if(pp.getPersonName() != pp2.getPersonName()){
						double returnValue = howMuchPersonAGivesBackToPersonB(pp.getPersonName(), pp2.getPersonName());
						if(returnValue!=0.0){
							sb.append("\n\treturns: ");
							sb.append(returnValue);
							sb.append(" to: ");
							sb.append(pp2.getPersonName());
						}
					}
				}
				it2 = c2.iterator();
				sb.append("\n\n");
			}
			it = c.iterator();
			it2 = c2.iterator();
		}
		return sb.toString();
	}

	public List<InputData> getInputPaysList() {
		return inputPaysList;
	}

	public void setCalculationResult(Hashtable<String, PeoplePays> calculationResult) {
		this.calculationResult = calculationResult;
	}

	public boolean isEqualPayments() {
		return equalPayments;
	}

	public void setEqualPayments(boolean equalPayments) {
		this.equalPayments = equalPayments;
	}
}
