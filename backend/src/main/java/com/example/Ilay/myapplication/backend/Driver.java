package com.example.Ilay.myapplication.backend;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonManagedReference;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Subclass;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ilay on 20/4/2015.
 */
@Subclass(index = true)
public class Driver extends Attender {
    int availableSeats;
    Date arrivalTime;
    @JsonManagedReference(value = "driver-passenger-list")
    List<Ref<Passenger>> passengerList = new ArrayList<>();
    //@JsonManagedReference(value = "driver-pendingpassenger-list")
    List<Ref<Passenger>> pendingPassengerList = new ArrayList<>();


    //eran katz!!!!!

    public Driver() {
        super();
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
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

    public List<Passenger> getPendingPassengerList() {
        List<Passenger> ret = new ArrayList<>();
        Iterator<Ref<Passenger>> it = pendingPassengerList.iterator();

        while (it.hasNext())
            ret.add(it.next().get());
        return ret;
    }

    public void setPendingPassengerList(List<Passenger> newPassengerList) {
        Iterator<Passenger> it = newPassengerList.iterator();

        while (it.hasNext()) {
            Ref<Passenger> newPassenger = Ref.create(it.next());
            pendingPassengerList.add(newPassenger);
        }
    }

    public void addPassengerToPassengerList(Passenger passenger) {
        Ref<Passenger> passengerRef = Ref.create(passenger);
        this.passengerList.add(passengerRef);
    }

    public void removePassengerFromPassengerList(Passenger passenger) {
        Ref<Passenger> passengerRef = Ref.create(passenger);
        this.passengerList.remove(passengerRef);
    }

    public void addPassengerToPendingPassengerList(Passenger passenger) {
        Ref<Passenger> passengerRef = Ref.create(passenger);
        this.pendingPassengerList.add(passengerRef);
    }

    public void removePassengerFromPendingPassengerList(Passenger passenger) {
        Ref<Passenger> passengerRef = Ref.create(passenger);
        this.pendingPassengerList.remove(passengerRef);
    }
}
