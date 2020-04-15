package ca.bcit.handipark;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class FavoritesTab extends Fragment {
    private ArrayList<CardViewAdapter.Card> cardArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String uuid;
    DatabaseReference favRef;

    public FavoritesTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            View root = inflater.inflate(R.layout.fragment_2, container, false);

            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            uuid = FirebaseAuth.getInstance().getUid();
            favRef = FirebaseDatabase.getInstance().getReference(uuid + "/favorites");

            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cardArrayList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CardViewAdapter.Card card = snapshot.getValue(CardViewAdapter.Card.class);

                        assert card != null;
                        if (card.isSelected) {
                            cardArrayList.add(new CardViewAdapter.Card(card.location, card.space, card.notes, card.distance, card.coordinates, true));
                        }
                    }

                    adapter = new CardViewAdapter(cardArrayList);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            return root;
        } else {

            return inflater.inflate(R.layout.fragment_2_visitor, container, false);
        }
    }
}
