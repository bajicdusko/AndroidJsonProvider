package com.bajicdusko.ajp.util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.bajicdusko.ajp.fragment.MessageDialogFragment;
import com.bajicdusko.ajp.fragment.OnDialogClickListener;

/**
 * Created by Bajic on 12-Jul-14.
 */
public class Utility {

    public static String SESSION_ID = "";

    public static void ShowMessage(FragmentActivity activity, String title, String message)
    {
        MessageDialogFragment msg = new MessageDialogFragment();
        try {
            OnDialogClickListener listener = (OnDialogClickListener)activity;
            msg.setDialogListener(listener);
        }
        catch (Exception ex)
        {
            Log.e("MessageFragment", activity.getClass().getName() + " should implement OnDialogClickListener");
        }

        msg.showNegativeButton(true);
        msg.setKey(0);
        Bundle data = new Bundle();
        data.putString("message", message);
        data.putString("title", title);
        msg.setArguments(data);
        msg.show(activity.getSupportFragmentManager(), "home");
    }
}
