package com.bajicdusko.ajp.async;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bajicdusko.ajp.util.Utilities;

public class ApplySmallImageAsync extends ApplyImageAsync {

	public ApplySmallImageAsync(Activity activity, String url, ImageView here) {
		super(activity, url, here);
	}
	

	@Override
	protected Object doInBackground(Void... params) {
		
		Bitmap b = Utilities.imageDictionary.get(url);
		
		if (b != null) 
			return b;		
		else {
			try
			{
				Bitmap bitmap = FetchImage(url);				
				Utilities.imageDictionary.put(url, bitmap);
				return bitmap;
			} catch (MalformedURLException e) {				
				return null;
			} catch (IOException e) {				
				return null;
			}
		}			
	}
	
	@Override
	protected void onPostExecute(Object result) {		
		if ( result != null && result instanceof Bitmap) 
			here.setImageBitmap((Bitmap)result);
	}
}
