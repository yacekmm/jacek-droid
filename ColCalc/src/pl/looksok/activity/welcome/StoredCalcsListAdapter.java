package pl.looksok.activity.welcome;

import java.util.List;

import pl.looksok.R;
import pl.looksok.logic.CalculationLogic;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        
        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ResultHolder();
            holder.txtCalcTitle = (TextView)row.findViewById(R.id.storedCalc_title);

            row.setTag(holder);

            setupItem(position, holder);
        } else {
            holder = (ResultHolder)row.getTag();
        }
        
        return row;
    }

	private void setupItem(int position, ResultHolder holder) {
		CalculationLogic calc = items.get(position);
        holder.txtCalcTitle.setText(calc.getCalcTitle());
	}

	
    public static class ResultHolder {
    	TextView txtCalcTitle;
    }
}
