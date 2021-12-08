package com.example.easycheckin;

import com.example.easycheckin.classes.Institute;
import com.example.easycheckin.classes.Person;
import com.example.easycheckin.classes.VisitsData;

import java.util.Calendar;
import java.util.List;

public class Helper {

    public static String email;
    public static String userName;
    public static Person myDetails;
    public static Institute myInstitute;
    public static List<VisitsData<Institute>> instituteVisitsData;
    public static List<VisitsData<Person>> personVisitsData;

    public static String getDate(){
        String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return day +" "+months[month]+" "+year;
    }

    public static String getTime(){
        String[] amPM = new String[]{"AM","PM"};
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR)%12;
        int minutes = calendar.get(Calendar.MINUTE);
        String min = (minutes<10)?"0"+minutes: String.valueOf(minutes);
        return hour+":"+min+" "+amPM[calendar.get(Calendar.AM_PM)];
    }
}
