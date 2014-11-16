package com.bajicdusko.ajp.json;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.bajicdusko.R;
import com.bajicdusko.ajp.json.enums.ResponseType;
import com.bajicdusko.ajp.cache.CacheManager;
import com.bajicdusko.ajp.exceptions.NetworkStatePermissionException;
import com.bajicdusko.ajp.exceptions.NotConnectedException;
import com.bajicdusko.ajp.exceptions.ResponseStatusException;
import com.bajicdusko.ajp.exceptions.UrlConnectionException;
import com.bajicdusko.ajp.iajp.OnArrayDataLoaded;
import com.bajicdusko.ajp.iajp.OnDataLoaded;
import com.bajicdusko.ajp.util.Utilities;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bajic on 12-Jul-14.
 */
public class AsyncJsonProvider<T extends Model, Request extends Model> extends AsyncTask<Object, Void, Object> {

    Class<T> tClass = null;
    Request request = null;

    String url;
    FragmentActivity activity;
    Context context;
    OnDataLoaded<T> onDataLoaded;
    OnArrayDataLoaded<T> onArrayDataLoaded;

    ResponseType responseType;

    boolean saveToCache = false;

    public AsyncJsonProvider(FragmentActivity activity, Class<T> tClass, String url)
    {
        this.tClass = tClass;
        this.url = url;
        this.activity = activity;
        this.context = activity;
        try {
            onDataLoaded = (OnDataLoaded) activity;
        }
        catch(Exception ex)
        {

        }
    }

    public AsyncJsonProvider(Context context, Class<T> tClass, String url)
    {
        this.tClass = tClass;
        this.url = url;
        this.context = context;
        try {
            onDataLoaded = (OnDataLoaded) activity;
        }
        catch(Exception ex)
        {

        }
    }

    public AsyncJsonProvider(FragmentActivity activity, Class<T> tClass, String url, boolean saveToCache)
    {
        this.tClass = tClass;
        this.url = url;
        this.activity = activity;
        this.context = activity;
        this.saveToCache = saveToCache;
        try {
            onDataLoaded = (OnDataLoaded) activity;
        }
        catch(Exception ex)
        {

        }
    }

    public AsyncJsonProvider(Context context, Class<T> tClass, String url, boolean saveToCache)
    {
        this.tClass = tClass;
        this.url = url;
        this.context = context;
        this.saveToCache = saveToCache;
        try {
            onDataLoaded = (OnDataLoaded) activity;
        }
        catch(Exception ex)
        {

        }
    }


    public AsyncJsonProvider(FragmentActivity activity, Class<T> tClass, String url, Request request, int key)
    {
        this.tClass = tClass;
        this.request = request;
        this.url = url;
        this.activity = activity;
        this.context = activity;
        try {
            onDataLoaded = (OnDataLoaded) activity;
        }
        catch(Exception ex)
        {

        }
    }

    public AsyncJsonProvider(Context context, Class<T> tClass, String url, Request request, int key)
    {
        this.tClass = tClass;
        this.request = request;
        this.url = url;
        this.context = context;
        try {
            onDataLoaded = (OnDataLoaded) activity;
        }
        catch(Exception ex)
        {

        }
    }

    public final AsyncJsonProvider shortExecute(OnDataLoaded<T> onDataLoadedListener)
    {
        onDataLoaded = onDataLoadedListener;
        responseType = ResponseType.SingleModel;
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        return this;
    }

    public final AsyncJsonProvider shortArrayExecute(OnArrayDataLoaded<T> onArrayDataLoadedListener)
    {
        onArrayDataLoaded = onArrayDataLoadedListener;
        responseType = ResponseType.ArrayModel;
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        return this;
    }

    @Override
    protected Object doInBackground(Object... params) {
        return Run(tClass, url);
    }

    private Object Run(Class<T> mClass, String url)
    {
        try {

            if(responseType == ResponseType.SingleModel) {

                Model result = null;
                if (request == null)
                    result = Provider.getModel(mClass, context, url);
                else
                    result = Provider.getModel(mClass, context, url, request);
                return result;
            }
            else{
                ArrayList<T> result = null;
                if (request == null)
                    result = Provider.getModelArray(mClass, context, url);
                else
                    result = Provider.getModelArray(mClass, context, url, request);
                return result;
            }

        } catch (JSONException e) {
            return e;
        } catch (UrlConnectionException e) {
            return  e;
        } catch (NotConnectedException e) {
            return  e;
        } catch (NetworkStatePermissionException e) {
            return  e;
        } catch (IllegalAccessException e) {
            return  e;
        } catch (IOException e) {
            return  e;
        } catch (ResponseStatusException e) {
            return  e;
        } catch (InstantiationException e) {
            return  e;
        }
    }

    @Override
    protected void onPostExecute(Object response) {

        CacheManager<T> cacheManager = new CacheManager(context, url);

        if(response instanceof NotConnectedException)
        {
            try
            {
                response = cacheManager.GetContent(tClass);
                RiseLoadEvents(cacheManager, response, false);
            }
            catch (Exception ex)
            {
                if(activity != null)
                    Utilities.ShowMessage(activity, "", activity.getString(R.string.no_connection), Utilities.MESSAGE_FRAGMENT_KEY);
                else
                    RiseErrorEvents(context.getString(R.string.no_connection));
            }
        }
        else if(response instanceof Exception) {
            if(activity != null)
                Utilities.ShowMessage(activity, "", response.getClass().getName(), Utilities.MESSAGE_FRAGMENT_KEY);
            else
                RiseErrorEvents(response.getClass().getName());
        }
        else
            RiseLoadEvents(cacheManager, response);
    }

    private void RiseLoadEvents(CacheManager cacheManager, Object response, boolean saveToCache)
    {
        this.saveToCache = saveToCache;
        RiseLoadEvents(cacheManager, response);
    }

    private void RiseLoadEvents(CacheManager cacheManager, Object response)
    {
        if(responseType == ResponseType.SingleModel) {
            if(response != null) {
                T m = (T) response;
                if (onDataLoaded != null) {

                    try {
                        if (saveToCache)
                            cacheManager.WriteContentObject(m);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    onDataLoaded.OnModelLoaded(m);
                }
            }
        }
        else{

            if(response != null) {
                ArrayList<T> m = (ArrayList<T>) response;
                if (onArrayDataLoaded != null) {
                    try {
                        if (saveToCache)
                            cacheManager.WriteContentArray(m);
                    } catch (Exception ex) {
                    }

                    onArrayDataLoaded.OnModelArrayLoaded(m);
                }
            }
        }
    }

    private void RiseErrorEvents(String errorMessage)
    {
        if(responseType == ResponseType.SingleModel) {
            if (onDataLoaded != null) {
                onDataLoaded.OnErrorOccured(errorMessage);
            }
        }
        else {
            if (onArrayDataLoaded != null) {
                onArrayDataLoaded.OnErrorOccured(errorMessage);
            }
        }
    }
}
