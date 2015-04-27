package com.mycompany.ofytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.ilay.myapplication.backend.trempitApi.model.Event;

import java.util.List;

/**
 * Created by Lee on 4/26/2015.
 */
public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        Button button = (Button) convertView.findViewById(R.id.eventButton);
        button.setTransformationMethod(null);
        button.setText(event.getTitle() + "\n" + event.getStartTime().toStringRfc3339() + "\n" + event.getLocation().getStreet() + ", " + event.getLocation().getCity());
        button.setTag(event);

        return convertView;
    }
}
