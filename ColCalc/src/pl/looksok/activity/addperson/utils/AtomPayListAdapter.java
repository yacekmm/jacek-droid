package pl.looksok.activity.addperson.utils;

import java.util.List;

import pl.looksok.R;
import pl.looksok.logic.AtomPayment;
import pl.looksok.utils.FormatterHelper;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AtomPayListAdapter extends ArrayAdapter<AtomPayment> {
	protected static final String LOG_TAG = AtomPayListAdapter.class.getSimpleName();

	private List<AtomPayment> items;
	private int layoutResourceId;
	private Context context;

	public AtomPayListAdapter(Context context, int layoutResourceId, List<AtomPayment> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		AtomPaymentHolder holder = null;

		if(row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new AtomPaymentHolder();
			holder.atomPayment = items.get(position);
			holder.removePaymentButton = (ImageButton)row.findViewById(R.id.atomPay_removePay);
			holder.removePaymentButton.setTag(holder.atomPayment);

			holder.name = (TextView)row.findViewById(R.id.atomPay_name);
			setNameTextChangeListener(holder);
			holder.value = (TextView)row.findViewById(R.id.atomPay_value);
			setValueTextListeners(holder);

			row.setTag(holder);
		} else {
			holder = (AtomPaymentHolder)row.getTag();
		}

		setupItem(holder);
		return row;
	}

	private void setupItem(AtomPaymentHolder holder) {
		holder.name.setText(holder.atomPayment.getName());
		holder.value.setText(String.valueOf(holder.atomPayment.getValue()));
	}


	public static class AtomPaymentHolder {
		AtomPayment atomPayment;
		TextView name;
		TextView value;
		ImageButton removePaymentButton;
	}

	private void setNameTextChangeListener(final AtomPaymentHolder holder) {
		holder.name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				holder.atomPayment.setName(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});
	}

	private void setValueTextListeners(final AtomPaymentHolder holder) {
		holder.value.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try{
					holder.atomPayment.setValue(Double.parseDouble(s.toString()));
				}catch (NumberFormatException e) {
					Log.d(LOG_TAG, "this is not correct double number. Temporarily it will not be persisted: " + e.getMessage());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});

		holder.value.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				EditText editTextView = (EditText)v;
				if(hasFocus){
					double payDouble = FormatterHelper.readDoubleFromEditText(editTextView);
					if(payDouble == 0.0){
						editTextView.setText(context.getResources().getString(R.string.EnterPays_TextView_EmptyText));
					}
				}else{
					if(editTextView.getText().length() == 0)
						editTextView.setText(context.getResources().getString(R.string.EnterPays_TextView_ZeroValue));
				}
			}
		});
	}

	public List<AtomPayment> getItems() {
		return items;
	}
}
