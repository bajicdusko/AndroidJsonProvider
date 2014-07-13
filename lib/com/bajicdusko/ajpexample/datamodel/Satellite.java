package com.bajicdusko.ajpexample.datamodel;

import com.bajicdusko.ajp.json.JsonResponseParam;
import com.bajicdusko.ajp.json.Model;

public class Satellite extends Model {

    @JsonResponseParam(Name = "satelliteName")
    public String Name;
}
