package com.cliche818.stockmarketv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class titlepage extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titlepage);
        
        Handler titlepagetimer = new Handler ();
        titlepagetimer.postDelayed (new titlePageHandler(), 50);
    }
    
    class titlePageHandler implements Runnable{
    	public void run(){
    		startActivity(new Intent(getApplication (), Main.class));
    		titlepage.this.finish ();
    	}
    }
}