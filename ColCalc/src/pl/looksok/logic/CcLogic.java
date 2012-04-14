package pl.looksok.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pl.looksok.exception.BadPayException;
import pl.looksok.exception.BadPeopleCountException;
import pl.looksok.exception.DuplicatePersonNameException;
import pl.looksok.exception.PaysNotCalculatedException;

public class CcLogic {
	private Hashtable<String, PeoplePays> calculationResult;
	
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
			return calculationResult.get(personA).howMuchShouldReturnTo(personB);
		}catch(NullPointerException e){
			throw new PaysNotCalculatedException("Call 'calculate' method before reading results");
		}
	}
}
