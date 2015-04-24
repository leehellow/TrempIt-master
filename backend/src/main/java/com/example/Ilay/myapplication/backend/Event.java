package com.example.Ilay.myapplication.backend;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    List<Ref<Attender>> attenderList = new ArrayList<>();
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

    public List<Attender> getAttenderList() {
        List<Attender> ret = new ArrayList<>();
        Iterator<Ref<Attender>> it = attenderList.iterator();

        while (it.hasNext())
            ret.add(it.next().get());
        return ret;
    }

    public void setAttenderList(List<Attender> newAttenderList) {
        Iterator<Attender> it = newAttenderList.iterator();

        while (it.hasNext()) {
            Ref<Attender> newAttender = Ref.create(it.next());
            attenderList.add(newAttender);
        }
    }

    public void addAttender(Attender newAttender) {
        Ref<Attender> attenderRef = Ref.create(newAttender);
        attenderList.add(attenderRef);
    }

    public void addDriver(Driver newDriver){
        Ref<Driver> driverRef = Ref.create(newDriver);
        driverList.add(driverRef);
    }
}
