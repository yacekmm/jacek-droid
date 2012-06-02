package pl.looksok.traintracker.client.cp;

import pl.looksok.traintracker.client.WebServiceClient;
import pl.looksok.traintracker.utils.Constants;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class TrainTrackerCP extends ContentProvider {

	public static final String AUTHORITY = "pl.looksok.traintracker.client.cp.TrainTrackerCP";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/");
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sUriMatcher.addURI(AUTHORITY, Constants.WS_CP_STATION_MATCH_LIST, CpUriMatcherEnum.STATION_LIST.ordinal());
    }
    
	WebServiceClient client;
    
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		return null;
	}

	@Override
	public boolean onCreate() {
		client = new WebServiceClient();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] arg1, String selection, String[] arg3,
			String arg4) {
		switch (CpUriMatcherEnum.values()[sUriMatcher.match(uri)]) {
			case STATION_LIST:
				String result = client.getStationList("?stacja=" + selection);
				return null;
				
			default:
				return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
