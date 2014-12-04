package com.bajicdusko.ajp.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Display;
import android.widget.ImageView;

import com.bajicdusko.R;
import com.bajicdusko.ajp.iajp.IImageLoader;
import com.bajicdusko.ajp.util.Utilities;

public class FetchLargeImage extends AsyncTask<Void, Void, Object> implements IImageLoader {
	public Context context = null;
	public Activity activity = null;
	public String url = "";
	public ImageView here = null;
	String url_md5 = "";
	

	public FetchLargeImage(Activity activity, String url, ImageView here) {
		super();
		this.activity = activity;
        this.context = activity;
		this.url = url;
		url_md5 = Utilities.md5(url);
		this.here = here;

        if(this.here != null)
		    this.here.setTag(url);
	}

    public FetchLargeImage(Context context, String url, ImageView here) {
        super();
        this.context = context;
        this.url = url;
        url_md5 = Utilities.md5(url);
        this.here = here;

        if(this.here != null)
            this.here.setTag(url);
    }
	
	@Override
	protected void onPreExecute() {
		try{ here.setImageResource(R.drawable.ic_launcher); }
		catch(Exception ex)
		{

		}
	}

	@Override
	protected Object doInBackground(Void... params) {

        if(context != null) {
            try {
                File file = new File(context.getCacheDir(), "image_" + url_md5 + ".jpg");
                if (file.exists()) {
                    BitmapFactory.Options ops = new BitmapFactory.Options();
                    ops.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap x = BitmapFactory.decodeFile(file.getAbsolutePath(), ops);
                    return x;
                } else {
                    try {
                        Bitmap b = SetDimensionsAndFetchImage(url);
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
            catch (Exception ex){
                return null;
            }
        }
        else
            return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {		
		if ( result != null && result instanceof Bitmap) 
			setBitmapToImageView(here, (Bitmap)result, url);
	}

    @Override
    public Bitmap SetDimensionsAndFetchImage(String url) throws IOException {
        return FetchImage(url, R.dimen.large_image_width, R.dimen.large_image_height);
    }

	public Bitmap FetchImage(String url, int reqWidth, int reqHeight) throws IOException
	{
		Bitmap image = decodeSampledBitmap(new URL(url), reqWidth, reqHeight);
		return image;
	}

    public static Bitmap decodeSampledBitmap(URL url, int reqWidth, int reqHeight) throws IOException {
        URLConnection connection = url.openConnection();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(connection.getInputStream(), null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        URLConnection connection2 = url.openConnection();

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap b = BitmapFactory.decodeStream(connection2.getInputStream(), null, options);
        return b;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

	private void setBitmapToImageView(ImageView view, Bitmap bitmap, String url)
	{
		try{
			if(view.getTag().equals(url))
                view.setImageBitmap(bitmap);
		}
		catch(Exception ex){ }
		
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
