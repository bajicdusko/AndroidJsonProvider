package com.bajicdusko.ajpexample;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bajicdusko.ajp.R;
import com.bajicdusko.ajp.async.AsyncJsonProvider;
import com.bajicdusko.ajp.async.OnDataLoaded;
import com.bajicdusko.ajp.json.Model;
import com.bajicdusko.ajpexample.datamodel.Planet;

public class MainActivity extends FragmentActivity implements OnDataLoaded<Planet> {

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

        @SuppressWarnings("unchecked")
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            new AsyncJsonProvider(getActivity(), Planet.class, "http://json.txt").shortExecute((MainActivity) getActivity());

            //or

            new AsyncJsonProvider(getActivity(), Planet.class, "http://json.txt").shortExecute(new OnDataLoaded() {
                @Override
                public void OnModelLoaded(Model responseModel) {
                    Planet p = (Planet) responseModel;
                    if (p != null) {
                        //something
                    }
                }
            });

            //or
        }
    }

    @Override
    public void OnModelLoaded(Planet responseModel) {
        Planet p = (Planet) responseModel;
        if (p != null) {
            //something
        }
    }
}
