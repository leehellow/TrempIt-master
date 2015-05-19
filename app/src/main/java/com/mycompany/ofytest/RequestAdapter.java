package com.mycompany.ofytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ilay.myapplication.backend.trempitApi.model.Passenger;

import java.util.List;

/**
 * Created by Lee on 4/28/2015.
 */
public class RequestAdapter extends ArrayAdapter<Passenger> {

    public RequestAdapter(Context context, List<Passenger> passengers) {
        super(context, 0, passengers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Passenger passenger = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_request, parent, false);
        }

        String username = passenger.getFullName();
        String location = TrempitUtils.parseLocation(passenger.getStartingLocation());
        String event = passenger.getEvent().getTitle();

        Button aprroveButton = (Button) convertView.findViewById(R.id.approveButton);
        Button declineButton = (Button) convertView.findViewById(R.id.declineButton);
        aprroveButton.setTag(passenger);
        declineButton.setTag(passenger);

        TextView content = (TextView) convertView.findViewById(R.id.passengerRequest);
        content.setText(username + " wants to join you ride from " + location + " to " + event);

        return convertView;
    }
}
