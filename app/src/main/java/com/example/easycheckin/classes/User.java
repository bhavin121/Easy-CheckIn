package com.example.easycheckin.classes;

public class User {
    protected String name;
    protected String mobileNo;
    protected String city;
    protected String state;
    protected String country;
    protected String email;

    public String getName( ){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getMobileNo( ){
        return mobileNo;
    }

    public void setMobileNo(String mobileNo){
        this.mobileNo = mobileNo;
    }

    public String getCity( ){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getState( ){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getCountry( ){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getEmail( ){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
