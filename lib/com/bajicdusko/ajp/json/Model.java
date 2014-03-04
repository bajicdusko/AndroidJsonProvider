package com.bajicdusko.ajp.json;


import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.text.Html;

public class Model {
	public JSONObject jsonObject = null;
	
	@JsonResponseParam(Name="exception")
	public boolean Exception;
	
	@JsonResponseParam(Name="exceptionMessage")
	public String ExceptionMessage;
	
	public Model(){ }
	
	public Model(JSONObject item) {
	    this.jsonObject = item;
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
	
	protected <T extends Model> T getModel(T t)
	{
		Field[] fields = t.getClass().getFields();
		
		for (Field field : fields) {
			Annotation an = field.getAnnotation(JsonResponseParam.class);
						
			if(an != null){
				try 
				{
				
					if(field.getType() == boolean.class)				
						field.setBoolean(t, t.getBool(((JsonResponseParam)an).Name()));
					
					if(field.getType() == String.class)
						field.set(t,  t.getString(((JsonResponseParam)an).Name()));
				
					if(field.getType() == int.class)
						field.setInt(t,  t.getInt(((JsonResponseParam)an).Name()));
					
					if(field.getType() == Date.class)
						field.set(t,  t.getDate(((JsonResponseParam)an).Name()));
					
					if(field.getType().isArray()){
						
						
						
						ArrayList<Model> modelArray = t.getModelArray(((JsonResponseParam)an).Name());
						ArrayList<Model> values = new ArrayList<Model>();
						
						for (Model model : modelArray) {
							
							Class<? extends Model> arrayType = field.getType().getComponentType().asSubclass(Model.class);
							Model m = arrayType.newInstance();
							m.jsonObject = model.jsonObject;
							model.getModel(m);
							values.add(m);						
						}
												
						field.set(t, values.toArray((T[]) Array.newInstance(field.getType().getComponentType().asSubclass(Model.class), values.size())));
					}
				
				} catch (IllegalArgumentException e) { 
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}

		return t;
	}
	
	
	
	
}
