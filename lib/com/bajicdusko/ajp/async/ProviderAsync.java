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



/**
 * Created by Bajic on 12-Jul-14.
 */
public class ProviderAsync<T extends Model, Request extends Model> extends AsyncTask<Void, Void, Object> {

    Class<T> tClass = null;
    Request request = null;
    T responseObject = null;
    String url;
    FragmentActivity activity;

    public ProviderAsync(FragmentActivity activity, Class<T> tClass, String url)
    {
        this.tClass = tClass;
        this.url = url;
        this.activity = activity;
    }

    public ProviderAsync(FragmentActivity activity, Class<T> tClass, String url, Request request)
    {
        this.tClass = tClass;
        this.request = request;
        this.url = url;
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Void... params) {
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
            T m = (T)response;
            SetResponseObject(m);
        }
    }

    private Object Run(Class<T> mClass, String url)
    {
        try {
            Model result = null;
            if(request == null)
                result = Provider.getModel(mClass, activity, url);
            else
                result = Provider.getModel(mClass, activity, url, request);
            return result;
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

    private void SetResponseObject(T m)
    {
        responseObject = m;
    }

    public T GetResponseModel()
    {
        return responseObject;
    }


    public final ProviderAsync emptyExecute()
    {
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
        return this;
    }
}
