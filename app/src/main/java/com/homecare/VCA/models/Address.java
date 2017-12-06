package com.homecare.VCA.models;

/**
 * Created by dok-1 on 09/11/2017.
 */

public class Address {

    private String apartment;
    private String street1;
    private String street2;
    private String town_city;
    private String county;
    private String country;

    public Address(){
    }

    public Address(String a, String s1, String s2, String t_c, String c1, String c2){
        this.apartment = a;
        this.street1 = s1;
        this.street2 = s2;
        this.town_city = t_c;
        this.county = c1;
        this.country = c2;
    }

}
