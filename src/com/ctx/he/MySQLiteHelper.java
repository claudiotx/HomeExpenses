package com.ctx.he;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	  public static final String TABLE_GAS = "gas";
	  public static final String TABLE_ELECTRICITY = "electricity";
	  public static final String TABLE_WATER = "water";
	  public static final String TABLE_FIXED = "fixed";
	  public static final String TABLE_UNITS = "units";
	  public static final String _ID = "_id";
	  public static final String _VALUE = "value";
	  public static final String _DATE = "date";
	  public static final String _METER = "meter";
	  public static final String _COST = "cost";
	  
	  private static final String DATABASE_NAME = "measures.db";
	  private static final int DATABASE_VERSION = 1;
	  
	  // Database creation sql statement
	  private static final String DATABASE_CREATE_GAS_TABLE = "create table "
	      + TABLE_GAS +"(" 
		  + _ID + " integer primary key autoincrement, " 
	      + _VALUE + " integer not null,"
	      + _DATE + " text not null"
	      +");";
	  
	  private static final String DATABASE_CREATE_UNITS_TABLE = "create table "
		      + TABLE_UNITS +"(" 
			  + _ID + " integer primary key autoincrement, " 
		      + _METER + " text not null,"
		      + _COST + " real not null,"
		      + _DATE + " text not null"
		      +");";
	  
	  private static final String DATABASE_CREATE_ELECTRICITY_TABLE = "create table "
	      + TABLE_ELECTRICITY +"(" 
		  + _ID + " integer primary key autoincrement, " 
	      + _VALUE + " integer not null,"
	      + _DATE + " text not null"
	      +");";
			  
	  private static final String DATABASE_CREATE_WATER_TABLE = "create table "
	      + TABLE_WATER +"(" 
		  + _ID + " integer primary key autoincrement, " 
	      + _VALUE + " integer not null,"
	      + _DATE + " text not null"
	      +");";	  
	  
	  private static final String DATABASE_CREATE_FIXED_TABLE = "create table "
	      + TABLE_FIXED +"(" 
		  + _ID + " integer primary key autoincrement, " 
	      + _VALUE + " integer not null,"
	      + _DATE + " text not null"
	      +");";
	  
	  public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_UNITS_TABLE);
		db.execSQL(DATABASE_CREATE_GAS_TABLE);
		db.execSQL(DATABASE_CREATE_ELECTRICITY_TABLE);
		db.execSQL(DATABASE_CREATE_WATER_TABLE);
		db.execSQL(DATABASE_CREATE_FIXED_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_UNITS_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_GAS_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_ELECTRICITY_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_ELECTRICITY_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_FIXED_TABLE);
	    onCreate(db);
	}

}
