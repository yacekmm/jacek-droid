package pl.looksok.traintracker.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import pl.looksok.traintracker.client.exceptions.DownloadFailedException;
import pl.looksok.traintracker.utils.Constants;
import android.util.Log;

public class WebServiceClient {
	private final String LOG_TAG = WebServiceClient.class.getSimpleName();
	
	/**Set the timeout in milliseconds until a connection is established.
	/* The default value is zero, that means the timeout is not used. */ 
	int timeoutConnection = 4000;
	/** Set the default socket timeout (SO_TIMEOUT) 
	/* in milliseconds which is the timeout for waiting for data.*/
	int timeoutSocket = 5500;
	private DefaultHttpClient httpClient;
	private String appUrl;
	
	public WebServiceClient(){
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httpClient = new DefaultHttpClient(httpParameters);
		appUrl = Constants.WS_URL_IC_STATION_LIST;
	}
	
	/** pobieranie listy stacji */
	public String getStationList(String urlParam) {
		String result = "";
		try {
			HttpResponse response = HttpGetUtil.getData(httpClient, appUrl + urlParam);
			result = HttpConvertor.convertResponseToString(response);
			Log.d(LOG_TAG, "Got result: " + result);
		} catch (IOException e) {
			Log.e(LOG_TAG, "IOException downloading station list: " + e.getMessage());
			return null;
		} catch (DownloadFailedException e){
			Log.e(LOG_TAG, "DownloadFailedException Downloading station list: " + e.getMessage());
			return null;
		} catch (Exception e){
			Log.e(LOG_TAG, "Exception while downloading survey list: " + e.getMessage());
			return null;
		}
		return result;
	} 
}
