package com.bajicdusko.ajp.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.bajicdusko.R;
import com.bajicdusko.ajp.iajp.IImageLoader;
import com.bajicdusko.ajp.util.Utilities;

public class FetchImageAsync extends AsyncTask<Void, Void, Object> implements IImageLoader {
	public Context context = null;
	public Activity activity = null;
	public String url = "";
	public ImageView here = null;
	String url_md5 = "";
    int DrawableResourceId = -1;
	

	public FetchImageAsync(Activity activity, String url, ImageView here) {
		super();
		this.activity = activity;
        this.context = activity;
		this.url = url;
		url_md5 = Utilities.md5(url);
		this.here = here;

        if(this.here != null)
		    this.here.setTag(url);
	}

    public FetchImageAsync(Activity activity, String url, ImageView here, int drawableResourceId) {
        super();
        this.activity = activity;
        this.context = activity;
        this.url = url;
        url_md5 = Utilities.md5(url);
        this.here = here;
        DrawableResourceId = drawableResourceId;

        if(this.here != null)
            this.here.setTag(url);
    }

    public FetchImageAsync(Context context, String url, ImageView here) {
        super();
        this.context = context;
        this.url = url;
        url_md5 = Utilities.md5(url);
        this.here = here;

        if(this.here != null)
            this.here.setTag(url);
    }

    protected int GetDefaultImageResource(){
        if (DrawableResourceId == -1)
            return R.drawable.ic_launcher;
        else
            return DrawableResourceId;
    }

	@Override
	protected void onPreExecute() {
		try{
            File file = new File(context.getCacheDir(), "image_" + url_md5 + ".jpg");
            if (!file.exists()) {
                here.setImageResource(GetDefaultImageResource());

            }
        }
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
        try {
            if(here != null) {
                if (result != null && result instanceof Bitmap)
                    setBitmapToImageView(here, (Bitmap) result, url);
                else
                    here.setImageResource(GetDefaultImageResource());
            }
        }catch (Exception ex){}
	}

    @Override
    public Bitmap SetDimensionsAndFetchImage(String url) throws IOException {
        int imageWidth = Utilities.GetDensityImageDimenssion(context, 384);
        int imageHeigth = Utilities.GetDensityImageDimenssion(context, 256);
        return FetchImage(url, imageWidth, imageHeigth);
    }

    public Bitmap FetchImage(String url, int reqWidth, int reqHeight) throws IOException
    {
        HttpURLConnection urlConnection = null;
        InputStream imageStream = null;
        InputStream in = null;
        InputStream in2 = null;
        try {
            final URL imgUrl = new URL(url);
            urlConnection = (HttpURLConnection) imgUrl.openConnection();
            imageStream = urlConnection.getInputStream();

            byte[] data = InputStreamTOByte(imageStream);
            try {
                in = byteTOInputStream(data);
                in2 = byteTOInputStream(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // resize the bitmap
            Bitmap bitmap = decodeSampledBitmap(in,in2, reqWidth, reqHeight);
            return bitmap;

        } catch (Exception e) {
            Log.e("IMG", "Error in down and process Bitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
                if (in2 != null){
                    in2.close();
                }
                if (imageStream != null){
                    imageStream.close();
                }
            } catch (final IOException e) {
                Log.e("IMG", "Error in when close the inputstream." + e);
            }
        }

        return null;
    }

    public byte[] InputStreamTOByte(InputStream in) throws IOException{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024*16];
        int count = -1;
        while((count = in.read(data,0,1024*16)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    public InputStream byteTOInputStream(byte[] in) throws Exception{

        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }


    public static Bitmap decodeSampledBitmap(InputStream streamOne, InputStream streamTwo, int reqWidth, int reqHeight) throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(streamOne, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap b = BitmapFactory.decodeStream(streamTwo, null, options);
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

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
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
