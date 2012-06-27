package pl.looksok.test.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.looksok.logic.PersonData;

public class TestScenarioBuilder {
	public static List<PersonData> buildTestCaseOnePerson(double paymentA) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, paymentA, new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseTwoPeople(double paymentA, double paymentB) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseOnePerson(paymentA);
		inputPaysList.add(new PersonData(Constants.personBName, paymentB, new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseTwoPeopleVariousPays(double paymentA, double howMuchAShouldPay,
			double paymentB, double howMuchBShouldPay) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, paymentA, howMuchAShouldPay, new HashSet<String>()));
		inputPaysList.add(new PersonData(Constants.personBName, paymentB, howMuchBShouldPay, new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseThreePeople(double paymentA, double paymentB, double paymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseTwoPeople(paymentA, paymentB);
		inputPaysList.add(new PersonData(Constants.personCName, paymentC, new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseThreePeople(String personAName, double paymentA, 
			String personBName, double paymentB, 
			String personCName, double paymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(personAName, paymentA, new HashSet<String>()));
		inputPaysList.add(new PersonData(personBName, paymentB, new HashSet<String>()));
		inputPaysList.add(new PersonData(personCName, paymentC, new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseFourPeople(double paymentA, double paymentB, double paymentC, double paymentD) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseThreePeople(paymentA, paymentB, paymentC);
		inputPaysList.add(new PersonData(Constants.personDName, paymentD, new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseSixPeople(double paymentA, double paymentB, double paymentC, 
			double paymentD, double paymentE, double paymentF) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseFourPeople(paymentA, paymentB, paymentC, paymentD);
		inputPaysList.add(new PersonData(Constants.personEName, paymentE, new HashSet<String>()));
		inputPaysList.add(new PersonData(Constants.personFName, paymentF, new HashSet<String>()));
		return inputPaysList;
	}
}
