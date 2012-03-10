package com.cliche818.stockmarketv2;
// test commit 
import android.content.Context;
import android.widget.Toast;

public class ErrorToast  {
	private Toast errorToast;
	
	public ErrorToast(Context context) {
		errorToast= Toast.makeText(context, "", Toast.LENGTH_SHORT);
	}
	
	public void showErrorMessage (String errorMsg)
	{
		errorToast.cancel();
		errorToast.setText(errorMsg);
		errorToast.show();
	}

}
