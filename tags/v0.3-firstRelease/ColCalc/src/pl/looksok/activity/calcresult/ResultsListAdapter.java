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
	private boolean giftsIncluded = true;

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
            holder.txtBalance = (TextView)row.findViewById(R.id.calcItem_textView_pay);
            holder.txtPaidForGift = (TextView)row.findViewById(R.id.calcItem_textView_personPayForGift);
            holder.txtDebts = (TextView)row.findViewById(R.id.calcItem_details_debts);
            holder.txtRefunds = (TextView)row.findViewById(R.id.calcItem_details_refunds);
            holder.itemLayout = (RelativeLayout)row.findViewById(R.id.calcItem_mainLayout);
            holder.itemLayout.setTag(items.get(position));
            holder.imgRemovePerson = (ImageButton)row.findViewById(R.id.calcItem_image_delete);
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
        
        if(giftsIncluded){
        	holder.imgReceivesGift.setVisibility(View.VISIBLE);
        	holder.txtPaidForGift.setVisibility(View.VISIBLE);
        	if(!pd.receivesGift()){
        		holder.imgReceivesGift.setVisibility(View.GONE);
        		holder.txtPaidForGift.setText("" + pd.getHowMuchIPaidForGift());
        		holder.txtPaidForGift.setVisibility(View.VISIBLE);
        	}else{
        		holder.imgReceivesGift.setVisibility(View.VISIBLE);
        		holder.txtPaidForGift.setVisibility(View.GONE);
        	}
        }else{
        	holder.imgReceivesGift.setVisibility(View.GONE);
        	holder.txtPaidForGift.setVisibility(View.GONE);
        }



        String debtsText = CalculationPrinter.printPersonReturnsToOthersSimple(pd);
        setResultText(holder, debtsText, holder.txtDebts);
        
        String refundsText = CalculationPrinter.printPersonRefundsFromOthersSimple(pd);
        setResultText(holder, refundsText, holder.txtRefunds);
	}

	protected void setResultText(ResultHolder holder, String debtsText, TextView textView) {
		if(debtsText.length()>0){
			textView.setText(debtsText);
			textView.setVisibility(View.VISIBLE);
        } else{
        	textView.setVisibility(View.GONE);
        }
	}

	private void setBalance(ResultHolder holder, PersonData pp) {
		holder.txtBalance.setText(FormatterHelper.roundDouble(pp.getPayMadeByPerson(), 2) + 
				Currency.getInstance(Locale.getDefault()).getSymbol());
	}
	
    public class ResultHolder {
    	RelativeLayout itemLayout;
    	TextView txtName;
    	TextView txtBalance;
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
