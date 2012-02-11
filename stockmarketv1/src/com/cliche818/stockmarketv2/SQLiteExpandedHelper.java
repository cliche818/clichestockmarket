package com.cliche818.stockmarketv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteExpandedHelper extends SQLiteOpenHelper {

	public SQLiteExpandedHelper(Context context) {
		super(context, "stockmarketv2", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creating database layout
		String query = "CREATE TABLE IF NOT EXISTS portfolio (_id INTEGER PRIMARY KEY AUTOINCREMENT, Ticker TEXT UNIQUE)" ;
		db.execSQL(query) ;
		query = "CREATE TABLE IF NOT EXISTS gamestats (_id INTEGER PRIMARY KEY AUTOINCREMENT, Field TEXT UNIQUE, Value REAL DEFAULT -1)" ;
		db.execSQL(query) ;
		
		// Inserting default status values
		ContentValues values = new ContentValues();
        values.put("Field", "GAMEACTIVE");
        values.put("Value", "0");
        db.insert("gamestats", "_id", values);
        values.put("Field", "FUNDS");
        values.put("Value", "0");
        db.insert("gamestats", "_id", values);
        values.put("Field", "STARTFUNDS");
        values.put("Value", "0");
        db.insert("gamestats", "_id", values);
        values.put("Field", "DAYSLEFT");
        values.put("Value", "-1");
        db.insert("gamestats", "_id", values);
        values.put("Field", "TRADES");
        values.put("Value", "0");
        db.insert("gamestats", "_id", values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS portfolio");
		db.execSQL("DROP TABLE IF EXISTS gamestats");
        onCreate(db);
	}

}
