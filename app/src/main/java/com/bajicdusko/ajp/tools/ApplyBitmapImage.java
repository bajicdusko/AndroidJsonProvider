package com.bajicdusko.ajp.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bajicdusko.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Bajic on 25-Oct-14.
 */
public class ApplyBitmapImage extends ApplyImageAsync {

    public ApplyBitmapImage(FragmentActivity activity, String url, ImageView here) {
        super(activity, url, here);
    }

    public ApplyBitmapImage(Context context, String url, ImageView here) {
        super(context, url, here);
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
            File file = new File(context.getCacheDir(), "image_" + url_md5 + ".png");
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
                    b.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();

                    return b;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        else
            return null;
    }

    @Override
    public Bitmap SetDimensionsAndFetchImage(String url) throws IOException {
        return FetchImage(url, R.dimen.bitmap_width, R.dimen.bitmap_height);
    }
}
