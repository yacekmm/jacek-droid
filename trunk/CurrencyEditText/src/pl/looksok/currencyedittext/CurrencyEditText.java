package pl.looksok.currencyedittext;

import pl.looksok.currencyedittext.utils.FormatterHelper;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CurrencyEditText extends EditText {

	private static final String USELESS_DOUBLE_FRACTION = ".0";
	private static final int CURRENCY_FRACTION_DIGITS = 2;
	private static final String LOG_TAG = CurrencyEditText.class.getSimpleName();

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
		this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		
		if(focused){
			Log.i(LOG_TAG, "focused!");
			String textToSet = getTextWithoutCurrrencyFormat(this.getText().toString());
			this.setText(textToSet);
		}else{
			Log.i(LOG_TAG, "lost focus!");
			double value = FormatterHelper.readDoubleFromEditText(this);
			String text = FormatterHelper.currencyFormat(value, CURRENCY_FRACTION_DIGITS);
			this.setText(text);
		}

		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	private String getTextWithoutCurrrencyFormat(String text) {
		double value = FormatterHelper.decodeValueFromCurrency(text);
		String textToSet = String.valueOf(value);
		if(textToSet.endsWith(USELESS_DOUBLE_FRACTION))
			textToSet = textToSet.substring(0, textToSet.indexOf(USELESS_DOUBLE_FRACTION));
		if(textToSet.equals("0"))
			textToSet = "";
		return textToSet;
	}
	
}
