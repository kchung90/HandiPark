package ca.bcit.handipark;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private ArrayList<Card> cardArrayList;
    private TabLayout tabs;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocation;
        TextView textViewSpace;
        TextView textViewNotes;
        TextView textViewDistance;
        ImageButton buttonDirections;
        Button buttonAddFav;
        Button buttonRemoveFav;

        ViewHolder(View v) {
            super(v);

            textViewLocation = (TextView) v.findViewById(R.id.location_name);
            textViewSpace = (TextView) v.findViewById(R.id.space_availability);
            textViewNotes = (TextView) v.findViewById(R.id.notes);
            textViewDistance = (TextView) v.findViewById(R.id.distance);
            buttonDirections = (ImageButton) v.findViewById(R.id.get_directions);
            buttonAddFav = (Button) v.findViewById(R.id.add_fav);
            buttonRemoveFav = (Button) v.findViewById(R.id.remove_fav);

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                buttonAddFav.setVisibility(View.VISIBLE);
                buttonRemoveFav.setVisibility(View.VISIBLE);
            } else {
                buttonAddFav.setVisibility(View.GONE);
                buttonRemoveFav.setVisibility(View.GONE);
            }

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        double userLongitude = Double.parseDouble(LandingPage.LONG);
        double userLatitude = Double.parseDouble(LandingPage.LAT);

        Location startLocation = new Location("");
        startLocation.setLongitude(userLongitude);
        startLocation.setLatitude(userLatitude);

        final Location destination = new Location("");
        destination.setLongitude(cardArrayList.get(position).getCoordinates().get(0));
        destination.setLatitude(cardArrayList.get(position).getCoordinates().get(1));

        final double distance = (double) ((destination.distanceTo(startLocation)) / 1000);
        final double distanceRounded = Math.round(distance * 10.0) / 10.0;

        holder.textViewLocation.setText(cardArrayList.get(position).getLocation());
        String spaces = "Spaces: " + cardArrayList.get(position).getSpace();
        String notes = "Notes: " + cardArrayList.get(position).getNotes();
        String dist = "" + distanceRounded + " km away";
        holder.textViewSpace.setText(spaces);
        holder.textViewNotes.setText(notes);
        holder.textViewDistance.setText(dist);

        holder.buttonDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LandingPage.longitude = "" + destination.getLongitude();
                LandingPage.latitude = "" + destination.getLatitude();
                LandingPage.title = cardArrayList.get(position).getLocation();
                LandingPage.snippet = "" + distanceRounded + " km away";
                tabs = v.getRootView().findViewById(R.id.tabs);
                Objects.requireNonNull(tabs.getTabAt(0)).select();
                Objects.requireNonNull(tabs.getTabAt(1)).select();
                Objects.requireNonNull(tabs.getTabAt(2)).select();
            }
        });

        holder.buttonAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardArrayList.get(position).setSelected(true);

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference user = database.getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                    user.child("favorites").child(cardArrayList.get(position).getLocation()).setValue(cardArrayList.get(position));
                }

                Toast.makeText(v.getContext(), "You have added " + cardArrayList.get(position).getLocation() + " to your favorites.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonRemoveFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardArrayList.get(position).setSelected(false);

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference user = database.getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                    user.child("favorites").child(cardArrayList.get(position).getLocation()).setValue(cardArrayList.get(position));
                }

                Toast.makeText(v.getContext(), "You have removed " + cardArrayList.get(position).getLocation() + " from your favorites.", Toast.LENGTH_SHORT).show();
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
