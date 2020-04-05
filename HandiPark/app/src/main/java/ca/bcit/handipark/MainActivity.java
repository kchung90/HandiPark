package ca.bcit.handipark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final String TOAST = "toast";
    private static final String TAG = "PlacesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);

                intent.putExtra(Main2Activity.LONG, "" + Objects.requireNonNull(place.getLatLng()).longitude);
                intent.putExtra(Main2Activity.LAT, "" + place.getLatLng().latitude);
                startActivity(intent);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        Intent i = getIntent();
        String toast = i.getStringExtra(TOAST);
        if (toast != null) {
            Toast.makeText(MainActivity.this,
                    toast, Toast.LENGTH_LONG).show();
        }
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        this.startActivity ( intent );
    }

    public void onClickGetLocation(View view) {
        LocationManager mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocListener = new MyLocationListener();
        assert mLocManager != null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LOCATION_PERMS, 1340);
            return;
        }

        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 100, mLocListener);
    }

    public class MyLocationListener implements LocationListener{

        public void onLocationChanged(Location loc) {
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);

            intent.putExtra(Main2Activity.LONG, "" + loc.getLongitude());
            intent.putExtra(Main2Activity.LAT, "" + loc.getLatitude());
            startActivity(intent);
        }
        public void onProviderDisabled(String arg0) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}
