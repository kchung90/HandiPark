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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String TAG = "PlacesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            findViewById(R.id.signup).setVisibility(View.VISIBLE);
            findViewById(R.id.signout).setVisibility(View.GONE);
        } else{
            findViewById(R.id.signup).setVisibility(View.GONE);
            findViewById(R.id.signout).setVisibility(View.VISIBLE);
        }

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
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);

                intent.putExtra(Main2Activity.LONG, "" + Objects.requireNonNull(place.getLatLng()).longitude);
                intent.putExtra(Main2Activity.LAT, "" + place.getLatLng().latitude);
                intent.putExtra(Main2Activity.title, "" + place.getAddress());
                startActivity(intent);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, FirebaseUIActivity.class);
        this.startActivity ( intent );

        findViewById(R.id.signup).setVisibility(View.GONE);
        findViewById(R.id.signout).setVisibility(View.VISIBLE);
    }

    public void onClickSignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Successfully Signed Out",
                Toast.LENGTH_LONG).show();

        findViewById(R.id.signup).setVisibility(View.VISIBLE);
        findViewById(R.id.signout).setVisibility(View.GONE);
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
            intent.putExtra(Main2Activity.title, "Current Location");
            intent.putExtra(Main2Activity.snippet, "You are here");
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
