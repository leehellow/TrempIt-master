package com.example.Ilay.myapplication.backend;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ilay on 20/4/2015.
 */

@Entity
public class Event {
    @Id
    Long id;
    Date startTime;
    Location location;
    String title;
    List<Ref<Driver>> driverList = new ArrayList<>();
    List<Ref<Passenger>> passengerList = new ArrayList<>();
    //Map<Driver, List<Passenger>> pendingPassengers; //TODO: use Driver as key or driverid?

    public Event() {
        //this.id = new Long(1);
    }

    public Event(Date startTime, Location location, String title) {
        this.startTime = startTime;
        this.location = location;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Driver> getDriverList() {
        List<Driver> ret = new ArrayList<>();
        Iterator<Ref<Driver>> it = driverList.iterator();

        while (it.hasNext())
            ret.add(it.next().get());
        return ret;
    }

    public void setDriverList(List<Driver> newDriverList) {
        Iterator<Driver> it = newDriverList.iterator();

        while (it.hasNext()) {
            Ref<Driver> newDriver = Ref.create(it.next());
            driverList.add(newDriver);
        }
    }

    public List<Passenger> getPassengerList() {
        List<Passenger> ret = new ArrayList<>();
        Iterator<Ref<Passenger>> it = passengerList.iterator();

        while (it.hasNext())
            ret.add(it.next().get());
        return ret;
    }

    public void setPassengerList(List<Passenger> newPassengerList) {
        Iterator<Passenger> it = newPassengerList.iterator();

        while (it.hasNext()) {
            Ref<Passenger> newPassenger = Ref.create(it.next());
            passengerList.add(newPassenger);
        }
    }

    public void addPassenger(Passenger newPassenger) {
        Ref<Passenger> passengerRef = Ref.create(newPassenger);
        passengerList.add(passengerRef);
    }

    public void addDriver(Driver newDriver){
        Ref<Driver> driverRef = Ref.create(newDriver);
        driverList.add(driverRef);
    }
}
