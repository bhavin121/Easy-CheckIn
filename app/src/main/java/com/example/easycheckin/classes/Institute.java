package com.example.easycheckin.classes;

import androidx.annotation.NonNull;

public class Institute extends User{
    private String institutionType;
    private String address;

    public String getInstitutionType( ){
        return institutionType;
    }

    public void setInstitutionType(String institutionType){
        this.institutionType = institutionType;
    }

    public String getAddress( ){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String completeAddress(){
        return address+",\n"+city+", "+state+", "+country;
    }

    @NonNull
    @Override
    public String toString( ){
        return "Institute{" +
                "institutionType='" + institutionType + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
