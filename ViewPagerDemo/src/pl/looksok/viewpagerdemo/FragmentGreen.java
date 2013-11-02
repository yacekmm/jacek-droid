package pl.looksok.viewpagerdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v4.app.Fragment;

public class FragmentGreen extends Fragment implements FragmentLifecycle{

	private static final String TAG = FragmentGreen.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_green, container, false);
		return view;
	}
	
	@Override
	public void onPauseFragment() {
		Log.i(TAG, "onPauseFragment()");
		Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show(); 
	}
	
	@Override
	public void onResumeFragment() {
		Log.i(TAG, "onResumeFragment()");
		Toast.makeText(getActivity(), "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show(); 
	}

}
