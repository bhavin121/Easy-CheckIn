package com.example.easycheckin.classes;

public class Visits {
    private String userId;
    private String institutionId;
    private String date;
    private String time;

    public String getDate( ){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTime( ){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getUserId( ){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getInstitutionId( ){
        return institutionId;
    }

    public void setInstitutionId(String institutionId){
        this.institutionId = institutionId;
    }

    @Override
    public String toString( ){
        return "Visits{" +
                "userId='" + userId + '\'' +
                ", institutionId='" + institutionId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
