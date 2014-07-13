package com.bajicdusko.ajpexample.datamodel;

import com.bajicdusko.ajp.json.JsonResponseParam;
import com.bajicdusko.ajp.json.Model;

/**
 * Created by Bajic on 3/4/14.
 */
public class Planet extends Model{

    /*
        If attribute have defined JsonResponseParam annotation
        That attribute will get value from fetched json, where JSONObject property have
        name defined in annotation.


        eg:
            json: {
                    "planetName": "Jupiter"
                  }

            @JsonResponseParam(Name="planetName") will set value "Jupiter"  to Name attribute in this class
     */
    @JsonResponseParam(Name="planetName")
    public String Name;

    /*
        Satellite class also have to extends Model class.
        Attributes in this class will get values from
        JSONArray named "satellites" in JSON file
     */
    @JsonResponseParam(Name = "satellites")
    public Satellite[] Satellites;
}
