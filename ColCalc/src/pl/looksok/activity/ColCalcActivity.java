package pl.looksok.activity;

import pl.looksok.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ColCalcActivity extends Activity {

	protected static final int DEFAULT_TRANSITION_ANIMATION_ENTER = R.anim.fade;
	protected static final int DEFAULT_TRANSITION_ANIMATION_EXIT = R.anim.hold;

	protected static final int DIALOG_REMOVE_CALC = 1;
	protected static final int DIALOG_REMOVE_PERSON = DIALOG_REMOVE_CALC+1;
	protected static final int DIALOG_REMOVE_PAY = DIALOG_REMOVE_PERSON+1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_REMOVE_CALC:
			return createDialogRemoveConfirm(DIALOG_REMOVE_CALC);
		case DIALOG_REMOVE_PERSON:
			return createDialogRemoveConfirm(DIALOG_REMOVE_PERSON);
		case DIALOG_REMOVE_PAY:
			return createDialogRemoveConfirm(DIALOG_REMOVE_PAY);
		}
		return null;
	}

	private Dialog createDialogRemoveConfirm(final int dialogRemove) {
		return new AlertDialog.Builder(this)
		.setIcon(R.drawable.content_discard)
		.setTitle(R.string.calculation_dialog_remove_text)
		.setPositiveButton(R.string.calculation_dialog_button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				handleRemoveConfirm(dialogRemove);
			}
		})
		.setNegativeButton(R.string.calculation_dialog_button_cancel, null)
		.create();
	}

	protected void handleRemoveConfirm(int dialogRemove) {}
}
