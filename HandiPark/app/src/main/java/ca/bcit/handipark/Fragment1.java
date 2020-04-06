package ca.bcit.handipark;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fragment1 extends Fragment {
    private ArrayList<CardViewAdapter.Card> cardArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_1, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        OkHttpClient client = new OkHttpClient();
        String url = "https://opendata.vancouver.ca/api/records/1.0/search/?dataset=disability-parking&facet=description&facet=notes&facet=geo_local_area";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final String myResponse = response.body().string();

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(myResponse);
                                JSONArray jsonArray = json.getJSONArray("records");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject record = jsonArray.getJSONObject(i);
                                    JSONObject fields = record.getJSONObject("fields");
                                    String location = fields.getString("location");
                                    int space = fields.getInt("spaces");
                                    String notes = fields.getString("notes");

                                    JSONObject geom = fields.getJSONObject("geom");
                                    JSONArray coordinates = geom.getJSONArray("coordinates");
                                    double longitude = coordinates.getDouble(0);
                                    double latitude = coordinates.getDouble(1);

                                    double userLongitude = Double.parseDouble(Main2Activity.longitude);
                                    double userLatitude = Double.parseDouble(Main2Activity.latitude);

                                    Location destination = new Location("");
                                    destination.setLongitude(userLongitude);
                                    destination.setLatitude(userLatitude);

                                    Location startLocation = new Location("");
                                    startLocation.setLongitude(longitude);
                                    startLocation.setLatitude(latitude);
                                    double distance = (double) ((startLocation.distanceTo(destination)) / 1000);

                                    cardArrayList.add(new CardViewAdapter.Card(location, space, notes, distance));
                                    Collections.sort(cardArrayList);
                                }

                                adapter = new CardViewAdapter(cardArrayList);
                                recyclerView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });

        return root;
    }
}
