package com.bajicdusko.ajpexample;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bajicdusko.ajp.R;
import com.bajicdusko.ajp.async.ProviderAsync;
import com.bajicdusko.ajp.exceptions.NetworkStatePermissionException;
import com.bajicdusko.ajp.exceptions.NotConnectedException;
import com.bajicdusko.ajp.exceptions.ResponseStatusException;
import com.bajicdusko.ajp.exceptions.UrlConnectionException;
import com.bajicdusko.ajp.json.Provider;
import com.bajicdusko.ajpexample.datamodel.Planet;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        TextView waiting;
        TextView planet;
        TextView sat1;
        TextView sat2;
        TextView sat3;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            waiting = (TextView)rootView.findViewById(R.id.waitingTextView);
            planet = (TextView)rootView.findViewById(R.id.planetTextView);
            sat1 = (TextView)rootView.findViewById(R.id.satelliteOneTextView);
            sat2 = (TextView)rootView.findViewById(R.id.satelliteTwoTextView);
            sat3 = (TextView)rootView.findViewById(R.id.satelliteThreeTextView);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            new LoadData().emptyExecute();
        }

        class LoadData extends ProviderAsync
        {
            public LoadData()
            {
                super(getActivity(), Planet.class, "http://json.txt");
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                Planet planet = (Planet)GetResponseModel();
                if(planet != null)
                {
                    //something
                }
            }
        }
    }

}
