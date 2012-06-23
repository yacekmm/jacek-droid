package pl.looksok.activity.calcresult;

import java.util.Hashtable;
import java.util.Iterator;

import android.content.Context;

import pl.looksok.R;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.PersonData;

public class CalcResultUtils {

	public String[] getEmailsArray(CalculationLogic calc) {
		Hashtable<String, PersonData> calcResult = calc.getCalculationResult();
		String [] emails = new String[calcResult.size()];
		
		Iterator<String> it = calcResult.keySet().iterator();
		int i = 0;
		while (it.hasNext()){
			String key = it.next();
			String email = calcResult.get(key).getEmail();
			emails[i++] = email; 
		}
		
		return emails;
	}

	public String buildEmailMessage(Context context) {
		StringBuilder sb = new StringBuilder();
		sb.append(context.getResources().getString(R.string.email_content_welcome)).append("\n\n");
		sb.append(context.getResources().getString(R.string.email_content_text)).append("\n\n");
		sb.append(context.getResources().getString(R.string.email_content_madeBy)).append(" ");
		sb.append(context.getResources().getString(R.string.app_name)).append("!\n\n");
		sb.append(context.getResources().getString(R.string.email_content_greetings));
		
		return sb.toString();
	}

}
