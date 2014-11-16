package com.bajicdusko.ajpexample.datamodel;

import com.bajicdusko.ajp.json.Model;
import com.bajicdusko.ajp.json.annotations.JsonResponseParam;

/**
 * Created by Bajic on 16-Nov-14.
 */
public class Planet extends Model {

    @JsonResponseParam(Name = "name")
    public String Name;
}
