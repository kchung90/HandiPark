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
    private ArrayList<Card> cardArrayList;

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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        double userLongitude = Double.parseDouble(Main2Activity.longitude);
        double userLatitude = Double.parseDouble(Main2Activity.latitude);

        Location destination = new Location("");
        destination.setLongitude(userLongitude);
        destination.setLatitude(userLatitude);

        Location startLocation = new Location("");
        startLocation.setLongitude(cardArrayList.get(position).getLongitude());
        startLocation.setLatitude(cardArrayList.get(position).getLatitude());
        double distance = (double) ((startLocation.distanceTo(destination)) / 1000);
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
