package pl.looksok.currencyedittext;

import pl.looksok.currencyedittext.utils.FormatterHelper;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class CurrencyEditText extends EditText {

	public CurrencyEditText(Context context) {
		super(context);
		setSpecificParams();
	}

	public CurrencyEditText(Context context, AttributeSet attrs){
		super(context, attrs);
		setSpecificParams();
	}

	public CurrencyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setSpecificParams();
	}

	private void setSpecificParams() {
		this.setOnFocusChangeListener(currencyEditTextFocusListener);
		this.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	private OnFocusChangeListener currencyEditTextFocusListener = new OnFocusChangeListener() {
		
		public void onFocusChange(View v, boolean hasFocus) {
			CurrencyEditText editTextView = CurrencyEditText.this;
			if(hasFocus){
				double payDouble = FormatterHelper.readDoubleFromEditText(editTextView);
				if(payDouble == 0.0){
					editTextView.setText("");
				}
			}else{
				if(editTextView.getText().length() == 0)
					editTextView.setText("0");
			}
		}
	};
}
