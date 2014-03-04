package com.bajicdusko.ajp.json;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;

import com.bajicdusko.ajp.exceptions.*;

public class Provider {
	
	static String EMPTY_STRING = "";
    public static String JsonContent = "";


    public static <T extends Model> ArrayList<T> getModelArray(T responseObject, String jsonContent) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException
    {
        JSONArray jsonArray = new JSONArray(jsonContent);
        ArrayList<T> items = new ArrayList<T>();

        try
        {
            for (int i = 0; i < jsonArray.length(); i++) {
                T tInstance = (T) responseObject.getClass().newInstance();
                tInstance.jsonObject = jsonArray.getJSONObject(i);
                tInstance.getModel(tInstance);
                items.add(tInstance);
            }
        }
        catch (Exception e) { /* Samo ce vratiti praznu listu */ }

        return items;
    }


    public static <T extends Model> ArrayList<T> getModelArray(T responseObject, Context context, String url) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
        JSONArray jsonArray = getJsonArray(context, url);
        ArrayList<T> items = new ArrayList<T>();

        try
        {
            for (int i = 0; i < jsonArray.length(); i++) {
                T tInstance = (T) responseObject.getClass().newInstance();
                tInstance.jsonObject = jsonArray.getJSONObject(i);
                tInstance.getModel(tInstance);
                items.add(tInstance);
            }
        }
        catch (Exception e) { /* Samo ce vratiti praznu listu */ }

