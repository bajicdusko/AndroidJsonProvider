package com.bajicdusko.ajp.util;

import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;

public class Utilities {

	public static ConcurrentHashMap<String, Bitmap> imageDictionary = new ConcurrentHashMap<String, Bitmap>();

    public static final int MESSAGE_FRAGMENT_KEY = 6854168;

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

    public static int GetDensityImageDimenssion(Context context, int dimm){
        float density = context.getResources().getDisplayMetrics().density;

        if(density >= 2)
            return dimm;
        else {
            int quarter = (int) Math.floor(dimm / 4);
            return quarter * 3;
        }
    }
}
