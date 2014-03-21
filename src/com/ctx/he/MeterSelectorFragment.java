package com.ctx.he;   

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Fragment that appears in the "content_frame"
 */
public class MeterSelectorFragment extends Fragment {
    public static final String CODE_ID = "planet_number";
    RelativeLayout gas;
    RelativeLayout electricity;
    RelativeLayout water;
    RelativeLayout fixed;
    switchFragmentInterface mCallback;
    private MeasureDAO dao;
    TextView tvGasValue;
    TextView tvElectricityValue;
    TextView tvWaterValue;
    TextView tvFixedValue;
    TextView tvGasCost;
    TextView tvElectricityCost;
    TextView tvWaterCost;
    TextView tvFixedCost;
    TextView tvResumeTitle;
    TextView tvResumeCost;
    
    // Communication Interface to Activity
    public interface switchFragmentInterface {
        public void switchFragment(int fragmentId); // communication method to the activity
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (switchFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement switchFragmentInterface");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meter_selector_fragment, container, false);
        int i = getArguments().getInt(CODE_ID);
        
        // References
        gas =  (RelativeLayout) rootView.findViewById(R.id.layoutGas);
        electricity =  (RelativeLayout) rootView.findViewById(R.id.layoutElectricity);
        water =  (RelativeLayout) rootView.findViewById(R.id.layoutWater);
        fixed =  (RelativeLayout) rootView.findViewById(R.id.layoutFixed);
        tvFixedValue = (TextView) rootView.findViewById(R.id.tvFixedValue);
        tvGasValue = (TextView) rootView.findViewById(R.id.tvGasValue);
        tvElectricityValue = (TextView) rootView.findViewById(R.id.tvElectricityValue);
        tvWaterValue = (TextView) rootView.findViewById(R.id.tvWaterValue);
        tvGasCost =  (TextView) rootView.findViewById(R.id.tvGasCost);
        tvWaterCost =  (TextView) rootView.findViewById(R.id.tvWaterCost);
        tvElectricityCost =  (TextView) rootView.findViewById(R.id.tvElectricityCost);
        tvFixedCost =  (TextView) rootView.findViewById(R.id.tvFixedCost);
        tvResumeTitle =  (TextView) rootView.findViewById(R.id.tvResumeTitle);
        tvResumeCost = (TextView) rootView.findViewById(R.id.tvResumeCost);
        
        // Touch Listeners for Layouts
        gas.setOnTouchListener(new LayoutItemClickListener());
        electricity.setOnTouchListener(new LayoutItemClickListener());
        water.setOnTouchListener(new LayoutItemClickListener());
        fixed.setOnTouchListener(new LayoutItemClickListener());
        
        // Compute Current Consumption from Database
        dao = new MeasureDAO(getActivity());
        dao.open();
        
        // Gas
        ArrayList<MeasureModel> valueGas;
        valueGas = dao.getCurrentMonthMeasures(MySQLiteHelper.TABLE_GAS);
        int gasConsumptionRaw = computeMonthConsumption(valueGas);
        double unitCostGas = dao.readCosts("gas");
        double gasCost = unitCostGas * gasConsumptionRaw;
        tvGasCost.setText(String.format("%.1f",gasCost));
        tvGasValue.setText(String.valueOf(gasConsumptionRaw));
      
        // Electricity
        ArrayList<MeasureModel> valueElectricity;
        valueElectricity = dao.getCurrentMonthMeasures(MySQLiteHelper.TABLE_ELECTRICITY);
        int electricityConsumptionRaw = computeMonthConsumption(valueElectricity);
        double unitCostElectricity = dao.readCosts("electricity");
        double electricityCost = unitCostElectricity * electricityConsumptionRaw;
        tvElectricityCost.setText(String.format("%.1f",electricityCost));
        tvElectricityValue.setText(String.valueOf(electricityConsumptionRaw));
        
        // Water
        ArrayList<MeasureModel> valueWater;
        valueWater = dao.getCurrentMonthMeasures(MySQLiteHelper.TABLE_WATER);
        int waterConsumptionRaw = computeMonthConsumption(valueWater);
        double unitCostWater = dao.readCosts("water");
        double waterCost = unitCostWater * waterConsumptionRaw;
        tvWaterCost.setText(String.format("%.1f",waterCost));
        tvWaterValue.setText(String.valueOf(waterConsumptionRaw));
        
        // Fixed
        ArrayList<MeasureModel> valueFixed;
        valueFixed = dao.getCurrentMonthMeasures(MySQLiteHelper.TABLE_FIXED);
        double fixedConsumptionRaw = computeMonthFixed(valueFixed);
        tvFixedCost.setText(String.format("%.1f",fixedConsumptionRaw));

        // Month Cost
        double totalMonthCost = fixedConsumptionRaw + waterCost + electricityCost + gasCost;
        String currentMonth = getCurrentMonth();
        tvResumeTitle.setText("RUNNING COSTS ("+currentMonth+")");
        tvResumeCost.setText(String.format("%.1f",totalMonthCost));
        
        return rootView;
    }
    
    private String getCurrentMonth() {
    	SimpleDateFormat sdf;
    	sdf = new SimpleDateFormat("MMM");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
    }
    
    private int computeMonthFixed(ArrayList<MeasureModel> measures) {
    	int size = measures.size();
    	int total = 0;
    	for (int i=0; i<size; i++) {
    		total += measures.get(i).getValue();
    	}
    	return total;
    }
    
    private int computeMonthConsumption(ArrayList<MeasureModel> measures) {
    	int firstAndLast[] = new int[2];
    	int size = measures.size();
    	if(size == 0 || size == 1) {
    		return 0;    		 
    	}
    	else {
        	firstAndLast[0] = measures.get(0).getValue();
        	firstAndLast[1] = measures.get(size-1).getValue();
    		return firstAndLast[1] - firstAndLast[0];
    	}
    }
    
    /* The click listener for Layouts */
    private class LayoutItemClickListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(MotionEvent.ACTION_DOWN == event.getAction()){
            	int id = v.getId();
            	switch (id) {
            		case R.id.layoutGas:
            			mCallback.switchFragment(R.id.layoutGas);
            		break;	
               		case R.id.layoutElectricity:
               			mCallback.switchFragment(R.id.layoutElectricity);
                	break;	
               		case R.id.layoutWater:
               			mCallback.switchFragment(R.id.layoutWater);
                	break;	
               		case R.id.layoutFixed:
               			mCallback.switchFragment(R.id.layoutFixed);
                	break;	
            	}
            	
            }
            return false;
        }
    }
}