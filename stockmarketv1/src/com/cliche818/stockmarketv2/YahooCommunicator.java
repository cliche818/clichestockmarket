package com.cliche818.stockmarketv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Dialog;
import android.database.Cursor;
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
	
	public YahooCommunicator (Main main)
	{
		mService = main;
	}
	
	public void getStockQuote (String symbolInput)
	{
		getStocksAsync getStockTask = new getStocksAsync();
		getStockTask.execute(symbolInput.replace(" ", ""));
		mCaller = Functions.getStockQuote;
		
	}
	
	public void refreshOne (Cursor cur, Dialog sellDialog)
	{
		mCur = cur;
		mSellDialog = sellDialog;
		getStocksAsync refreshOneTask = new getStocksAsync();
		refreshOneTask.execute(cur.getString(1));
		mCaller = Functions.refreshOne;
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
			Log.i(TAG, stockTxt);
			
			switch (mCaller) {
			case getStockQuote:
				mService.getQuoteButtonAftermath(stockTxt);
				break;
				
			case refreshOne:
				mService.refreshOneStockAftermath(stockTxt, mCur, mSellDialog);
				break;
			}
		}	
	}
		
}
