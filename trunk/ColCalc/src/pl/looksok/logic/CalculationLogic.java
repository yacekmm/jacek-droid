package pl.looksok.logic;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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

	public Hashtable<String, PersonData> calculate(List<PersonData> inputPaysList) throws DuplicatePersonNameException{
		this.inputPaysList = inputPaysList;
		HashMap<String, PersonData> inputPays = convertAndValidateInput();
		
		return calculate(inputPays);
	}

	private HashMap<String, PersonData> convertAndValidateInput() {
		HashMap<String, PersonData> inputPays = new HashMap<String, PersonData>();
		double sumOfAllPays = 0.0;
		double sumOfAllShouldPays = 0.0;
		
		for (PersonData in : this.inputPaysList) {
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
		return inputPays;
	}
	

	public Hashtable<String, PersonData> recalculate() {
		resetInputData();
		HashMap<String, PersonData> inputPays = convertAndValidateInput();
		return calculate(inputPays);
	}

	private void resetInputData() {
		setCalculationResult(new Hashtable<String, PersonData>());
		for (PersonData data : getInputPaysList()) {
			data.setAlreadyRefunded(0.0);
		}
	}
	
	private Hashtable<String, PersonData> calculate(HashMap<String, PersonData> inputPays) {
		Double totalPay = calculateTotalPayValue(inputPays);
		int peopleCount = inputPays.size();
		double howMuchPersonShouldPay = -1;
		
		Collection<String> c = inputPays.keySet();
		Iterator<String> itr = c.iterator();
		while (itr.hasNext()){
			String key = itr.next();
			if(!equalPayments)
				howMuchPersonShouldPay = inputPays.get(key).getHowMuchPersonShouldPay();
			else{
				howMuchPersonShouldPay = howMuchPerPerson(totalPay, peopleCount);
				inputPays.get(key).setHowMuchPersonShouldPay(howMuchPersonShouldPay);
			}
			
			PersonData p = new PersonData(key, inputPays);
			p.prepareCalculationData(howMuchPersonShouldPay);
			calculationResult.put(p.getName(), p);
		}
		
		performCalculation();
		
		return calculationResult;
	}

	private void performCalculation() {
		Hashtable<String, PersonData> newCalculationResult = new Hashtable<String, PersonData>();
		
		Iterator<String> it = calculationResult.keySet().iterator();
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
		StringBuilder sb = new StringBuilder();
		sb.append(printCalcResultForResultsList("Calculation results", "should return", "for"));
		return sb.toString();
	}
	
	/**
	 * returns String with calculation result like:
	 * [titleText]:
	 * [personName] [returnToText]:
	 * [calculatedReturnValue] [forText] [otherPersonName] 
	 * @param titleText
	 * @param returnToText
	 * @return
	 */
	public String printCalcResultForResultsList(String titleText, String returnToText, String forText){
		StringBuilder sb = new StringBuilder(titleText).append(":\n");
		
		if(calculationResult != null){
			Iterator<String> it = calculationResult.keySet().iterator();
			
			while (it.hasNext()){
				PersonData pp = calculationResult.get(it.next());
				sb.append(pp.printPersonReturnsToOthers(returnToText, forText)).append("\n");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * returns String with calculation result like:
	 * [titleText]:
	 * [personName] [howMuchPaidText]: [paidValue] ([howMuchShouldPayText]: [shouldPayValue]) [returnToText]:
	 * [calculatedReturnValue] [forText] [otherPersonName] 
	 * 
	 * example: 
	 * Calculation Result:
	 * person A paid: $20 (but should pay: $30) and has to return money to:
	 * $10 to personB
	 * @param titleText
	 * @param returnToText
	 * @param endOfLine 
	 * @return
	 */
	public String printCalcResultForEmail(String titleText, String howMuchPaidText, 
			String howMuchShouldPayText, String returnToText, String forText, String endOfLine){
		StringBuilder sb = new StringBuilder(titleText).append(":").append(endOfLine);
		
		if(calculationResult != null){
			Iterator<String> it = calculationResult.keySet().iterator();
			
			while (it.hasNext()){
				PersonData pp = calculationResult.get(it.next());
				sb.append(pp.printPersonReturnsToOthersDetails(howMuchPaidText, howMuchShouldPayText, returnToText, forText, endOfLine)).append(endOfLine);
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

	public PersonData findPersonInList(PersonData pd) {
		for (PersonData data : getInputPaysList()) {
			if(data.getName().equals(pd.getName())){
				pd = data;
				break;
			}
		}
		return pd;
	}

	public void removePerson(PersonData pd) {
		inputPaysList.remove(pd);
		getCalculationResult().remove(pd.getName());
	}

}
