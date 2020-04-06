package ca.bcit.handipark;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Fragment3 extends Fragment {
    private ArrayList<CardViewAdapter.Card> cardArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String uuid = FirebaseAuth.getInstance().getUid();
    DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference(uuid + "/history");


    public Fragment3() {
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


//        cardArrayList.add(new CardViewAdapter.Card(location, space, notes, distance));
//        Collections.sort(cardArrayList);
//    }
//
//    adapter = new CardViewAdapter(cardArrayList);
//                                recyclerView.setAdapter(adapter);


        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CardViewAdapter.Card card = dataSnapshot.getValue(CardViewAdapter.Card.class);
                cardArrayList.add(new CardViewAdapter.Card(card.location, card.space, card.notes, card.distance));
                Collections.sort(cardArrayList);
                adapter = new CardViewAdapter(cardArrayList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Log.d("DB123", "onCreateView: " + historyRef);

////        try {
////            for (int i = 0; i < jsonArray.length(); i++) {
////                JSONObject record = jsonArray.getJSONObject(i);
////                JSONObject fields = record.getJSONObject("fields");
////                String location = fields.getString("location");
////                int space = fields.getInt("spaces");
////                String notes = fields.getString("notes");
////
////                JSONObject geom = fields.getJSONObject("geom");
////                JSONArray coordinates = geom.getJSONArray("coordinates");
////                double longitude = coordinates.getDouble(0);
////                double latitude = coordinates.getDouble(1);
////
////                double userLongitude = Double.parseDouble(Main2Activity.longitude);
////                double userLatitude = Double.parseDouble(Main2Activity.latitude);
////
////                Location destination = new Location("");
////                destination.setLongitude(userLongitude);
////                destination.setLatitude(userLatitude);
////
////                Location startLocation = new Location("");
////                startLocation.setLongitude(longitude);
////                startLocation.setLatitude(latitude);
////                double distance = (double) ((startLocation.distanceTo(destination)) / 1000);
////
//                CardViewAdapter.Card card1 = new CardViewAdapter.Card(location, space, notes, distance);
//
//                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference user = database.getReference(FirebaseAuth.getInstance().getUid());
//
//                    FirebaseDatabase.getInstance().getReference()
//                    user.child("favorites").setValue(card1);
//                    user.child("history").setValue(card1);
//                }
//                cardArrayList.add(card1);
//                Collections.sort(cardArrayList);
////            }
////
////            adapter = new CardViewAdapter(cardArrayList);
////            recyclerView.setAdapter(adapter);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return root;
    }
}
