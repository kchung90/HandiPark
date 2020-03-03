package ca.bcit.handipark;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import ca.bcit.handipark.ui.main.SectionsPagerAdapter;

public class Main2Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView list;
    SearchView editsearch;
    ArrayAdapter<String> adapter;
    String[] animalNameList;
    ArrayList<String> arraylist = new ArrayList<>();
    ViewPager viewPager;
    public static final String LONG = "longitude";
    public static final String LAT = "latitude";
    private static final String TAG = "PlacesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main2);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        int[] tabIcons = {
                R.drawable.search,
                R.drawable.favorite,
                R.drawable.history
        };
        for(int i=0; i<tabs.getTabCount(); i++){
            if(tabs.getTabAt(i) != null){
                Objects.requireNonNull(tabs.getTabAt(i)).setIcon(tabIcons[i]);
            }
        }

        Intent intent = getIntent();
        String longitude = intent.getStringExtra(LONG);
        String latitude = intent.getStringExtra(LAT);

        String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                longitude, latitude
            );
            Toast.makeText(Main2Activity.this, message, Toast.LENGTH_LONG).show();

        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        Objects.requireNonNull(autocompleteFragment.getView()).setElevation(10);
        Objects.requireNonNull(autocompleteFragment.getView()).setBackgroundColor(Color.WHITE);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng() + ", " + place.getAddress());
                String message = String.format(
                        "New Location \n Longitude: %1$s \n Latitude: %2$s",
                        Objects.requireNonNull(place.getLatLng()).longitude, place.getLatLng().latitude
                );
                Toast.makeText(Main2Activity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

//        animalNameList = new String[]{"Lion", "Tiger", "Dog",
//                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
//                "Cow","Donkey","Monkey"};
//
//        list = (ListView) findViewById(R.id.listview);
//        list.setVisibility(View.INVISIBLE);
//
//        Collections.addAll(arraylist, animalNameList);
//
//        adapter = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_selectable_list_item, arraylist);
//
//        list.setAdapter(adapter);
//
//        editsearch = (SearchView) findViewById(R.id.search);
//        editsearch.setOnQueryTextListener(Main2Activity.this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() < 1){
            viewPager.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            viewPager.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
            adapter.getFilter().filter(query);
        }
        editsearch.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() < 1){
            viewPager.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            viewPager.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
            adapter.getFilter().filter(newText);
        }
        return false;
    }
}