package com.example.Ilay.myapplication.backend;

import com.googlecode.objectify.annotation.Subclass;

import java.util.Date;
import java.util.List;

/**
 * Created by Ilay on 20/4/2015.
 */
@Subclass(index = true)
public class Driver extends Attender {
    int availableSeats;
    Date arrivalTime;
    List<Passenger> passengerList;
    List<Passenger> pendingPassengerList;


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
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public void approvePassenger(Passenger passenger) {
        this.pendingPassengerList.remove(passenger);
        this.passengerList.add(passenger);
        passenger.setDriver(this);
    }
}
