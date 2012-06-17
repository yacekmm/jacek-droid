package pl.looksok.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import pl.looksok.logic.CalculationLogic;
import android.content.Context;
import android.util.Log;

public class CalcPersistence {
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
    	
    	Log.v("opened Calc", "success");
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
		
		Log.v("saveList", "Successful Save");
		} catch (IOException ex) {
		ex.printStackTrace();
		}
	}
	
//	public static void saveInputData(Context context, String filename, InputData inputData) {
//		FileOutputStream fos = null;
//		ObjectOutputStream out = null;
//		try {
//		fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
//		out = new ObjectOutputStream(fos);
//		out.writeObject(inputData);
//		out.close();
//		fos.close();
//		
//		Log.v("saveList", "Successful Save");
//		} catch (IOException ex) {
//		ex.printStackTrace();
//		}
//	}
//	
//	public static InputData readStoredInputData(Context context, String filename) {
//		
//		InputData inputData = null;
//    	
//    	FileInputStream fis = null;
//    	ObjectInputStream in = null;
//    	try {
//    		fis = context.openFileInput(filename);
//    		in = new ObjectInputStream(fis);
//    		inputData = (InputData) in.readObject();
//    		in.close();
//    		fis.close();
//    	}catch (ClassNotFoundException e) {
//    		e.printStackTrace();
//    		
//    	} catch (FileNotFoundException e) {
//    		e.printStackTrace();
//    	} catch (StreamCorruptedException e) {
//    		
//    	} catch (IOException e) {
//    		e.printStackTrace();
//    	}
//    	
//    	Log.v("opened Calc", "success");
//		return inputData;
//	}
}
