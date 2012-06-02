package pl.looksok.traintracker.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

public class HttpConvertor {
	
	public static String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
		BufferedReader br = new BufferedReader(
			        new InputStreamReader((response.getEntity().getContent())));
		
		String output;
		StringBuffer sb = new StringBuffer();
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
		return sb.toString();
	}
}
