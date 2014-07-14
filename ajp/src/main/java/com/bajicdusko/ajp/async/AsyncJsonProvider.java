package com.bajicdusko.ajp.async;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.bajicdusko.ajp.R;
import com.bajicdusko.ajp.exceptions.NetworkStatePermissionException;
import com.bajicdusko.ajp.exceptions.NotConnectedException;
import com.bajicdusko.ajp.exceptions.ResponseStatusException;
import com.bajicdusko.ajp.exceptions.UrlConnectionException;
import com.bajicdusko.ajp.json.Model;
import com.bajicdusko.ajp.json.Provider;
import com.bajicdusko.ajp.util.Utility;

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
    OnDataLoaded<T> onDataLoaded;
    OnArrayDataLoaded<T> onArrayDataLoaded;

    ResponseType responseType;


    public AsyncJsonProvider(FragmentActivity activity, Class<T> tClass, String url)
    {
        this.tClass = tClass;
        this.url = url;
        this.activity = activity;
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
        try {
            onDataLoaded = (OnDataLoaded) activity;
        }
        catch(Exception ex)
        {

        }
    }

    @Override
    protected Object doInBackground(Object... params) {
        return Run(tClass, url);
    }

    @Override
    protected void onPostExecute(Object response) {
        if(response instanceof NotConnectedException)
            Utility.ShowMessage(activity, "", activity.getString(R.string.no_connection));
        else if(response instanceof Exception)
            Utility.ShowMessage(activity, "", response.getClass().getName());
        else
        {

            if(responseType == ResponseType.SingleModel) {
                T m = (T) response;
                if (onDataLoaded != null)
                    onDataLoaded.OnModelLoaded(m);
            }
            else{
                ArrayList<T> m = (ArrayList<T>) response;
                if (onArrayDataLoaded != null)
                    onArrayDataLoaded.OnModelArrayLoaded(m);
            }
        }
    }

    private Object Run(Class<T> mClass, String url)
    {
        try {

            if(responseType == ResponseType.SingleModel) {

                Model result = null;
                if (request == null)
                    result = Provider.getModel(mClass, activity, url);
                else
                    result = Provider.getModel(mClass, activity, url, request);
                return result;
            }
            else{
                ArrayList<T> result = null;
                if (request == null)
                    result = Provider.getModelArray(mClass, activity, url);
                else
                    result = Provider.getModelArray(mClass, activity, url, request);
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


}
