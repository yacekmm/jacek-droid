package pl.looksok.test.utils;

import java.util.ArrayList;
import java.util.List;

import pl.looksok.logic.InputData;

public class TestScenarioBuilder {
	public static List<InputData> buildTestCaseOnePerson(double paymentA) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList.add(new InputData(Constants.personAName, paymentA));
		return inputPaysList;
	}

	public static List<InputData> buildTestCaseTwoPeople(double paymentA, double paymentB) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList = buildTestCaseOnePerson(paymentA);
		inputPaysList.add(new InputData(Constants.personBName, paymentB));
		return inputPaysList;
	}
	
	public static List<InputData> buildTestCaseTwoPeopleVariousPays(double paymentA, double howMuchAShouldPay,
			double paymentB, double howMuchBShouldPay) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList.add(new InputData(Constants.personAName, paymentA, howMuchAShouldPay));
		inputPaysList.add(new InputData(Constants.personBName, paymentB, howMuchBShouldPay));
		return inputPaysList;
	}

	public static List<InputData> buildTestCaseThreePeople(double paymentA, double paymentB, double paymentC) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList = buildTestCaseTwoPeople(paymentA, paymentB);
		inputPaysList.add(new InputData(Constants.personCName, paymentC));
		return inputPaysList;
	}
	
	public static List<InputData> buildTestCaseThreePeople(String personAName, double paymentA, 
			String personBName, double paymentB, 
			String personCName, double paymentC) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList.add(new InputData(personAName, paymentA));
		inputPaysList.add(new InputData(personBName, paymentB));
		inputPaysList.add(new InputData(personCName, paymentC));
		return inputPaysList;
	}

	public static List<InputData> buildTestCaseFourPeople(double paymentA, double paymentB, double paymentC, double paymentD) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList = buildTestCaseThreePeople(paymentA, paymentB, paymentC);
		inputPaysList.add(new InputData(Constants.personDName, paymentD));
		return inputPaysList;
	}
	
	public static List<InputData> buildTestCaseSixPeople(double paymentA, double paymentB, double paymentC, 
			double paymentD, double paymentE, double paymentF) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList = buildTestCaseFourPeople(paymentA, paymentB, paymentC, paymentD);
		inputPaysList.add(new InputData(Constants.personEName, paymentE));
		inputPaysList.add(new InputData(Constants.personFName, paymentF));
		return inputPaysList;
	}
}
