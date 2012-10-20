package pl.looksok.activity.addperson.utils;

import java.util.List;

import pl.looksok.R;
import pl.looksok.logic.AtomPayment;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AtomPayListAdapter extends ArrayAdapter<AtomPayment> {

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
			holder.value = (TextView)row.findViewById(R.id.atomPay_value);

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
}
