package pl.looksok.activity.welcome;

import java.util.List;

import pl.looksok.R;
import pl.looksok.currencyedittext.utils.FormatterHelper;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.utils.Constants;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoredCalcsListAdapter extends ArrayAdapter<CalculationLogic> {

	private List<CalculationLogic> items;
	private int layoutResourceId;
	private Context context;

	public StoredCalcsListAdapter(Context context, int layoutResourceId, List<CalculationLogic> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ResultHolder holder = null;

		if(items.size() == 0)
			Log.e("TAG", "NO CALCS!!!!");

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new ResultHolder();
		holder.calc = items.get(position);
		holder.removeCalcButton = (ImageButton)row.findViewById(R.id.storedCalc_removeCalc);
		holder.removeCalcButton.setTag(holder.calc);
		holder.shareCalcButton = (ImageButton)row.findViewById(R.id.storedCalc_shareCalc);
		holder.shareCalcButton.setTag(holder.calc);
		holder.calcNameLayout = (RelativeLayout)row.findViewById(R.id.storedCalc_calcDetails);

		holder.calcDetailsLayout = (RelativeLayout)row.findViewById(R.id.storedCalc_calcDetails);
		holder.calcDetailsLayout.setTag(holder.calc);
		holder.txtCalcName = (TextView)row.findViewById(R.id.storedCalc_calcName);
		holder.txtCalcDate = (TextView)row.findViewById(R.id.calcDetailsHeader_calcDate);
		holder.txtCalcDate.setTextColor(context.getResources().getColor(R.color.gray_dark));
		holder.txtCalcTotal = (TextView)row.findViewById(R.id.calcDetailsHeader_calcTotal);
		holder.txtCalcTotal.setTextColor(context.getResources().getColor(R.color.gray_dark));
		holder.txtCalcPersons = (TextView)row.findViewById(R.id.calcDetailsHeader_calcPersons);
		holder.txtCalcPersons.setTextColor(context.getResources().getColor(R.color.gray_dark));

		setupItemContent(holder);
		setupItemStyle(holder);
		return row;
	}

	private void setupItemContent(ResultHolder holder) {
		holder.txtCalcName.setText(holder.calc.getCalcName());
		holder.txtCalcDate.setText(holder.calc.getDateSaved().toString(Constants.SIMPLE_DATE_FORMAT));
		holder.txtCalcTotal.setText(FormatterHelper.currencyFormat(holder.calc.getTotalPay(), 0));
		holder.txtCalcPersons.setText(String.valueOf(holder.calc.getTotalPersons()));
	}
	
	private void setupItemStyle(ResultHolder holder) {
		if(holder.calc.getCalculationType().equals(CalculationType.RESTAURANT)){
			holder.removeCalcButton.setBackgroundResource(R.drawable.button_bgnd_restaurant_right);
			holder.shareCalcButton.setBackgroundResource(R.drawable.button_bgnd_restaurant_middle);
			holder.calcNameLayout.setBackgroundResource(R.drawable.button_bgnd_restaurant_left);
		}
	}

	public static class ResultHolder {
		RelativeLayout calcNameLayout;
		CalculationLogic calc;
		TextView txtCalcName;
		ImageButton removeCalcButton;
		ImageButton shareCalcButton;
		RelativeLayout calcDetailsLayout;
		TextView txtCalcDate;
		TextView txtCalcTotal;
		TextView txtCalcPersons;
	}

	public List<CalculationLogic> getItems() {
		return items;
	}
}
