package pl.looksok.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import pl.looksok.logic.CcLogic;
import android.content.Context;
import android.util.Log;

public class CalcPersistence {
	public static CcLogic readStoredCalculation(Context context, String filename) {
		
		CcLogic calc = null;
    	
    	FileInputStream fis = null;
    	ObjectInputStream in = null;
    	try {
    		fis = context.openFileInput(filename);
    		in = new ObjectInputStream(fis);
    		calc = (CcLogic) in.readObject();
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
	
	public static void saveCalculation(Context context, String filename, CcLogic calc) {
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
}
