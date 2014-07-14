package com.bajicdusko.ajp.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Display;
import android.widget.ImageView;

public class ApplyImageAsync extends AsyncTask<Void, Void, Object> {
	public Context context = null;
	public Activity activity = null;
	public String url = "";
	public ImageView here = null;
	String url_md5 = "";
	

	public ApplyImageAsync(Activity activity, String url, ImageView here) {
		super();
		this.activity = activity;
		this.context = activity;
		this.url = url;
		url_md5 = md5(url);
		this.here = here;
		
		this.here.setTag(url);
	}
	
	@Override
	protected void onPreExecute() {
		try{ here.setImageDrawable(Drawable.createFromStream(context.getAssets().open("appicon"), ""));}
		catch(Exception ex)
		{
			ex.printStackTrace();			
		}
	}

	@Override
	protected Object doInBackground(Void... params) {
		
		File file = new File(context.getCacheDir(), "image_" + url_md5 + ".jpg");
		if (file.exists()) {		
			Bitmap x = BitmapFactory.decodeFile(file.getAbsolutePath());		
			return x;
		} 
		else {
			try
			{
				Bitmap b = FetchImage(url);
		    	file.getParentFile().mkdirs();
		    	file.createNewFile();
			    FileOutputStream out = new FileOutputStream(file);
			    b.compress(Bitmap.CompressFormat.JPEG, 90, out);
			    out.flush();
			    out.close();
			    
				return b;
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
			setBitmapToImageView(here, (Bitmap)result, url);
	}

	protected Bitmap FetchImage(String url) throws MalformedURLException, IOException
	{
		Bitmap image;
		URLConnection conn = new URL(url).openConnection();
		image = BitmapFactory.decodeStream(conn.getInputStream());
		return image;
	}

	private void setBitmapToImageView(ImageView view, Bitmap bitmap, String url)
	{
		try{
			if(view.getTag().equals(url))
				setImage(bitmap, view); 
		}
		catch(Exception ex){ }
		
	}
	
	public static final String md5(final String s) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	

	protected void setImage(Bitmap temp, ImageView a) {
		int[] dim=new int[2];
		dim=calculateAscpect(temp.getHeight(), temp.getWidth());

		a.getLayoutParams().height=dim[1];
		a.getLayoutParams().width=dim[0];
		a.setImageBitmap(temp);
	}
	

	
	protected int[] calculateAscpect(int height, int width) {
		int[] temp=new int[2];
		Display display = activity.getWindowManager().getDefaultDisplay();
		float aspect = (float)width/(float)height;
		float w=(float)display.getWidth();
		float h=w/aspect;
		int displayHeight=(int)h;
		int displayWidth=(int)w;
		temp[0]=displayWidth;
		temp[1]=displayHeight;
		return temp;
		
	}
}
