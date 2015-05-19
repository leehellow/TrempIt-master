package com.mycompany.ofytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.ilay.myapplication.backend.trempitApi.model.Driver;

import java.util.List;

/**
 * Created by Lee on 4/26/2015.
 */
public class DriverAdapter extends ArrayAdapter<Driver> {

    public DriverAdapter(Context context, List<Driver> drivers) {
        super(context, 0, drivers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Driver driver = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_driver, parent, false);
        }
        Button button = (Button) convertView.findViewById(R.id.driverButton);
        button.setTransformationMethod(null);
        button.setText(TrempitUtils.parseDriver(driver));
        button.setTag(driver);

        return convertView;
    }
}
