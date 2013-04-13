package pl.looksok.activity.calcresult;

import java.util.List;

import pl.looksok.R;
import pl.looksok.currencyedittext.utils.FormatterHelper;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.utils.CalculationPrinter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultsListAdapter extends ArrayAdapter<PersonData> {
	@SuppressWarnings("unused")
	private static final String LOG_TAG = ResultsListAdapter.class.getSimpleName();

	private List<PersonData> items;
	private int layoutResourceId;
	private Context context;
	private CalculationLogic calc;


	public ResultsListAdapter(Context context, int layoutResourceId, List<PersonData> items, CalculationLogic calc) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
		this.calc = calc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ResultHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new ResultHolder();
		holder.txtName = (TextView)row.findViewById(R.id.calcItem_textView_name);
		holder.txtBalance = (TextView)row.findViewById(R.id.calcItem_textView_pay);
		holder.txtShouldPay = (TextView)row.findViewById(R.id.calcItem_textView_shouldPay);
		holder.txtPaidForGift = (TextView)row.findViewById(R.id.calcItem_textView_personPayForGift);
		holder.txtDebts = (TextView)row.findViewById(R.id.calcItem_details_debts);
		holder.txtRefunds = (TextView)row.findViewById(R.id.calcItem_details_refunds);
		holder.itemLayout = (RelativeLayout)row.findViewById(R.id.calcItem_mainLayout);
		holder.itemLayout.setTag(items.get(position));
		holder.imgRemovePerson = (ImageButton)row.findViewById(R.id.calcItem_image_delete);
		holder.imgRemovePerson.setTag(items.get(position));
		holder.imgReceivesGift = (ImageView)row.findViewById(R.id.calcItem_image_receivesGift);

		setupItem(position, holder);

		return row;
	}

	private void setupItem(int position, ResultHolder holder) {
		PersonData pd = items.get(position);
		if(holder.txtName.getText().toString().equals(pd.getName()))
			return;

		holder.txtName.setText(pd.getName());
		setHowMuchUserPaidText(holder, pd);

		holder.imgReceivesGift.setVisibility(View.GONE);
		holder.txtPaidForGift.setVisibility(View.GONE);
		holder.txtShouldPay.setVisibility(View.GONE);
		
		if(calc.getCalculationType().equals(CalculationType.POTLUCK_PARTY_WITH_GIFT_V2)){
			holder.imgReceivesGift.setVisibility(View.VISIBLE);
			holder.txtPaidForGift.setVisibility(View.VISIBLE);
			if(!pd.receivesGift()){
				holder.imgReceivesGift.setVisibility(View.GONE);
				holder.txtPaidForGift.setText(FormatterHelper.currencyFormat(pd.getHowMuchIPaidForGift(), 2));
				holder.txtPaidForGift.setVisibility(View.VISIBLE);
			}else{
				holder.imgReceivesGift.setVisibility(View.VISIBLE);
				holder.txtPaidForGift.setVisibility(View.GONE);
			}
		}else if(calc.getCalculationType().equals(CalculationType.RESTAURANT)){
			holder.txtShouldPay.setVisibility(View.VISIBLE);
			holder.txtShouldPay.setText(FormatterHelper.currencyFormat(pd.getHowMuchPersonShouldPay(), 2));
		}
		

		String debtsText = CalculationPrinter.printPersonDebtsSimple(pd, context.getString(R.string.email_calcResult_printText_for));
		setCalcResultText(debtsText, holder.txtDebts);

		String refundsText = CalculationPrinter.printPersonRefundsFromOthersSimple(pd, context.getString(R.string.email_calcResult_printText_from));
		setCalcResultText(refundsText, holder.txtRefunds);
	}

	protected void setCalcResultText(String text, TextView textView) {
		if(text.length()>0){
			textView.setText(text);
			textView.setVisibility(View.VISIBLE);
		} else{
			textView.setVisibility(View.GONE);
		}
	}

	private void setHowMuchUserPaidText(ResultHolder holder, PersonData pp) {
		holder.txtBalance.setText(FormatterHelper.currencyFormat(pp.getHowMuchPersonPaid(), 2));
	}

	public class ResultHolder {
		RelativeLayout itemLayout;
		TextView txtName;
		TextView txtBalance;
		TextView txtShouldPay;
		TextView txtPaidForGift;
		TextView txtDebts;
		TextView txtRefunds;
		ImageButton imgRemovePerson;
		ImageView imgReceivesGift;
	}

	public List<PersonData> getItems() {
		return items;
	}
}
