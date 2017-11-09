package com.homecare.VCA.models;

import java.util.Date;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public Date age;
    public Address address;
    public String role;


    //TODO Make user an interface for patient, carer, doctor
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, Date DOB, String[] address, String role) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.address = new Address(address[0],address[1],address[2],address[3],address[4],address[5]);
        this.role = role;
    }

}
// [END blog_user_class]
