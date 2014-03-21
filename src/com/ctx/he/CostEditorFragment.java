package com.ctx.he;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CostEditorFragment extends Fragment implements OnClickListener{
	// Database fields
	private MeasureDAO dao;
	EditText etUnitCostGas;
	EditText etUnitCostElectricity;
	EditText etUnitCostWater;
	Button btnUpdateCosts;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cost_editor_fragment, container, false);
        
        // Layout References
        etUnitCostGas = (EditText) rootView.findViewById(R.id.etUnitCostGas);
        etUnitCostElectricity = (EditText) rootView.findViewById(R.id.etUnitCostElectricity);
        etUnitCostWater = (EditText) rootView.findViewById(R.id.etUnitCostWater);
        btnUpdateCosts = (Button) rootView.findViewById(R.id.btnUpdateCosts);
        
        // Get Costs from Database
        dao = new MeasureDAO(getActivity());
        dao.open();
        double costGas = dao.readCosts("gas");
        double costElectricity = dao.readCosts("electricity");
        double costWater = dao.readCosts("water");
        
        // Update Layout Values
        etUnitCostGas.setText(String.valueOf(costGas));
        etUnitCostElectricity.setText(String.valueOf(costElectricity));
        etUnitCostWater.setText(String.valueOf(costWater));
        
        // Button
        btnUpdateCosts.setOnClickListener(this);
        
        return rootView;
    }

	@Override
	public void onClick(View v) {
		double gasCost = Double.parseDouble(etUnitCostGas.getText().toString());
		dao.updateCost("gas", gasCost);
		double waterCost = Double.parseDouble(etUnitCostWater.getText().toString());
		dao.updateCost("water", waterCost);
		double electricityCost = Double.parseDouble(etUnitCostElectricity.getText().toString());
		dao.updateCost("electricity", electricityCost);
	}
}
