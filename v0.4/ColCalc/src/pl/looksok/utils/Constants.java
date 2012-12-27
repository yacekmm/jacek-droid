package pl.looksok.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
	public static final String BUNDLE_CALCULATION_OBJECT = "CalculationObject";
	public static final String BUNDLE_INPUT_DATA_OBJECT = "InputDataObject";
	public static final String BUNDLE_PERSON_TO_EDIT = "personToEdit";
	
	public static final String SHARED_PREFS_NAME = "PayCalc";
	
	public static final String PERSISTENCE_SAVED_CALCS_FILE = "StoredCalculations";
	public static final int PERSISTENCE_MAX_STORED_CALCS = 10;
	
	public static final String APPLICATION_WEBSITE_URL = "http://looksok.wordpress.com/";
	
	public static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.yyyy");
	public static final DateTimeFormatter SIMPLE_DATE_FORMAT_WITH_HOUR = DateTimeFormat.forPattern("dd.MM.yyyy, HH:mm");
	
	public static final int MULTI_PERSON_DEFAULT_COUNT = 3;
	public static final int MULTI_PERSON_MIN_COUNT = 2;
	public static final int MULTI_PERSON_MAX_COUNT = 12;
}
