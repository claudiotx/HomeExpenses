package com.ctx.he;   

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Fragment that appears in the "content_frame"
 */
public class MeterFragment extends Fragment implements OnClickListener {
    public static final String CODE_ID = "99";
    private int code;
    public static final String TITLE = "Meter";
    private ListView lvMeasures;
    private MeasureDAO dao;
    private EditText etMeasureValue;
    private Button btnSubmit;
    private MeasuresListArrayAdapter measuresListAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meter_fragment, container, false);
        
        code = getArguments().getInt(CODE_ID);
        String title = getArguments().getString(TITLE);
        
        etMeasureValue = (EditText) rootView.findViewById(R.id.etMeasureValue);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        
        // Database
        dao = new MeasureDAO(getActivity());
        dao.open();
        ArrayList<MeasureModel> values;
        
        switch (code) {
        	case 1:
        		values = dao.getAllMeasures(MySQLiteHelper.TABLE_GAS);
        	break;
        	case 2:
        		values = dao.getAllMeasures(MySQLiteHelper.TABLE_ELECTRICITY);
        	break;
        	case 3:
        		values = dao.getAllMeasures(MySQLiteHelper.TABLE_WATER);
        	break;
        	case 4:
        		values = dao.getAllMeasures(MySQLiteHelper.TABLE_FIXED);
        	break;
        	
        	// SAFE
        	default:
        		values = dao.getAllMeasures(MySQLiteHelper.TABLE_GAS);
        	break;	
        }
        
        // use the MeasuresListArrayAdapter to show the elements in a ListView
        measuresListAdapter=new MeasuresListArrayAdapter(getActivity(),values);
        lvMeasures = (ListView) rootView.findViewById(R.id.lvMeasures);
        lvMeasures.setAdapter(measuresListAdapter);
        
        return rootView;

    }
    

	@Override
	public void onClick(View v) {
		 MeasuresListArrayAdapter adapter = (MeasuresListArrayAdapter) lvMeasures.getAdapter();
	   	 MeasureModel measure = null;
        int measureValue = Integer.parseInt(etMeasureValue.getText().toString());
        // save the new measure to the database
	        switch (code) {
	    	case 1:
	    		measure = dao.createMeasure(measureValue, MySQLiteHelper.TABLE_GAS);
	    	break;
	    	case 2:
	    		measure = dao.createMeasure(measureValue, MySQLiteHelper.TABLE_ELECTRICITY);
	    	break;
	    	case 3:
	    		measure = dao.createMeasure(measureValue, MySQLiteHelper.TABLE_WATER);
	    	break;
	    	case 4:
	    		measure = dao.createMeasure(measureValue, MySQLiteHelper.TABLE_FIXED);
	    	break;
	    }
        
        adapter.add(measure);
        adapter.notifyDataSetChanged();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
        	      Context.INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(etMeasureValue.getWindowToken(), 0);
	}

}