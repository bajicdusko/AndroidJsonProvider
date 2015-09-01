package com.bajicdusko.ajp.tools;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bajicdusko.R;
import com.bajicdusko.ajp.util.Utilities;

public class FetchSmallImageAsync extends FetchImageAsync {

	public FetchSmallImageAsync(Activity activity, String url, ImageView here) {
		super(activity, url, here);
	}

    @Override
    protected int GetDefaultImageResource() {
        return R.drawable.ic_launcher;
    }

    @Override
    public Bitmap SetDimensionsAndFetchImage(String url) throws IOException {
        int imageDimm = Utilities.GetDensityImageDimenssion(context, 200);
        return FetchImage(url, imageDimm, imageDimm);
    }
}
