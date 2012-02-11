package com.cliche818.stockmarketv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StockDBAdapter {
    	public static final String STOCK_KEY = "stockSymbol"; //used to be known as key_title
    	public static final String STOCK_PRICE_OLD = "stockPriceOld"; //used to be known as key_body
    	public static final String KEY_ROWID = "_id";
    	public static final String STOCK_PRICE_NEW = "stockPriceNew";
    	public static final String STOCK_NUM = "stocksNumber";
    	
    	private static final String DATABASE_NAME = "data";
    	private static final int DATABASE_VERSION = 1;
    	private static final String DATABASE_TABLE = "stocks";
	   
    	private static final String TAG = "StockDBHelper";
    	private DatabaseHelper sDbHelper;
    	private SQLiteDatabase sDb;
	   
    	private static final String DATABASE_CREATE =
		        "create table stocks (_id integer primary key autoincrement, "
		        + "stockSymbol text not null, stockPriceNew text not null, stockPriceOld text no null, stocksNumber);";
	   
    	private final Context sCtx;
    	
    	private static class DatabaseHelper extends SQLiteOpenHelper {

            DatabaseHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
               
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(DATABASE_CREATE);
                

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
                db.execSQL("DROP TABLE IF EXISTS stocks");
                onCreate(db);
            }
        }

        /**
         * Constructor - takes the context to allow the database to be
         * opened/created
         * 
         * @param ctx the Context within which to work
         */
        public StockDBAdapter(Context ctx) {
            this.sCtx = ctx;
        }

        /**
         * Open the notes database. If it cannot be opened, try to create a new
         * instance of the database. If it cannot be created, throw an exception to
         * signal the failure
         * 
         * @return this (self reference, allowing this to be chained in an
         *         initialization call)
         * @throws SQLException if the database could be neither opened or created
         */
        public StockDBAdapter open() throws SQLException {
            sDbHelper = new DatabaseHelper(sCtx);
            sDb = sDbHelper.getWritableDatabase();
            return this;
        }

        public void close() {
            sDbHelper.close();
        }


        /**
         * Create a new note using the title and body provided. If the note is
         * successfully created return the new rowId for that note, otherwise return
         * a -1 to indicate failure.
         * 
         * @param title the title of the note
         * @param body the body of the note
         * @return rowId or -1 if failed
         */
        public long createStock(String stockSymbol, String stockPriceOld, String stockPriceNew, String stocksNumber) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(STOCK_KEY, stockSymbol);
            initialValues.put(STOCK_PRICE_NEW, stockPriceNew);
            initialValues.put(STOCK_PRICE_OLD, stockPriceOld);
            initialValues.put(STOCK_NUM, stocksNumber);
            return sDb.insert(DATABASE_TABLE, null, initialValues);
        }

        /**
         * Delete the note with the given rowId
         * 
         * @param rowId id of note to delete
         * @return true if deleted, false otherwise
         */
        public boolean deleteStock(long rowId) {

            return sDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        }

        /**
         * Return a Cursor over the list of all notes in the database
         * 
         * @return Cursor over all notes
         */
        public Cursor fetchAllStocks() {

            return sDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, STOCK_KEY,
            		STOCK_PRICE_NEW, STOCK_PRICE_OLD, STOCK_NUM}, null, null, null, null, null);
        }

        /**
         * Return a Cursor positioned at the note that matches the given rowId
         * 
         * @param rowId id of note to retrieve
         * @return Cursor positioned to matching note, if found
         * @throws SQLException if note could not be found/retrieved
         */
        public Cursor fetchStock(long rowId) throws SQLException {

            Cursor sCursor =

                sDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                		STOCK_KEY, STOCK_PRICE_NEW, STOCK_PRICE_OLD, STOCK_NUM}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
            if (sCursor != null) {
                sCursor.moveToFirst();
            }
            return sCursor;

        }

        /**
         * Update the note using the details provided. The note to be updated is
         * specified using the rowId, and it is altered to use the title and body
         * values passed in
         * 
         * @param rowId id of note to update
         * @param title value to set note title to
         * @param body value to set note body to
         * @return true if the note was successfully updated, false otherwise
         */
        public boolean updateStock(long rowId, String stockSymbol, String stockPriceNew, String stockPriceOld, String stockNumbers) {
            ContentValues args = new ContentValues();
            args.put(STOCK_KEY, stockSymbol);
            args.put(STOCK_PRICE_NEW, stockPriceNew);
            args.put(STOCK_PRICE_OLD, stockPriceOld);
            args.put(STOCK_NUM, stockNumbers);
            
            return sDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
        }
}
