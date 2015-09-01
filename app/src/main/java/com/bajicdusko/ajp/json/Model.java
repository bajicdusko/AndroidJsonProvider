package com.bajicdusko.ajp.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.text.Html;

import com.bajicdusko.ajp.json.annotations.*;

public class Model {
	protected JSONObject jsonObject = null;
    public String JSONString = "";
	
	@JsonResponseParam(Name="exception")
	public boolean Exception;
	
	@JsonResponseParam(Name="exceptionMessage")
	public String ExceptionMessage;

    @JsonResponseParam(Name="forceClose")
    public boolean ForceClose;
	
	public Model(){ }
	
	public Model(JSONObject item) {
	    setJsonObject(item);
    }

    public void setJsonObject(JSONObject item)
    {
        jsonObject = item;
        JSONString = item.toString();
    }

    public JSONObject getJsonObject(){
        return jsonObject;
    }


	public ArrayList<String> getObjectsFieldList() throws JSONException {
		ArrayList<String> names = new ArrayList<String>();
		if ( this.jsonObject.names() == null ) return names;
		for ( int i=0; i<this.jsonObject.names().length(); i++ ) {
			names.add( this.jsonObject.names().getString(i) );
		}
		return names;
	}

	public String getString(String fieldName) {
		try {
			if ( this.jsonObject.has(fieldName) && this.jsonObject.get(fieldName) instanceof String ) {
				return Html.fromHtml( this.jsonObject.getString(fieldName) ).toString();
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	public boolean getBool(String fieldName){
		try
		{
			return this.jsonObject.getBoolean(fieldName);
		}
		catch(Exception ex){
			return false;
		}		
	}

	@SuppressLint("SimpleDateFormat")
	public Date getDate(String fieldName) {
		try {
			String fieldValue= this.getString(fieldName);
			if (!fieldValue.trim().equals("")) return new SimpleDateFormat("MM/dd/yyyy").parse(fieldValue);
			else return new Date();
		} catch (Exception e) {
			return new Date();
		}
	}

    public float getFloat(String floatValue)
    {
        try
        {
            return (float)this.jsonObject.getDouble(floatValue);
        }
        catch(Exception ex){
            return 0;
        }
    }

    public double getDouble(String doubleValue)
    {
        try
        {
            return this.jsonObject.getDouble(doubleValue);
        }
        catch(Exception ex){
            return 0;
        }
    }
	
	public int getInt(String fieldName) {		
		try {
			return this.jsonObject.getInt(fieldName);
		} catch (JSONException e) {
			return -1;
		}		
	}

	public ArrayList<Model> getModelArray(String fieldName) {
		ArrayList<Model> items = new ArrayList<Model>();

		try {
			JSONArray list = (JSONArray) this.jsonObject.getJSONArray(fieldName);
			for ( int i = 0 ; i < list.length() ; i++ ) {
				items.add(new Model(list.getJSONObject(i)));
			}
		} catch (Exception e) {
			// Samo ce vratiti praznu listu
		}

		return items;
	}

	public Model getModel(String fieldName) {
		Model item = null;

		try {
			item = new Model(this.jsonObject.getJSONObject(fieldName));
		} catch (Exception e) { }

		return item;
	}
	

	
	
	
	
}
