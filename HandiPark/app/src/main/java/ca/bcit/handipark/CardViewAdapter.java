package ca.bcit.handipark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private static final String LONG = "-123.0";
    private static final String LAT = "49.249999";
    private ArrayList<Card> cardArrayList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocation;
        TextView textViewSpace;
        TextView textViewNotes;
        TextView textViewDistance;

        ViewHolder(View v) {
            super(v);

            textViewLocation = (TextView) v.findViewById(R.id.location_name);
            textViewSpace = (TextView) v.findViewById(R.id.space_availability);
            textViewNotes = (TextView) v.findViewById(R.id.notes);
            textViewDistance = (TextView) v.findViewById(R.id.distance);
        }
    }

    CardViewAdapter(ArrayList<Card> cards) {
        cardArrayList = cards;
    }

    @NonNull
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        context = v.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Intent intent = ((Activity) context).getIntent();
//        String longitude = intent.getStringExtra(LONG);
//        String latitude = intent.getStringExtra(LAT);

        double userLongitude = Double.parseDouble(LONG);
        double userLatitude = Double.parseDouble(LAT);

        Location loc1 = new Location("");
        loc1.setLongitude(userLongitude);
        loc1.setLatitude(userLatitude);

        Location loc2 = new Location("");
        loc2.setLongitude(cardArrayList.get(position).getLongitude());
        loc2.setLatitude(cardArrayList.get(position).getLatitude());
        double distance = (double) ((loc2.distanceTo(loc1)) / 1000);
        double distanceRounded = Math.round(distance * 10.0) / 10.0;

        holder.textViewLocation.setText(cardArrayList.get(position).getLocation());
        String spaces = "Spaces: " + cardArrayList.get(position).getSpace();
        String notes = "Notes: " + cardArrayList.get(position).getNotes();
        String dist = "" + distanceRounded + " km away";
        holder.textViewSpace.setText(spaces);
        holder.textViewNotes.setText(notes);
        holder.textViewDistance.setText(dist);
    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    static class Card {
        String location;
        int space;
        String notes;
        double longitude;
        double latitude;

        Card(String location, int space, String notes, double longitude, double latitude) {
            this.location = location;
            this.space = space;
            this.notes = notes;
            this.longitude = longitude;
            this.latitude = latitude;
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

        double getLongitude() {
            return longitude;
        }

        double getLatitude() {
            return latitude;
        }
    }
}
