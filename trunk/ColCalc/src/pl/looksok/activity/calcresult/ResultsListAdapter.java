package pl.looksok.activity.calcresult;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import pl.looksok.R;
import pl.looksok.logic.PersonData;
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
        
        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ResultHolder();
            holder.txtName = (TextView)row.findViewById(R.id.calcItem_textView_name);
            holder.txtBalance = (TextView)row.findViewById(R.id.calcItem_textView_personPay);
            holder.txtCurrency = (TextView)row.findViewById(R.id.calcItem_textView_currency);
            holder.txtDetails = (TextView)row.findViewById(R.id.calcItem_details_text);
            holder.imgEditPerson = (ImageView)row.findViewById(R.id.calcItem_image_edit);
            holder.imgEditPerson.setTag(items.get(position));
            holder.imgRemovePerson = (ImageView)row.findViewById(R.id.calcItem_image_delete);
            holder.imgRemovePerson.setTag(items.get(position));
            
            row.setTag(holder);

            setupItem(position, holder);
        } else {
            holder = (ResultHolder)row.getTag();
        }
        
        return row;
    }

	private void setupItem(int position, ResultHolder holder) {
		PersonData pp = items.get(position);
        holder.txtName.setText(pp.getName());
        setBalance(holder, pp);
        holder.txtCurrency.setText(Currency.getInstance(Locale.getDefault()).getSymbol());
        holder.txtDetails.setText(pp.printPersonReturnsToOthers(context.getString(R.string.calculation_printText_return),
        		context.getString(R.string.calculation_printText_for)));
	}

	private void setBalance(ResultHolder holder, PersonData pp) {
		double refund = pp.getTotalRefundForThisPerson();
		double toReturn = pp.getToReturn();
		
		if(refund > toReturn){
			holder.txtBalance.setText("+" + FormatterHelper.roundDouble(refund,2));
			holder.txtBalance.setTextAppearance(context, R.style.balancePositiveText);
			holder.txtCurrency.setTextAppearance(context, R.style.balancePositiveText);
		}else{
			holder.txtBalance.setText("-" +  FormatterHelper.roundDouble(toReturn, 2));
			holder.txtBalance.setTextAppearance(context, R.style.balanceNegativeText);
			holder.txtCurrency.setTextAppearance(context, R.style.balanceNegativeText);
		}
	}
	
    public static class ResultHolder {
    	TextView txtName;
    	TextView txtBalance;
    	TextView txtCurrency;
    	TextView txtDetails;
    	ImageView imgEditPerson;
    	ImageView imgRemovePerson;
    }
}
