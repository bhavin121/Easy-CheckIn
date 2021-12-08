package com.example.easycheckin.classes;

public class Person extends User{

    private int age;
    private String gender;
    private String covidStatus;

    public int getAge( ){
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public String getGender( ){
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public String getCovidStatus( ){
        return covidStatus;
    }

    public void setCovidStatus(String covidStatus){
        this.covidStatus = covidStatus;
    }

    public String completeAddress(){
        return city+", "+state+", "+country;
    }

    @Override
    public String toString( ){
        return "Person{" +
                "age=" + age +
                ", gender='" + gender + '\'' +
                ", covidStatus='" + covidStatus + '\'' +
                ", name='" + name + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
