package com.cliche818.stockmarketv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class YahooCommunicator {
	//debug constants
	private static final String TAG = "YahooCommunicator";
		
	private Main mService;
	public enum Functions {unknown, getStockQuote, refreshOne, refreshAll};
	private Functions mCaller = Functions.unknown;
	private Cursor mCur;
	private Dialog mSellDialog;
	private boolean isRunning = false;
	
	public YahooCommunicator (Main main)
	{
		mService = main;
	}
	
	public boolean getStockQuote (String symbolInput)
	{
		if (isRunning)
			return false;
		isRunning = true;
		getStocksAsync getStockTask = new getStocksAsync();
		getStockTask.execute(symbolInput.replace(" ", ""));
		mCaller = Functions.getStockQuote;
		return true;
		
	}
	
	public boolean refreshOne (Cursor cur, Dialog sellDialog)
	{
		if (isRunning)
			return false;
		isRunning = true;
		mCur = cur;
		mSellDialog = sellDialog;
		getStocksAsync refreshOneTask = new getStocksAsync();
		refreshOneTask.execute(cur.getString(1));
		mCaller = Functions.refreshOne;
		return true;
	}
	
	public boolean refreshAll (Cursor cur)
	{
		if (isRunning)
			return false;
		isRunning = true;
		mCur = cur;
		getStocksAsync refreshAllTask = new getStocksAsync();
		refreshAllTask.execute (cur.getString(1));
		mCaller = Functions.refreshAll;
		return true;
	}	
	
	
	public void moreInfo (Cursor cur)
	{
		Uri uri = Uri.parse ("http://ca.finance.yahoo.com/q?s=" + cur.getString (1) + ".to&ql=1");
    	Intent intent = new Intent (Intent.ACTION_VIEW, uri);
    	mService.startActivity (intent);
    	cur.close();
	}
	
	/*
	 * This sub class is to asynchronously get stock data for GetStock Module
	 * @param 1st argument is params, the type of the parameters sent to the task upon execution
	 * @param 2nd argument is progress, the type of progress units published during the background computation
	 * @param Result, the type of the result of the background computation
	 */
	private class getStocksAsync extends AsyncTask <String, Void, String>{	
		protected String doInBackground(String... symbolInput) {			
			URL url;
			String stockTxt = "";
			
			
			try {
				// getting info from Yahoo Finance API [meat of the program]
				url = new URL(
						"http://download.finance.yahoo.com/d/quotes.csv?s="
								+ symbolInput[0] + ".to" +"&f=sl1p2n");
				
				//!!!!!!added .to TSX stocks only!!!!!!!!!!!//
				
				/*
				 * s = stock symbol
				 * l1 = last trade (price only)
				 * p2 = change in percent
				 * n = name of company
				 */

				InputStream stream = url.openStream();
				
				//convert stream to string
				//reason to use bufferedReader is so there are more functions to use: readLine()
				BufferedReader r = new BufferedReader(new InputStreamReader(stream));
				
				
				stockTxt = r.readLine();

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return stockTxt;
		}


		protected void onPostExecute(String stockTxt) {
			Log.d(TAG, stockTxt);
			isRunning = false;
			switch (mCaller) {
			case getStockQuote:
				mService.getQuoteButtonAftermath(stockTxt);
				break;
				
			case refreshOne:
				mService.refreshOneAftermath(stockTxt, mCur, mSellDialog);
				break;
			
			case refreshAll:
				mService.refreshALLAftermath(stockTxt, mCur);
				break;
			}

		}	
	}
		
}
