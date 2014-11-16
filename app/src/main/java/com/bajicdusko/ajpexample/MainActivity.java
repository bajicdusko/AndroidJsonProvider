package com.bajicdusko.ajpexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bajicdusko.R;
import com.bajicdusko.ajp.iajp.OnDataLoaded;
import com.bajicdusko.ajp.json.AsyncJsonProvider;
import com.bajicdusko.ajp.json.Model;
import com.bajicdusko.ajpexample.datamodel.Planet;
import com.bajicdusko.ajpexample.datamodel.SolarSystem;

public class MainActivity extends ActionBarActivity {

    ListView planetListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        planetListView = (ListView)findViewById(R.id.planetListView);

        new AsyncJsonProvider<SolarSystem, Model>(this, SolarSystem.class, "http://bajicdusko.com/ajp/json/solarsystem.txt").shortExecute(new OnDataLoaded<SolarSystem>() {
            @Override
            public void OnModelLoaded(SolarSystem responseModel) {
                getSupportActionBar().setTitle(responseModel.Galaxy + " - " + responseModel.Name);

                String[] planetNames = new String[responseModel.Planets.length];
                for(int i = 0; i < responseModel.Planets.length; i++) {
                    Planet planet = responseModel.Planets[i];
                    planetNames[i] = planet.Name;
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, planetNames);
                planetListView.setAdapter(adapter);
            }

            @Override
            public void OnErrorOccured(String message) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
