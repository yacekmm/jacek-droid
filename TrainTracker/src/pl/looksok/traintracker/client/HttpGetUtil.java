package pl.looksok.traintracker.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import pl.looksok.traintracker.client.exceptions.DownloadFailedException;

public class HttpGetUtil {
	
	public static HttpResponse getData(HttpClient httpClient, String url) throws IOException, DownloadFailedException{
		HttpGet getRequest = new HttpGet(url);
		
		HttpResponse response = httpClient.execute(getRequest);
		
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new DownloadFailedException("Failed : HTTP response code : "
			   + response.getStatusLine().getStatusCode());
		}
		
		return response;
	}
}
