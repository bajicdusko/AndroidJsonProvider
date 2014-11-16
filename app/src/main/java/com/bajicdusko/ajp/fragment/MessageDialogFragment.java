package com.bajicdusko.ajp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bajicdusko.R;
import com.bajicdusko.ajp.iajp.OnDialogClickListener;

public class MessageDialogFragment extends DialogFragment implements OnClickListener {
	
	String Message;	
	String Title;
    String PositiveButtonText;
	boolean ShowNegativeButton = false;
	int KEY = 9999;
	Object[] args;
	
	com.bajicdusko.ajp.iajp.OnDialogClickListener OnDialogClickListener = null;
	Activity activity;
		
	public void setDialogListener(OnDialogClickListener listener)
	{	
		OnDialogClickListener = listener;
	}
	
	public void setKey(int key){
		KEY = key;	
	}
	
	public void showNegativeButton(boolean showButton)
	{
		ShowNegativeButton = showButton;
	}
	
	public void setArguments(Object... args){
		this.args = args;
	}
	
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
        {
            Message = savedInstanceState.getString("message");
            Title = savedInstanceState.getString("title");
            ShowNegativeButton = savedInstanceState.getBoolean("showInstanceState");
            KEY = savedInstanceState.getInt("key");
            PositiveButtonText = getArguments().getString("positiveButtonText");
        }
        else {
            Message = getArguments().getString("message");
            Title = getArguments().getString("title");
            PositiveButtonText = getArguments().getString("positiveButtonText");
        }
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("message", Message);
        outState.putString("title", Title);
        outState.putBoolean("showNegativeButton", ShowNegativeButton);
        outState.putInt("key", KEY);
        outState.putString("positiveButtonText", PositiveButtonText);

        super.onSaveInstanceState(outState);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);				
		return view;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String positiveButtonText = "OK";
		if(Title == null || Title.equals(""))
        {
            Title = getResources().getString(R.string.error_occured);
            positiveButtonText = getResources().getString(R.string.try_again);
        }

        if(PositiveButtonText != null && !PositiveButtonText.equals(""))
            positiveButtonText = PositiveButtonText;
		
        AlertDialog dialog =  new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.ic_dialog_alert_holo_light)
                .setTitle(Title)
                .setMessage(Message)
                .setPositiveButton(positiveButtonText, this)
                .setCancelable(false)
                .create();
        
        if(ShowNegativeButton)
        	dialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getResources().getString(R.string.cancel), this);
        
        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {    
    	if(OnDialogClickListener != null){
	        switch (which) {
		        case DialogInterface.BUTTON_POSITIVE:
                    this.dismiss();
		            OnDialogClickListener.OnPositiveClick(KEY, args);
		            break;
		        default:
                    this.dismiss();
		        	OnDialogClickListener.OnNegativeClick(KEY, args);
		        	break;
	        }
    	}
    	else
    		this.dismiss();
    }    
}
