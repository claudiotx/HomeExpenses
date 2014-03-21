package com.ctx.he;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class MeasuresListArrayAdapter extends ArrayAdapter<MeasureModel> {
	
    private Context mContext;
    ArrayList<MeasureModel> mList;
    
    public MeasuresListArrayAdapter(Context context, ArrayList<MeasureModel> arrayList) 
    {
    		super(context, 0, arrayList);
            mContext=context;
            mList=arrayList;
    }

	@Override
	public int getCount() {
        return mList.size();
	}

	
	public void addItem(MeasureModel measure){
		mList.add(measure);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// getView method is called for each item of ListView
	@Override
	public View getView(int position, View view, ViewGroup vg) {
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.measures_list_item, null);
        
        // move the cursor to required position 
        //ListIterator<MeasureModel> iterator = mList.listIterator(position);
        
        //cursor.moveToPosition(position);
        
        // fetch the sender number and sms body from cursor
        	MeasureModel current = getItem(position);

        	//MeasureModel current = iterator.next();
            int measure = current.getValue();
            String date = current.getDate();
            

            TextView textViewMeasure=(TextView)view.findViewById(R.id.tvMeasure);
            TextView textViewDate=(TextView)view.findViewById(R.id.tvDate);
            
            textViewDate.setText(date);
            textViewMeasure.setText(String.valueOf(measure));


        return view;
        //int measure=cursor.getInt(cursor.getIndex("measure"));
        //String date=cursor.getString(cursor.getIndex("date"));
       

	}
	
}
