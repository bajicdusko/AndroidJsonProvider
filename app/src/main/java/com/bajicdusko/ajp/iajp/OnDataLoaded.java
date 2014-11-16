package com.bajicdusko.ajp.iajp;

import com.bajicdusko.ajp.json.Model;

/**
 * Created by Bajic on 13-Jul-14.
 */
public interface OnDataLoaded<T extends Model> {

    public void OnModelLoaded(T responseModel);
    public void OnErrorOccured(String message);
}
