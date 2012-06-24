package pl.looksok.activity.addperson;

import java.util.HashSet;

import android.database.Cursor;
import android.provider.ContactsContract;

public class AddPersonUtils {

	public HashSet<String> getPersonEmailsSet(String id, AddNewPerson addNewPerson){
        HashSet<String> emails = new HashSet<String>(); 
		Cursor emailCur = addNewPerson.managedQuery( 
  		ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
			null,
			ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
			new String[]{id}, null); 
	      	
		while (emailCur.moveToNext()) { 
      		String email = emailCur.getString(
                      emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
      		emails.add(email);
      	}
		
		return emails;
      	
	}
}
