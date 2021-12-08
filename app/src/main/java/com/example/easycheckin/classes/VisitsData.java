package com.example.easycheckin.classes;

public class VisitsData<T> {
    private Visits visits;
    private T t;

    public Visits getVisits( ){
        return visits;
    }

    public void setVisits(Visits visits){
        this.visits = visits;
    }

    public T getData( ){
        return t;
    }

    public void setData(T t){
        this.t = t;
    }

    @Override
    public String toString( ){
        return "VisitsData{" +
                "visits=" + visits +
                ", t=" + t +
                '}';
    }
}
