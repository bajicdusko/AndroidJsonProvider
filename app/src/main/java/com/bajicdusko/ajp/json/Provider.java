package com.bajicdusko.ajp.json;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.bajicdusko.ajp.exceptions.*;
import com.bajicdusko.ajp.util.Utility;
import com.bajicdusko.ajp.json.annotations.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.Log;

public class Provider {

    static String EMPTY_STRING = "";
    public static String JsonContent = "";

    public static <T extends Model> ArrayList<T> getModelArray(Class<T> tClass, String jsonContent) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException
    {
        JSONArray jsonArray = new JSONArray(jsonContent);
        ArrayList<T> items = new ArrayList<T>();

        try
        {
            for (int i = 0; i < jsonArray.length(); i++) {
                T tInstance = tClass.newInstance();
                tInstance.setJsonObject(jsonArray.getJSONObject(i));
                tInstance = getModel(tInstance);
                items.add(tInstance);
            }
        }
        catch (Exception e) {}

        return items;
    }


    public static <T extends Model> ArrayList<T> getModelArray(Class<T> tClass, Context context, String url) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
        JSONArray jsonArray = getJsonArray(context, url);
        ArrayList<T> items = new ArrayList<T>();

        try
        {
            for (int i = 0; i < jsonArray.length(); i++) {
                T tInstance = tClass.newInstance();
                tInstance.setJsonObject(jsonArray.getJSONObject(i));
                tInstance = getModel(tInstance);
                items.add(tInstance);
            }
        }
        catch (Exception e) {}

        return items;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Model, E extends Model> ArrayList<T> getModelArray(Class<T> tClass, Context context, String url, E requestObject) throws UrlConnectionException, NotConnectedException, NetworkStatePermissionException, JSONException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
        JSONArray jsonArray = getJsonArray(context, url, requestObject);
        ArrayList<T> items = new ArrayList<T>();

        try
        {
            for (int i = 0; i < jsonArray.length(); i++) {
                T tInstance = tClass.newInstance();
                tInstance.setJsonObject(jsonArray.getJSONObject(i));
                tInstance = getModel(tInstance);
                items.add(tInstance);
            }
        }
        catch (Exception e) {}

