package ca.bcit.handipark;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
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
    TabLayout tabs;
    public static String title;
    public static String snippet;
    public static String LONG = "longitude";
    public static String LAT = "latitude";
    public static String longitude = LONG;
    public static String latitude = LAT;
    private static String TAG = "PlacesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        reload();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            findViewById(R.id.signup).setVisibility(View.VISIBLE);
            findViewById(R.id.signout).setVisibility(View.GONE);
        } else{
            findViewById(R.id.signup).setVisibility(View.GONE);
            findViewById(R.id.signout).setVisibility(View.VISIBLE);
        }

        Intent intent = getIntent();
        LONG = intent.getStringExtra(LONG);
        LAT = intent.getStringExtra(LAT);
        title = intent.getStringExtra(title);
        longitude = LONG;
        latitude = LAT;

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
                longitude = LONG = "" + Objects.requireNonNull(place.getLatLng()).longitude;
                latitude = LAT = "" + Objects.requireNonNull(place.getLatLng()).latitude;
                title = "" + Objects.requireNonNull(place.getAddress());

                reload();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void reload() {
        viewPager = findViewById(R.id.view_pager);
        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reload();
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

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, FirebaseUIActivity.class);
        this.startActivity ( intent );

        findViewById(R.id.signup).setVisibility(View.GONE);
        findViewById(R.id.signout).setVisibility(View.VISIBLE);
    }

    public void onClickSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Main2Activity.this, "Successfully Signed Out",
                Toast.LENGTH_LONG).show();

        findViewById(R.id.signup).setVisibility(View.VISIBLE);
        findViewById(R.id.signout).setVisibility(View.GONE);
        reload();
    }
}