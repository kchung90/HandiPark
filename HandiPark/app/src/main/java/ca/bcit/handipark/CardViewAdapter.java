package ca.bcit.handipark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private ArrayList<Card> cardArrayList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewLocation;
        public TextView textViewSpace;
        public TextView textViewNotes;

        public ViewHolder(View v) {
            super(v);

            textViewLocation = (TextView) v.findViewById(R.id.location_name);
            textViewSpace = (TextView) v.findViewById(R.id.space_availability);
            textViewNotes = (TextView) v.findViewById(R.id.notes);
        }
    }

    public CardViewAdapter(ArrayList<Card> cards) {
        cardArrayList = cards;
    }

    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewLocation.setText(cardArrayList.get(position).getLocation());
        holder.textViewSpace.setText(String.valueOf(cardArrayList.get(position).getSpace()));
        holder.textViewNotes.setText(cardArrayList.get(position).getNotes());
    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    public static class Card {
        String location;
        int space;
        String notes;

        public Card(String location, int space, String notes) {
            this.location = location;
            this.space = space;
            this.notes = notes;
        }

        public String getLocation() {
            return location;
        }

        public int getSpace() {
            return space;
        }

        public String getNotes() {
            return notes;
        }
    }
}
