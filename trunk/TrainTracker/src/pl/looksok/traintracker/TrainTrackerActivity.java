package pl.looksok.traintracker;

import pl.looksok.traintracker.client.cp.TrainTrackerCP;
import pl.looksok.traintracker.utils.Constants;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.looksok.traintracker.R;

public class TrainTrackerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Cursor stationCursor = managedQuery(Uri.parse(TrainTrackerCP.CONTENT_URI + Constants.WS_CP_STATION_MATCH_LIST), 
        		null, "Mrozy", null, null);
    }
}