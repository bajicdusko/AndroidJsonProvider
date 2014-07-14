package com.bajicdusko.ajp.async;

import com.bajicdusko.ajp.json.Model;

import java.util.ArrayList;

/**
 * Created by Bajic on 14-Jul-14.
 */
public interface OnArrayDataLoaded<T extends Model> {

    public void OnModelArrayLoaded(ArrayList<T> responseModelArray);
}
