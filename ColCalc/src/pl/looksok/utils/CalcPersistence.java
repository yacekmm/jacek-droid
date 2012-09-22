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
		calcList.add(0, calc);
		//		while(calcList.size() > Constants.PERSISTENCE_MAX_STORED_CALCS){
		//			CalculationLogic tmp = calcList.remove(calcList.size()-1);
		//			Log.d(LOG_TAG, "storedCalcsList reached maxSize limit. Removing item titled: " + tmp.getCalcName());
		//		}

		saveCalculationList(context, filename, calcList);
	}

	public static void removeCalculationFromList(Context context,
			String filename, CalculationLogic calc) {
		List<CalculationLogic> calcList = readStoredCalculationList(context, filename);
		if(calcList!=null){
			CalculationLogic calcToRemove = new CalculationLogic();
			for (CalculationLogic storedCalc : calcList) {
				if(storedCalc.getCalcName().equals(calc.getCalcName())){
					calcToRemove = storedCalc;
					break;
				}
			}
			if(calcToRemove.getCalcName().length() !=0){
				calcList.remove(calcToRemove);
				saveCalculationList(context, filename, calcList);
			}
		}

	}
}
