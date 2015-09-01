package com.bajicdusko.ajp.util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.bajicdusko.ajp.fragment.MessageDialogFragment;
import com.bajicdusko.ajp.iajp.OnDialogClickListener;

/**
 * Created by Bajic on 12-Jul-14.
 */
public class Utility {

    public static String SESSION_ID = "";

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
}
