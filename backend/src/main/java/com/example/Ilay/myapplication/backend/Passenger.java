package com.example.Ilay.myapplication.backend;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Subclass;

/**
 * Created by Ilay on 20/4/2015.
 */
@Subclass(index = true)
public class Passenger extends Attender {
    Ref<Driver> driver;

    public Passenger() {
        super();
    }


    public Driver getDriver() {
        if (driver != null) {
            return driver.get();
        }
        else {
            return null;
        }
    }

    public void setDriver(Driver newDriver) {
        this.driver = Ref.create(newDriver);
    }
}
