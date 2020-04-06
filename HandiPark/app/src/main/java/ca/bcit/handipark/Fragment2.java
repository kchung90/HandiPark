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


public class Fragment2 extends Fragment {
    private ArrayList<CardViewAdapter.Card> cardArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String uuid = FirebaseAuth.getInstance().getUid();
    DatabaseReference favRef = FirebaseDatabase.getInstance().getReference(uuid + "/favorites");

    public Fragment2() {
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

        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CardViewAdapter.Card card = snapshot.getValue(CardViewAdapter.Card.class);

                    if (card.isSelected) {
                        cardArrayList.add(new CardViewAdapter.Card(card.location, card.space, card.notes, card.distance, card.coordinates, card.isSelected));
                    }
                }

                adapter = new CardViewAdapter(cardArrayList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Log.d("DB123", "onCreateView: " + favRef);

        return root;
    }
}
