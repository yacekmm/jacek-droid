package pl.looksok.logic.test.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pl.looksok.logic.PersonData;

public class GiftTestScenarioBuilder extends TestScenarioBuilder{
	
	public static List<PersonData> buildTestCaseOnePersonGift(double paymentA, 
			HashSet<String> giftReceivers, double giftPaymentA) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList.add(new PersonData(Constants.personAName, getAtomPaymentsList(paymentA), new HashSet<String>(), giftReceivers.contains(Constants.personAName), giftPaymentA));
		return inputPaysList;
	}

	public static List<PersonData> buildTestCaseTwoPeopleGift(double paymentA, double paymentB, 
			HashSet<String> giftReceivers, double giftPaymentA, double giftPaymentB) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseOnePersonGift(paymentA, giftReceivers, giftPaymentA);
		inputPaysList.add(new PersonData(Constants.personBName, getAtomPaymentsList(paymentB), new HashSet<String>(), giftReceivers.contains(Constants.personBName), giftPaymentB));
		return inputPaysList;
	}
	
	public static List<PersonData> buildTestCaseThreePeopleGift(double paymentA, double paymentB, double paymentC, 
			HashSet<String> giftReceivers, double giftPaymentA, double giftPaymentB, double giftPaymentC) {
		List<PersonData> inputPaysList = new  ArrayList<PersonData>();
		inputPaysList = buildTestCaseTwoPeopleGift(paymentA, paymentB, giftReceivers, giftPaymentA, giftPaymentB);
		inputPaysList.add(new PersonData(Constants.personCName, getAtomPaymentsList(paymentC), new HashSet<String>(), giftReceivers.contains(Constants.personCName), giftPaymentC));
		return inputPaysList;
	}
}
