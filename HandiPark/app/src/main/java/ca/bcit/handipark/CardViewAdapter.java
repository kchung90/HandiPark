package ca.bcit.handipark;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
//        CheckBox favSelected;
        Button buttonDirections;
        Button buttonAddFav;
        Button buttonRemoveFav;

        ViewHolder(View v) {
            super(v);

            textViewLocation = (TextView) v.findViewById(R.id.location_name);
            textViewSpace = (TextView) v.findViewById(R.id.space_availability);
            textViewNotes = (TextView) v.findViewById(R.id.notes);
            textViewDistance = (TextView) v.findViewById(R.id.distance);
//            favSelected = (CheckBox) v.findViewById(R.id.fav_button);
            buttonDirections = (Button) v.findViewById(R.id.get_directions);
            buttonAddFav = (Button) v.findViewById(R.id.add_fav);
            buttonRemoveFav = (Button) v.findViewById(R.id.remove_fav);

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
        double userLongitude = Double.parseDouble(Main2Activity.longitude);
        double userLatitude = Double.parseDouble(Main2Activity.latitude);

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
                tabs = v.getRootView().findViewById(R.id.tabs);
                Main2Activity.longitude = "" + destination.getLongitude();
                Main2Activity.latitude = "" + destination.getLatitude();
                Main2Activity.title = cardArrayList.get(position).getLocation();
                Main2Activity.snippet = "" + distanceRounded + " km away";
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

//        holder.buttonDirections.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putDouble("longitude", cardArrayList.get(position).getCoordinates().get(0));
//                bundle.putDouble("latitude", cardArrayList.get(position).getCoordinates().get(1));
//
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Fragment3 frag3 = new Fragment3();
//                frag3.setArguments(bundle);
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.card_view, frag3).addToBackStack(null).commit();
//            }
//        });
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