        return items;
    }

	@SuppressWarnings("unchecked")
	public static <T extends Model> ArrayList<T> getModelArray(T responseObject, Context context, String url, T requestObject) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
		JSONArray jsonArray = getJsonArray(context, url, requestObject);
		ArrayList<T> items = new ArrayList<T>();

		try 
		{
			for (int i = 0; i < jsonArray.length(); i++) {	
				T tInstance = (T) responseObject.getClass().newInstance(); 
				tInstance.jsonObject = jsonArray.getJSONObject(i);
				tInstance.getModel(tInstance);
				items.add(responseObject);
			}
		} 
		catch (Exception e) { /* Samo ce vratiti praznu listu */ }

		return items;
	}

    public static <T extends Model> T getModel(T responseObject, Context context, String url) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
        responseObject.jsonObject = getJsonObject(context, url);
        responseObject.getModel(responseObject);
        return responseObject;
    }

	public static <T extends Model, E extends Model> T getModel(T responseObject, Context context, String url, E requestObject) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
		responseObject.jsonObject = getJsonObject(context, url, requestObject);
		responseObject.getModel(responseObject);
		return responseObject;
	}

    private static JSONArray getJsonArray(Context context, String url) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
        String content = DownloadJSON(context, url);
        return new JSONArray(content);
    }

	private static <E extends Model> JSONArray getJsonArray(Context context, String url, E e) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
		String content = DownloadJSON(context, url, e);
		return new JSONArray(content);			
	}


    public static JSONObject getJsonObject(Context context, String url) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
        return new JSONObject( DownloadJSON(context, url ) );
    }

	public static <E extends Model> JSONObject getJsonObject(Context context, String url, E e) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
		return new JSONObject( DownloadJSON(context, url, e) );
	}	


    /*
    * Method for HTTP GET request
    * */
    private synchronized static String DownloadJSON(Context context, String url) throws IOException, NetworkStatePermissionException, NotConnectedException
    {
        IsConnected(context);
        String content = "";

        URL link = new URL(url);
        URLConnection connection = link.openConnection();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        int length;
        InputStream is = connection.getInputStream();
        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        content = os.toString();
        os.close();
        is.close();

        JsonContent = content;
        return content;
    }


    /*
        Method for HTTP POST request
     */
	private synchronized static <E extends Model> String DownloadJSON(Context context, String url, E e) throws
		UrlConnectionException, 
		NotConnectedException, 
		NetworkStatePermissionException, 
		JSONException, 
		IllegalAccessException, 
		IllegalArgumentException, 
		ClientProtocolException, 
		IOException, 
		ResponseStatusException
		{

			IsConnected(context);

			HttpPost postRequest = new HttpPost(url);
			postRequest.setHeader("Content-Type", "application/json");

			JSONStringer requestJson = new JSONStringer();

			Field[] fields = e.getClass().getFields();

			JSONStringer obj = requestJson.object();
			for (Field field : fields) {

				Annotation a = field.getAnnotation(JsonRequestParam.class);

				if(a != null)
				{
					obj.key(((JsonRequestParam)a).Name()).value(field.get(e).toString());
				}
			}
			requestJson.endObject();


			StringEntity loginEntity = new StringEntity(requestJson.toString());
			postRequest.setEntity(loginEntity);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(postRequest);

			if(httpResponse.getStatusLine().getStatusCode() != 200)
				throw new ResponseStatusException(httpResponse.getStatusLine().getStatusCode());

			HttpEntity responseEntity = httpResponse.getEntity();

			String response = EMPTY_STRING;
			if(responseEntity != null)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));

				String temp = "";
				StringBuilder responseStringBuilder = new StringBuilder();

				while((temp = br.readLine()) != null)
					responseStringBuilder.append(temp);

				response = responseStringBuilder.toString();
			}

            JsonContent = response;
			return response;
		}
	
	public static <T extends Model> T SendRequest(T t, String url) throws JSONException, IllegalArgumentException, IllegalAccessException, ClientProtocolException, IOException, MessageSendingException, InstantiationException
	{	
		Model t2 = new Model();
		HttpPost postRequest = new HttpPost(url);		
		postRequest.setHeader("Content-Type", "application/json");

		JSONStringer requestJson = new JSONStringer();
			
		
		Field[] fields = t.getClass().getFields();
		
		JSONStringer obj = requestJson.object();		
		for (Field field : fields) {
			
			Annotation a = field.getAnnotation(JsonRequestParam.class);
			
			if(a != null)
			{
				obj.key(((JsonRequestParam)a).Name()).value(field.get(t).toString());
			}
		}		
		requestJson.endObject();
		

		StringEntity loginEntity = new StringEntity(requestJson.toString());
		postRequest.setEntity(loginEntity);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(postRequest);
		
		if(httpResponse.getStatusLine().getStatusCode() != 200)
			throw new MessageSendingException();
		
		HttpEntity responseEntity = httpResponse.getEntity();
		
		if(responseEntity != null)
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
			
			String temp = "";			
			StringBuilder responseStringBuilder = new StringBuilder();
			
			while((temp = br.readLine()) != null)
			{
				responseStringBuilder.append(temp);
			}
			
			String response = responseStringBuilder.toString();
			
			t2 = CreateModelSubclass(t, response);
		}
			
		return (T) t2;
		
	}
	
	public static <T extends Model> T UploadImage(T t, String url, Bitmap bitmap) throws MalformedURLException, IOException, InstantiationException, IllegalAccessException, JSONException {

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		
		conn.setRequestProperty("Content-Type", "multipart/form=data");
		conn.setRequestMethod("POST");
		
		conn.connect();
		OutputStream out = conn.getOutputStream();
		
		
		//FileInputStream fis = new FileInputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
		
//		byte[] buf = new byte[1024];
//		int len;
//		while ((len = fis.read(buf)) > 0) {
//			out.write(buf, 0, len);
//		}
//		fis.close();		
		
		out.flush();
		out.close();
		
		InputStream is=conn.getInputStream();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] b = new byte[4096];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		
		String value = os.toString();			

		os.close();
		is.close();	
		
		return CreateModelSubclass(t, value);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Model> T CreateModelSubclass(T t, String json) throws InstantiationException, IllegalAccessException, JSONException
	{
		Model t2 = new Model();
		Class<? extends Model> type = t.getClass().asSubclass(Model.class);
		t2 = type.newInstance();
		t2.jsonObject = new JSONObject(json);
		t2.getModel(t2);
		return (T) t2;
	}
	
	
	private static void IsConnected(Context context) throws NetworkStatePermissionException, NotConnectedException{
		try
		{
			ConnectivityManager conman = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if(conman == null || conman.getActiveNetworkInfo() == null || !conman.getActiveNetworkInfo().isConnectedOrConnecting())
				throw new NotConnectedException();
		}
		catch(SecurityException ex){
			throw new NetworkStatePermissionException();
		}
	}
	
	public static boolean IsWiFiConnection(Context context)
	{
		try
		{
			ConnectivityManager conman = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if(conman == null || conman.getActiveNetworkInfo() == null)
				throw new NotConnectedException();
			else
				return conman.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
				
		}
		catch(Exception ex){ return false; }
	}
}
