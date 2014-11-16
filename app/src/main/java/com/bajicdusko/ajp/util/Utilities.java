package com.bajicdusko.ajp.util;

import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.bajicdusko.ajp.exceptions.NotConnectedException;
import com.bajicdusko.ajp.fragment.MessageDialogFragment;
import com.bajicdusko.ajp.iajp.OnDialogClickListener;

public class Utilities {


    static ConcurrentHashMap<String, Bitmap> imageDictionary = new ConcurrentHashMap<String, Bitmap>();

    public static final int MESSAGE_FRAGMENT_KEY = 6854168;
    public static String SESSION_ID = "";

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


    public static void ShowMessage(FragmentActivity activity, String title, String message, int key)
    {
        if(activity != null) {
            MessageDialogFragment msg = new MessageDialogFragment();
            try {
                OnDialogClickListener listener = (OnDialogClickListener) activity;
                msg.setDialogListener(listener);
            } catch (Exception ex) {
                Log.e("MessageFragment", activity.getClass().getName() + " should implement OnDialogClickListener");
            }

            msg.showNegativeButton(true);
            msg.setKey(key);
            Bundle data = new Bundle();
            data.putString("message", message);
            data.putString("title", title);
            msg.setArguments(data);
            msg.show(activity.getSupportFragmentManager(), "home");
        }
    }

    public static void ShowMessage(FragmentActivity activity, String title, String message, String positiveButtonText, int key)
    {
        if(activity != null) {
            MessageDialogFragment msg = new MessageDialogFragment();
            try {
                OnDialogClickListener listener = (OnDialogClickListener) activity;
                msg.setDialogListener(listener);
            } catch (Exception ex) {
                Log.e("MessageFragment", activity.getClass().getName() + " should implement OnDialogClickListener");
            }

            msg.showNegativeButton(true);
            msg.setKey(key);
            Bundle data = new Bundle();
            data.putString("message", message);
            data.putString("title", title);
            data.putString("positiveButtonText", positiveButtonText);
            msg.setArguments(data);
            msg.show(activity.getSupportFragmentManager(), "home");
        }
    }

    public static boolean IsWiFiConnection(Context context)
    {
        try
        {
            ConnectivityManager conman = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if(conman == null || conman.getActiveNetworkInfo() == null)
                throw new NotConnectedException();
            else
                return conman.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

        }
        catch(Exception ex){ return false; }
    }
}
