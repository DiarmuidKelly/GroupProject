package com.homecare.VCA.models;

import java.util.Date;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public Boolean signedIn;
    public String username;
    public String email;
    public Date age;
    public Address address;
    public String role;
    public FirebaseAuth auth;

    //TODO Make user an interface for patient, carer, doctor
    public User() {

        this.resetAll();

    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getAge() {
        return age;
    }

    public void setAge(Date age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }
    public void resetAll(){
        this.signedIn = false;
        this.username = null;
        this.email = null;
        this.age = null;
        this.address = null;
        this.role = null;
        this.auth = null;

    }
}
