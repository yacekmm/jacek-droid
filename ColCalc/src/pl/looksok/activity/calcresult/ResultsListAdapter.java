package pl.looksok.activity.calcresult;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import pl.looksok.R;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.utils.CalculationPrinter;
import pl.looksok.utils.FormatterHelper;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultsListAdapter extends ArrayAdapter<PersonData> {
	@SuppressWarnings("unused")
	private static final String LOG_TAG = ResultsListAdapter.class.getSimpleName();
	
	private List<PersonData> items;
	private int layoutResourceId;
	private Context context;

	public ResultsListAdapter(Context context, int layoutResourceId, List<PersonData> items) {
		super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
		this.items = items;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ResultHolder holder = null;
        
//        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ResultHolder();
            holder.txtName = (TextView)row.findViewById(R.id.calcItem_textView_name);
            holder.txtBalance = (TextView)row.findViewById(R.id.calcItem_textView_personPay);
            holder.txtPaidForGift = (TextView)row.findViewById(R.id.calcItem_textView_personPayForGift);
            holder.txtDebts = (TextView)row.findViewById(R.id.calcItem_details_debts);
            holder.txtRefunds = (TextView)row.findViewById(R.id.calcItem_details_refunds);
            holder.imgEditPerson = (ImageView)row.findViewById(R.id.calcItem_image_edit);
            holder.imgEditPerson.setTag(items.get(position));
            holder.imgRemovePerson = (ImageView)row.findViewById(R.id.calcItem_image_delete);
            holder.imgRemovePerson.setTag(items.get(position));
            holder.imgReceivesGift = (ImageView)row.findViewById(R.id.calcItem_image_receivesGift);
            
//            row.setTag(holder);
//        } else {
//            holder = (ResultHolder)row.getTag();
//        }
        setupItem(position, holder);
        
        return row;
    }

	private void setupItem(int position, ResultHolder holder) {
		PersonData pd = items.get(position);
		if(holder.txtName.getText().toString().equals(pd.getName()))
			return;

		holder.txtName.setText(pd.getName());
        setBalance(holder, pd);
        if(!pd.receivesGift()){
        	holder.imgReceivesGift.setVisibility(View.GONE);
	        holder.txtPaidForGift.setText("Zapłacił za prezent: " + pd.getHowMuchIPaidForGift());
		}

        String debtsText = CalculationPrinter.printPersonReturnsToOthersSimple(pd);
        holder.txtDebts.setText(debtsText);
        String refundsText = CalculationPrinter.printPersonRefundsFromOthersSimple(pd);
        holder.txtRefunds.setText(refundsText);
	}

	private void setBalance(ResultHolder holder, PersonData pp) {
		
		if(pp.getPayMadeByPerson() > pp.getHowMuchPersonShouldPay()){
			holder.txtBalance.setTextAppearance(context, R.style.balancePositiveText);
		}else{
			holder.txtBalance.setTextAppearance(context, R.style.balanceNegativeText);
		}
		
		holder.txtBalance.setText(context.getString(R.string.calculation_TextView_personPaid_text) + "\n" + 
				FormatterHelper.roundDouble(pp.getPayMadeByPerson(), 2) + 
				Currency.getInstance(Locale.getDefault()).getSymbol());
	}
	
    public class ResultHolder {
    	TextView txtName;
    	TextView txtBalance;
    	TextView txtPaidForGift;
    	TextView txtDebts;
    	TextView txtRefunds;
    	ImageView imgEditPerson;
    	ImageView imgRemovePerson;
    	ImageView imgReceivesGift;
    }

	public List<PersonData> getItems() {
		return items;
	}
}
