package pl.looksok.logic.test.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.looksok.logic.PersonData;

public class GiftTestScenarioBuilder extends TestScenarioBuilder{
	public static List<PersonData> buildTestCaseOnePersonGift(double paymentA, 
			HashSet<String> giftReceivers, double giftPaymentA) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, paymentA, new HashSet<String>(), giftReceivers.contains(Constants.personAName), giftPaymentA));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseTwoPeopleGift(double paymentA, double paymentB, 
			HashSet<String> giftReceivers, double giftPaymentA, double giftPaymentB) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseOnePersonGift(paymentA, giftReceivers, giftPaymentA);
		inputPaysList.add(new PersonData(Constants.personBName, paymentB, new HashSet<String>(), giftReceivers.contains(Constants.personBName), giftPaymentB));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseThreePeopleGift(double paymentA, double paymentB, double paymentC, 
			HashSet<String> giftReceivers, double giftPaymentA, double giftPaymentB, double giftPaymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseTwoPeopleGift(paymentA, paymentB, giftReceivers, giftPaymentA, giftPaymentB);
		inputPaysList.add(new PersonData(Constants.personCName, paymentC, new HashSet<String>(), giftReceivers.contains(Constants.personCName), giftPaymentC));
		return inputPaysList;
	}
	
/*	public static List<PersonData> buildTestCaseTwoPeopleVariousPays(double paymentA, double howMuchAShouldPay,
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
	*/
}
