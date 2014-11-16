package com.bajicdusko.ajp.tools;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bajicdusko.R;

public class ApplySmallImageAsync extends ApplyImageAsync {

	public ApplySmallImageAsync(Activity activity, String url, ImageView here) {
		super(activity, url, here);
	}

    @Override
    protected void onPreExecute() {
        try{ here.setImageResource(R.drawable.ic_launcher); }
        catch(Exception ex)
        {

        }
    }

    @Override
    public Bitmap SetDimensionsAndFetchImage(String url) throws IOException {
        return FetchImage(url, R.dimen.thumbnail_width, R.dimen.thumbnail_height);
    }
}
