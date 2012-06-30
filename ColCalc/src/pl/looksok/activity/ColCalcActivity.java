package pl.looksok.activity;

import pl.looksok.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ColCalcActivity extends Activity {
	
	protected static final int DEFAULT_TRANSITION_ANIMATION_ENTER = R.anim.fade;
	protected static final int DEFAULT_TRANSITION_ANIMATION_EXIT = R.anim.hold;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
	}
	
}
