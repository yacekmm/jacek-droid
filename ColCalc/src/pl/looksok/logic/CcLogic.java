package pl.looksok.logic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.looksok.exception.BadPayException;
import pl.looksok.exception.BadPeopleCountException;
import pl.looksok.exception.DuplicatePersonNameException;
import pl.looksok.exception.PaysNotCalculatedException;

public class CcLogic implements Serializable {
	private static final long serialVersionUID = -1238265432953764569L;
	private Hashtable<String, PeoplePays> calculationResult;
	private List<InputData> inputPaysList = null;
	
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
		HashMap<String, Double> inputPays = new HashMap<String, Double>();
		for (InputData in : inputPaysList) {
			if(inputPays.containsKey(in.getName()))
				throw new DuplicatePersonNameException();
			else
				inputPays.put(in.getName(), in.getPay());
		}
		return calculate(inputPays);
	}
	
	private Hashtable<String, PeoplePays> calculate(HashMap<String, Double> inputPays) {
		Double totalPay = calculateTotalPayValue(inputPays);
		int peopleCount = inputPays.size();
		double howMuchPerPerson = howMuchPerPerson(totalPay, peopleCount);
		
		Collection<String> c = inputPays.keySet();
		Iterator<String> itr = c.iterator();
		while (itr.hasNext()){
			String key = itr.next();
			PeoplePays p = new PeoplePays(key, inputPays);
			p.calculateToReturn(howMuchPerPerson);
			calculationResult.put(p.getPersonName(), p);
		}
		
		return calculationResult;
	}

	private Double calculateTotalPayValue(HashMap<String, Double> inputPays) {
		Double totalPay = 0.0;
		
		Collection<Double> c = inputPays.values();
		Iterator<Double> itr = c.iterator();

		while (itr.hasNext()){
			totalPay += itr.next();
		}
		return totalPay;
	}

	public double howMuchPersonAGivesBackToPersonB(String personA, String personB) {
		try{
			double result = calculationResult.get(personA).howMuchShouldReturnTo(personB);
			int decimalPlace = 2;
		    BigDecimal bd = new BigDecimal(result);
		    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		    result = bd.doubleValue();
			return result;
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
				while(it2.hasNext()) {
					PeoplePays pp2 = calculationResult.get(it2.next());
					if(pp.getPersonName() != pp2.getPersonName()){
						sb.append("\n\treturns: ");
						sb.append(howMuchPersonAGivesBackToPersonB(pp.getPersonName(), pp2.getPersonName()));
						sb.append(" to: ");
						sb.append(pp2.getPersonName());
					}
				}
				it2 = c2.iterator();
				sb.append("\n\n");
			}
		}
		return sb.toString();
	}

	public List<InputData> getInputPaysList() {
		return inputPaysList;
	}
}
