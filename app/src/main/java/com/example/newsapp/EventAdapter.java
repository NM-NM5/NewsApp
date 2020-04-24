package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, ArrayList<Event> words) {
        super(context, 0, words);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.event_layout, parent, false);
        }

        Event currentEvent = getItem(position);

        TextView title = listItemView.findViewById(R.id.title_text);
        TextView section = listItemView.findViewById(R.id.section_text);
        TextView date = listItemView.findViewById(R.id.date_text);
        TextView time = listItemView.findViewById(R.id.time_text);

        assert currentEvent != null;
        title.setText(currentEvent.getTitle());
        section.setText(currentEvent.getSection());
        date.setText(currentEvent.getDate());
        time.setText(currentEvent.getTime());

        return listItemView;
    }
}