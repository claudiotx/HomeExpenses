package com.ctx.he;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MeasureDAO {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] alls = { MySQLiteHelper._ID,
			MySQLiteHelper._DATE,  MySQLiteHelper._VALUE };

	public MeasureDAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	// DB Methods
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * @param value
	 * @param table
	 * @return MeasureModel object
	 */
	public MeasureModel createMeasure(int value, String table) {
		ContentValues values = new ContentValues();
		String date = getCurrentDate('x');
		values.put(MySQLiteHelper._VALUE, value);
		values.put(MySQLiteHelper._DATE, date);
		long insertId = database.insert(table, null,
				values);
		Cursor cursor = database.query(table,
				alls, MySQLiteHelper._ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		MeasureModel newMeasure = cursorToMeasureModel(cursor);
		cursor.close();
		return newMeasure;
	}

	public void deleteMeasure(MeasureModel measure, String table) {
		long id = measure.getId();
		database.delete(MySQLiteHelper.TABLE_GAS, MySQLiteHelper._ID
				+ " = " + id, null);
	}
	
	public void deleteAllMeasures() {
		database.delete(MySQLiteHelper.TABLE_ELECTRICITY, null, null);
		database.delete(MySQLiteHelper.TABLE_FIXED, null, null);
		database.delete(MySQLiteHelper.TABLE_GAS, null, null);
		database.delete(MySQLiteHelper.TABLE_WATER, null, null);
		database.delete(MySQLiteHelper.TABLE_UNITS, null, null);
	}
	
    public double readCosts(String meter) {
		// Query and Iteratre through Results filtering only current month
		Cursor cursor = database.query(
				MySQLiteHelper.TABLE_UNITS,
				new String[] {MySQLiteHelper._COST}, 
				MySQLiteHelper._METER + " LIKE ?", 
				new String[] {meter},
				null,  // groupby
				null,  // having
				null); //order by
		
		if(cursor.moveToFirst()) {
			double cost = cursor.getDouble(0);
			cursor.close();
			return cost;
		}
		
		cursor.close();
		return 0;
    }

	/**
	 * @param table
	 * @return
	 */
	public ArrayList<MeasureModel> getAllMeasures(String table) {
		ArrayList<MeasureModel> measures = new ArrayList<MeasureModel>();
		
		// Query and Iteratre through Results
		Cursor cursor = database.query(table,
				alls, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MeasureModel comment = cursorToMeasureModel(cursor);
			measures.add(comment);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return measures; //ArrayList
	}


	private MeasureModel cursorToMeasureModel(Cursor cursor) {
		MeasureModel measure = new MeasureModel();
		measure.setId(cursor.getLong(0));
		measure.setValue(cursor.getInt(2));
		measure.setDate(cursor.getString(1));
		return measure;
	}

	public String getCurrentDate(char filter) {
		SimpleDateFormat sdf;
		
		if (filter == 'm') {
			sdf = new SimpleDateFormat("MM");
		}
		else if (filter == 'r') {
			return "12-02-14 12:12";
		}
		else {
			sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
		}		
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
	}

	public ArrayList<MeasureModel> getCurrentMonthMeasures(String table) {
		
		String currentMonth = getCurrentDate('m');
		ArrayList<MeasureModel> currentMonthMeasures = new ArrayList<MeasureModel>();
		
		// Query and Iteratre through Results filtering only current month
		Cursor cursor = database.query(
				table,
				alls, 
				MySQLiteHelper._DATE + " LIKE ?", 
				new String[] {"%-"+currentMonth+"-%"},
				null,  // groupby
				null,  // having
				MySQLiteHelper._ID); //order by
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MeasureModel measure = cursorToMeasureModel(cursor);
			currentMonthMeasures.add(measure);
			cursor.moveToNext();
		};
		// make sure to close the cursor
		cursor.close();
		
		return currentMonthMeasures;
	}
	

    
    public void updateCost(String meter, double costValue) {
    	database = dbHelper.getWritableDatabase();
    	String date = getCurrentDate('x');
    	
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper._METER, meter);
		values.put(MySQLiteHelper._COST, costValue);
		values.put(MySQLiteHelper._DATE, date);
	
		// Update Row
		int results = database.update(MySQLiteHelper.TABLE_UNITS, 
				values, 
				MySQLiteHelper._METER + " LIKE ?", 
				new String[] {meter});
		
		// SAFE ==insert
		if (results == 0) {
			database.insert(MySQLiteHelper.TABLE_UNITS, null,
					values);

		}
		
		dbHelper.close();
    }

}
