package pl.looksok.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.looksok.logic.CalculationLogic;
import android.content.Context;
import android.util.Log;

public class CalcPersistence {
	private static final String LOG_TAG = CalcPersistence.class.getSimpleName();

	public static CalculationLogic readStoredCalculation(Context context, String filename) {
		
		CalculationLogic calc = null;
    	
    	FileInputStream fis = null;
    	ObjectInputStream in = null;
    	try {
    		fis = context.openFileInput(filename);
    		in = new ObjectInputStream(fis);
    		calc = (CalculationLogic) in.readObject();
    		in.close();
    		fis.close();
    	}catch (ClassNotFoundException e) {
    		e.printStackTrace();
    		
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (StreamCorruptedException e) {
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	Log.v(LOG_TAG, "open calcs: success");
		return calc;
	}
	
	public static void saveCalculation(Context context, String filename, CalculationLogic calc) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
		fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
		out = new ObjectOutputStream(fos);
		out.writeObject(calc);
		out.close();
		fos.close();
		
		Log.v(LOG_TAG, "Successful Save");
		} catch (IOException ex) {
		ex.printStackTrace();
		}
	}

	public static void saveCalculationList(Context context, String filename, List<CalculationLogic> calcList) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			out = new ObjectOutputStream(fos);
			out.writeObject(calcList);
			out.close();
			fos.close();
			
			Log.v(LOG_TAG, "Successful Save List");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<CalculationLogic> readStoredCalculationList(Context context, String filename) {
		List<CalculationLogic> calcList = null;
    	
    	FileInputStream fis = null;
    	ObjectInputStream in = null;
    	try {
    		fis = context.openFileInput(filename);
    		in = new ObjectInputStream(fis);
			calcList = (List<CalculationLogic>) in.readObject();
    		in.close();
    		fis.close();
    		Log.v(LOG_TAG, "open calcs list: success");
    	}catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (StreamCorruptedException e) {
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch(ClassCastException e){
    		Log.e(LOG_TAG, "Error casting stored Calcs: " + e.getMessage());
    	}
    	
		return calcList;
	}

	public static void addCalculationToList(Context context, String filename, CalculationLogic calc) {
		List<CalculationLogic> calcList = readStoredCalculationList(context, filename);
		if(calcList==null){
			Log.d(LOG_TAG, "Stored Calculation List was empty. creating new");
			calcList = new ArrayList<CalculationLogic>();
		}
		calc.setDateSaved(Calendar.getInstance());
		calcList.add(calc);
		
		saveCalculationList(context, filename, calcList);
	}
}
