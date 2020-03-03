package ca.bcit.handipark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
