package com.bajicdusko.ajpexample.datamodel;

import com.bajicdusko.ajp.json.Model;
import com.bajicdusko.ajp.json.annotations.JsonResponseParam;

/**
 * Created by Bajic on 16-Nov-14.
 */
public class SolarSystem extends Model {

    @JsonResponseParam(Name = "system_name")
    public String Name;

    @JsonResponseParam(Name = "galaxy_description")
    public String Galaxy;

    @JsonResponseParam(Name = "planets")
    public Planet[] Planets;
}
