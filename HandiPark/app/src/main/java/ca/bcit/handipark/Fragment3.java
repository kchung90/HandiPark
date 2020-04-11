package ca.bcit.handipark;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment3 extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;
    public double longitudeMap;
    public double latitudeMap;

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_3, container, false);
//        String longitude = Main2Activity.LONG;
//        String latitude = Main2Activity.LAT;

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately



        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                Bundle bundle = getArguments();
                if(bundle != null){
                    longitudeMap = bundle.getDouble("longitude");
                    latitudeMap = bundle.getDouble("latitude");
                } else {
                    longitudeMap = Double.parseDouble(Main2Activity.longitude);
                    latitudeMap = Double.parseDouble(Main2Activity.latitude);
                }

                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);
                googleMap.setTrafficEnabled(true);


                // For dropping a marker at a point on the Map
                LatLng cLoc = new LatLng(latitudeMap, longitudeMap);
                googleMap.addMarker(new MarkerOptions().position(cLoc).title(Main2Activity.title).snippet(Main2Activity.snippet));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cLoc).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
