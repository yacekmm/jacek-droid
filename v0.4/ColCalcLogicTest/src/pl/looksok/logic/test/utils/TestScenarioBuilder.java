package pl.looksok.logic.test.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.utils.PersonDataUtils;

public class TestScenarioBuilder {
	public static List<PersonData> buildTestCaseOnePerson(double paymentA) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, PersonDataUtils.getDefaultAtomPaymentsList(paymentA), new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseTwoPeople(double paymentA, double paymentB) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseOnePerson(paymentA);
		inputPaysList.add(new PersonData(Constants.personBName, PersonDataUtils.getDefaultAtomPaymentsList(paymentB), new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseThreePeople(double paymentA, double paymentB, double paymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseTwoPeople(paymentA, paymentB);
		inputPaysList.add(new PersonData(Constants.personCName, PersonDataUtils.getDefaultAtomPaymentsList(paymentC), new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseThreePeopleWithNames(String personAName, double paymentA, 
			String personBName, double paymentB, 
			String personCName, double paymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(personAName, PersonDataUtils.getDefaultAtomPaymentsList(paymentA), new HashSet<String>()));
		inputPaysList.add(new PersonData(personBName, PersonDataUtils.getDefaultAtomPaymentsList(paymentB), new HashSet<String>()));
		inputPaysList.add(new PersonData(personCName, PersonDataUtils.getDefaultAtomPaymentsList(paymentC), new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseFourPeople(double paymentA, double paymentB, double paymentC, double paymentD) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseThreePeople(paymentA, paymentB, paymentC);
		inputPaysList.add(new PersonData(Constants.personDName, PersonDataUtils.getDefaultAtomPaymentsList(paymentD), new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseSixPeople(double paymentA, double paymentB, double paymentC, 
			double paymentD, double paymentE, double paymentF) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseFourPeople(paymentA, paymentB, paymentC, paymentD);
		inputPaysList.add(new PersonData(Constants.personEName, PersonDataUtils.getDefaultAtomPaymentsList(paymentE), new HashSet<String>()));
		inputPaysList.add(new PersonData(Constants.personFName, PersonDataUtils.getDefaultAtomPaymentsList(paymentF), new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseTwoPeopleVariousPays(double paymentA, double howMuchAShouldPay,
			double paymentB, double howMuchBShouldPay) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, PersonDataUtils.getDefaultAtomPaymentsList(paymentA), howMuchAShouldPay, new HashSet<String>()));
		inputPaysList.add(new PersonData(Constants.personBName, PersonDataUtils.getDefaultAtomPaymentsList(paymentB), howMuchBShouldPay, new HashSet<String>()));
		return inputPaysList;
	}

	
	public static List<PersonData> buildTestCaseOnePersonAtomPays(AtomPayment... inputAtomPays) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		List<AtomPayment> atomPays = new ArrayList<AtomPayment>();
		for (AtomPayment ap : inputAtomPays) {
			atomPays.add(ap);
		}
		
		inputPaysList.add(new PersonData(Constants.personAName, atomPays, new HashSet<String>()));
		return inputPaysList;
	}
}