        return items;
    }

    public static <T extends Model> T getModelForJson(Class<T> tClass, Context context, String json) throws JSONException, IllegalAccessException, InstantiationException
    {
        T responseObject = tClass.newInstance();
        responseObject.setJsonObject(new JSONObject(json));
        responseObject = getModel(responseObject);
        return responseObject;
    }

    public static <T extends Model> ArrayList<T> getModelArrayForJson(Class<T> tClass, Context context, String json) throws JSONException, IllegalAccessException, InstantiationException
    {
        JSONArray jsonArray = new JSONArray(json);
        ArrayList<T> items = new ArrayList<T>();

        try
        {
            for (int i = 0; i < jsonArray.length(); i++) {
                T tInstance = tClass.newInstance();
                tInstance.setJsonObject(jsonArray.getJSONObject(i));
                tInstance = getModel(tInstance);
                items.add(tInstance);
            }
        }
        catch (Exception e) {}

        return items;
    }

    public static <T extends Model> T getModel(Class<T> tClass, Context context, String url) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException, InstantiationException {
        T responseObject = tClass.newInstance();
        responseObject.setJsonObject(getJsonObject(context, url));
        responseObject = getModel(responseObject);
        return responseObject;
    }

    public static <T extends Model, E extends Model> T getModel(Class<T> tClass, Context context, String url, E requestObject) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException, InstantiationException {
        T responseObject = tClass.newInstance();
        responseObject.setJsonObject(getJsonObject(context, url, requestObject));
        responseObject = getModel(responseObject);
        return responseObject;
    }

    public static <T extends Model> T getModel(Class<T> tClass, Context context, String url, String json) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException, InstantiationException {
        T responseObject = tClass.newInstance();
        responseObject.setJsonObject(getJsonObject(context, url, json));
        responseObject = getModel(responseObject);
        return responseObject;
    }

    public static String getNscToken(Context context, String url) throws IOException, NetworkStatePermissionException, NotConnectedException, ResponseStatusException
    {
        DownloadJSON(context, url);
        return JsonContent;
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

    public static JSONObject getJsonObject(Context context, String url, String e) throws JSONException, UrlConnectionException, NotConnectedException, NetworkStatePermissionException, IllegalAccessException, IllegalArgumentException, ClientProtocolException, IOException, ResponseStatusException {
        return new JSONObject( DownloadJSON(context, url, e) );
    }


    /*
    * Method for HTTP GET request
    * */
    private synchronized static String DownloadJSON(Context context, String url) throws IOException, NetworkStatePermissionException, NotConnectedException, ResponseStatusException
    {

        IsConnected(context);

        String content = "";

        HttpGet getRequest = new HttpGet(url);
        if(Utility.SESSION_ID != null && Utility.SESSION_ID != "")
            getRequest.addHeader("Cookie", " SESSIONID=" + Utility.SESSION_ID);

        getRequest.setHeader("Content-Type", "application/json");

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(getRequest);

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

            content = responseStringBuilder.toString();
        }

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
        if(Utility.SESSION_ID != null && Utility.SESSION_ID != "")
            postRequest.addHeader("Cookie", " SESSIONID=" + Utility.SESSION_ID);

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

    private synchronized static String DownloadJSON(Context context, String url, String json) throws
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

        StringEntity loginEntity = new StringEntity(json);
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


    public static <T extends Model> T SendRequest(Class<T> tClass, String url) throws JSONException, IllegalArgumentException, IllegalAccessException, ClientProtocolException, IOException, MessageSendingException, InstantiationException
    {
        T t = tClass.newInstance();
        T t2 = tClass.newInstance();
        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("Content-Type", "application/json");

        JSONStringer requestJson = new JSONStringer();


        Field[] fields = tClass.getFields();

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

            t2 = CreateModelSubclass(tClass, response);
        }

        return t2;

    }

    public static <T extends Model> T UploadImage(Class<T> tClass, String url, Bitmap bitmap) throws MalformedURLException, IOException, InstantiationException, IllegalAccessException, JSONException {

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

        return CreateModelSubclass(tClass, value);
    }

    private static <T extends Model> T getModel(T t) throws InstantiationException, IllegalAccessException
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

                    if(field.getType() == float.class)
                        field.set(t,  t.getFloat(((JsonResponseParam)an).Name()));

                    if(field.getType() == double.class)
                        field.set(t,  t.getDouble(((JsonResponseParam)an).Name()));

                    if(field.getType().getSuperclass() != null && field.getType().getSuperclass().equals(Model.class))
                    {
                        Log.i("superclass", field.getType().getSuperclass().getName());
                        Log.i("param", ((JsonResponseParam)an).Name());
                        Model model = t.getModel(((JsonResponseParam)an).Name());

                        if(model != null)
                        {
                            Class<? extends Model> modelSubclass = field.getType().asSubclass(Model.class);
                            Model m = modelSubclass.newInstance();
                            m.setJsonObject(model.getJsonObject());
                            m = getModel(m);
                            field.set(t, m);
                        }
                    }

                    if(field.getType().isArray()){



                        ArrayList<Model> modelArray = t.getModelArray(((JsonResponseParam)an).Name());
                        ArrayList<Model> values = new ArrayList<Model>();

                        for (Model model : modelArray) {

                            Class<? extends Model> arrayType = field.getType().getComponentType().asSubclass(Model.class);
                            Model m = arrayType.newInstance();
                            m.setJsonObject(model.getJsonObject());
                            m = getModel(m);
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

    @SuppressWarnings("unchecked")
    private static <T extends Model> T CreateModelSubclass(Class<T> tClass, String json) throws InstantiationException, IllegalAccessException, JSONException
    {
        T t2 = tClass.newInstance();
        t2.setJsonObject(new JSONObject(json));
        t2 = getModel(t2);
        return t2;
    }


    private static void IsConnected(Context context) throws NetworkStatePermissionException, NotConnectedException{
        if(context != null) {
            try {
                ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                if (conman == null || conman.getActiveNetworkInfo() == null || !conman.getActiveNetworkInfo().isConnectedOrConnecting())
                    throw new NotConnectedException();
            } catch (SecurityException ex) {
                throw new NetworkStatePermissionException();
            }
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
