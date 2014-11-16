package com.bajicdusko.ajp.iajp;

import android.graphics.Bitmap;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Bajic on 25-Oct-14.
 */
public interface IImageLoader {

    public Bitmap SetDimensionsAndFetchImage(String url) throws IOException;
}
