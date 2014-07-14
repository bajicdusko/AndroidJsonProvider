package com.bajicdusko.ajp.async;

import com.bajicdusko.ajp.json.Model;

/**
 * Created by Bajic on 13-Jul-14.
 */
public interface OnDataLoaded<T extends Model> {

    public void OnModelLoaded(T responseModel);
}
