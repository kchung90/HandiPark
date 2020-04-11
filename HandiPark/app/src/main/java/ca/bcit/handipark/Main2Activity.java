package ca.bcit.handipark;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Objects;


public class Main2Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView list;
    SearchView editsearch;
    ArrayAdapter<String> adapter;
    ViewPager viewPager;
    public static String title;
    public static String snippet;
    public static String longitude;
    public static String latitude;
    public static String LONG = "longitude";
    public static String LAT = "latitude";
    private static String TAG = "PlacesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        viewPager = findViewById(R.id.view_pager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//        System.out.println(myPagerAdapter.getItem(2).getView());

        int[] tabIcons = {
                R.drawable.search,
                R.drawable.favorite,
                R.drawable.maps
        };
        for(int i=0; i<tabs.getTabCount(); i++){
            if(tabs.getTabAt(i) != null){
                Objects.requireNonNull(tabs.getTabAt(i)).setIcon(tabIcons[i]);
            }
        }

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        Intent intent = getIntent();
        LONG = intent.getStringExtra(LONG);
        LAT = intent.getStringExtra(LAT);

        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

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
        if (newText.length() < 1) {
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