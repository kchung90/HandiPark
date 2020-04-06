package ca.bcit.handipark;

import android.content.Intent;
import android.location.Location;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private ArrayList<Card> cardArrayList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocation;
        TextView textViewSpace;
        TextView textViewNotes;
        TextView textViewDistance;
        CheckBox favSelected;

        ViewHolder(View v) {
            super(v);

            textViewLocation = (TextView) v.findViewById(R.id.location_name);
            textViewSpace = (TextView) v.findViewById(R.id.space_availability);
            textViewNotes = (TextView) v.findViewById(R.id.notes);
            textViewDistance = (TextView) v.findViewById(R.id.distance);
            favSelected = (CheckBox) v.findViewById(R.id.fav_button);
        }
    }

    CardViewAdapter(ArrayList<Card> cards) {
        cardArrayList = cards;
    }

    @NonNull
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        double userLongitude = Double.parseDouble(Main2Activity.longitude);
        double userLatitude = Double.parseDouble(Main2Activity.latitude);

        Location startLocation = new Location("");
        startLocation.setLongitude(userLongitude);
        startLocation.setLatitude(userLatitude);

        Location destination = new Location("");
        destination.setLongitude(cardArrayList.get(position).getCoordinates().get(0));
        destination.setLatitude(cardArrayList.get(position).getCoordinates().get(1));

        double distance = (double) ((destination.distanceTo(startLocation)) / 1000);
        double distanceRounded = Math.round(distance * 10.0) / 10.0;

//        String key = "";
//        for (int i = 0; i < cardArrayList.get(position).getCoordinates().size(); i++) {
//            key += cardArrayList.get(position).getCoordinates().get(i);
//        }

        holder.textViewLocation.setText(cardArrayList.get(position).getLocation());
        String spaces = "Spaces: " + cardArrayList.get(position).getSpace();
        String notes = "Notes: " + cardArrayList.get(position).getNotes();
        String dist = "" + distanceRounded + " km away";
        holder.textViewSpace.setText(spaces);
        holder.textViewNotes.setText(notes);
        holder.textViewDistance.setText(dist);
        holder.favSelected.setChecked(cardArrayList.get(position).getSelected());
        holder.favSelected.setTag(cardArrayList.get(position));

        holder.favSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Card card = (Card) cb.getTag();

                card.setSelected(cb.isChecked());
                cardArrayList.get(position).setSelected(cb.isChecked());

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference user = database.getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                    user.child("favorites").child(cardArrayList.get(position).getLocation()).setValue(cardArrayList.get(position));
                }

                Toast.makeText(v.getContext(), "You have saved " + cardArrayList.get(position).getLocation() + " in your favorites.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    static class Card implements Comparable<Card> {
        public String location;
        public int space;
        public String notes;
        public double distance;
        public ArrayList<Double> coordinates;
        public boolean isSelected;

        public Card() {

        }

        Card(String location, int space, String notes, double distance, ArrayList<Double> coordinates, boolean isSelected) {
            this.location = location;
            this.space = space;
            this.notes = notes;
            this.distance = distance;
            this.coordinates = coordinates;
            this.isSelected = isSelected;
        }

        String getLocation() {
            return location;
        }

        int getSpace() {
            return space;
        }

        String getNotes() {
            return notes;
        }

        double getDistance() {
            return distance;
        }

        ArrayList<Double> getCoordinates() {
            return coordinates;
        }

        boolean getSelected() {
            return isSelected;
        }

        void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        @Override
        public int compareTo(Card o) {
            if (distance > o.getDistance()) {
                return 1;
            } else if (distance < o.getDistance()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
