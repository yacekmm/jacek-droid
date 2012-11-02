package pl.looksok.currencyedittexttest;

import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EditTextTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_test);
        
        setFormattedText();
    }

	private void setFormattedText() {
		TextView textView = (TextView)findViewById(R.id.curTextView);
		double value = 25;
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMaximumFractionDigits(0);
		
		textView.setText(nf.format(value));
	}
    
}
