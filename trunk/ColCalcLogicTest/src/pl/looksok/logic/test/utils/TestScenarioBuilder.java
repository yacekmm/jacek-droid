package pl.looksok.logic.test.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.looksok.logic.AtomPayment;
import pl.looksok.logic.PersonData;

public class TestScenarioBuilder {
	public static List<PersonData> buildTestCaseOnePerson(double paymentA) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, getAtomPaymentsList(paymentA), new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseTwoPeople(double paymentA, double paymentB) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseOnePerson(paymentA);
		inputPaysList.add(new PersonData(Constants.personBName, getAtomPaymentsList(paymentB), new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseThreePeople(double paymentA, double paymentB, double paymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseTwoPeople(paymentA, paymentB);
		inputPaysList.add(new PersonData(Constants.personCName, getAtomPaymentsList(paymentC), new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseThreePeopleWithNames(String personAName, double paymentA, 
			String personBName, double paymentB, 
			String personCName, double paymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(personAName, getAtomPaymentsList(paymentA), new HashSet<String>()));
		inputPaysList.add(new PersonData(personBName, getAtomPaymentsList(paymentB), new HashSet<String>()));
		inputPaysList.add(new PersonData(personCName, getAtomPaymentsList(paymentC), new HashSet<String>()));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseFourPeople(double paymentA, double paymentB, double paymentC, double paymentD) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseThreePeople(paymentA, paymentB, paymentC);
		inputPaysList.add(new PersonData(Constants.personDName, getAtomPaymentsList(paymentD), new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseSixPeople(double paymentA, double paymentB, double paymentC, 
			double paymentD, double paymentE, double paymentF) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseFourPeople(paymentA, paymentB, paymentC, paymentD);
		inputPaysList.add(new PersonData(Constants.personEName, getAtomPaymentsList(paymentE), new HashSet<String>()));
		inputPaysList.add(new PersonData(Constants.personFName, getAtomPaymentsList(paymentF), new HashSet<String>()));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseTwoPeopleVariousPays(double paymentA, double howMuchAShouldPay,
			double paymentB, double howMuchBShouldPay) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, getAtomPaymentsList(paymentA), howMuchAShouldPay, new HashSet<String>()));
		inputPaysList.add(new PersonData(Constants.personBName, getAtomPaymentsList(paymentB), howMuchBShouldPay, new HashSet<String>()));
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
	
	static List<AtomPayment> getAtomPaymentsList(double paymentA) {
		List<AtomPayment> atomPayments = new ArrayList<AtomPayment>();
		atomPayments.add(new AtomPayment("", paymentA));
		return atomPayments;
	}
}
