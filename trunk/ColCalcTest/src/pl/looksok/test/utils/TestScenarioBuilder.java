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
		inputPaysList.add(new InputData(Constants.personAName, paymentA));
		inputPaysList.add(new InputData(Constants.personBName, paymentB));
		return inputPaysList;
	}

	public static List<InputData> buildTestCaseThreePeople(double paymentA, double paymentB, double paymentC) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList.add(new InputData(Constants.personAName, paymentA));
		inputPaysList.add(new InputData(Constants.personBName, paymentB));
		inputPaysList.add(new InputData(Constants.personCName, paymentC));
		return inputPaysList;
	}

	public static List<InputData> buildTestCaseFourPeople(double paymentA, double paymentB, double paymentC, double paymentD) {
		List<InputData> inputPaysList = new  ArrayList<InputData>();
		inputPaysList.add(new InputData(Constants.personAName, paymentA));
		inputPaysList.add(new InputData(Constants.personBName, paymentB));
		inputPaysList.add(new InputData(Constants.personCName, paymentC));
		inputPaysList.add(new InputData(Constants.personDName, paymentD));
		return inputPaysList;
	}
}
