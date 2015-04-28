package com.example.Ilay.myapplication.backend;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ilay on 20/4/2015.
 */

@Entity
public class TrempitUser {
    @Id
    Long id;
    String fullName;
    Location homeLocation;
    List<Ref<Passenger>> passengerList = new ArrayList<>(); // a different Attender object for each Event
    List<Ref<Driver>> driverList = new ArrayList<>();

    public TrempitUser() {
        //this.id = new Long(1);
    }

    public TrempitUser(String fullName, Location homeLocation) {
        this.fullName = fullName;
        this.homeLocation = homeLocation;
    }

//    public List<Passenger> getPendingPassengers(){
//        List<Passenger> pendingPassengers = new ArrayList<Passenger>();
//        for(Driver driver : driverList)
//            for(Passenger passenger : driver.pendingPassengerList)
//                pendingPassengers.add(passenger);
//        return pendingPassengers;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Location getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
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

    public void addPassengerToUser(Passenger newPassenger){
        Ref<Passenger> passengerRef = Ref.create(newPassenger);
        passengerList.add(passengerRef);
    }

    public void addDriverToUser(Driver newDriver){
        Ref<Driver> driverRef = Ref.create(newDriver);
        driverList.add(driverRef);
    }


}
