package com.bajicdusko.ajp.json.enums;

/**
 * Created by Bajic on 14-Jul-14.
 */
public enum ResponseType
{
    SingleModel(1),
    ArrayModel(2);

    public int Value;

    private ResponseType(int v){
        Value = v;
    }
}
