package com.bajicdusko.ajp.cache;

/**
 * Created by Bajic Dusko (www.bajicdusko.com) on 19-Sep-15.
 */
public enum CacheModeEnum {
    OnLineContentFirst(1),
    CacheContentFirst(2);

    public int Value;

    private CacheModeEnum(int v){
        Value = v;
    }
}
