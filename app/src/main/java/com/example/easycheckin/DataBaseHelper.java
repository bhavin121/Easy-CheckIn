package com.example.easycheckin;

import androidx.annotation.NonNull;

import com.example.easycheckin.classes.Institute;
import com.example.easycheckin.classes.Person;
import com.example.easycheckin.classes.Visits;
import com.example.easycheckin.classes.VisitsData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataBaseHelper {

    public static final String PERSONS_COLLECTION = "persons";
    public static final String INSTITUTE_COLLECTION = "institute";
    public static final String VISITS_COLLECTION = "visits";

    public static void registerUser(Person person, Listener<Void> listener){
        uploadData(PERSONS_COLLECTION, person.getEmail() , person, listener);
    }

    public static void registerInstitution(Institute institute , Listener<Void> listener){
        uploadData(INSTITUTE_COLLECTION, institute.getEmail() , institute, listener);
    }

    public static void markVisit(Visits visits , Listener<Void> listener){
        FirebaseFirestore.getInstance().collection(INSTITUTE_COLLECTION)
                .document(visits.getInstitutionId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        listener.onDataPassed(documentSnapshot.get("name", String.class));
                        uploadData(VISITS_COLLECTION, String.valueOf((long) Math.pow(10, 15) - Calendar.getInstance().getTimeInMillis()) , visits, listener);
                    }else{
                        listener.onFailure("Institute is not registered");
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public static void getUserData(String email, Listener<Person> listener){
        DocumentReference reference = FirebaseFirestore.getInstance().collection(PERSONS_COLLECTION).document(email);
        
        reference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                listener.onSuccess(documentSnapshot.toObject(Person.class));
            }else{
                listener.onSuccess(null);
            }
        })
        .addOnFailureListener(e -> {
            listener.onFailure(e.getMessage());
        });
    }

    public static void getInstituteData(String email, Listener<Institute> listener){
        DocumentReference reference = FirebaseFirestore.getInstance().collection(INSTITUTE_COLLECTION).document(email);
        reference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                listener.onSuccess(documentSnapshot.toObject(Institute.class));
            }else{
                listener.onSuccess(null);
            }
        }).addOnFailureListener(e -> {
            listener.onFailure(e.getMessage());
        });
    }

    private static<T> void uploadData(String collection, String fileName,T data, Listener<Void> listener){
        FirebaseFirestore.getInstance().collection(collection)
                .document(fileName)
                .set(data)
                .addOnSuccessListener(documentReference -> {
                    listener.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e.getMessage());
                });
    }

    public static void fetchUsersVisited(String email, String date,Listener<List<VisitsData<Person>>> listener){
        FirebaseFirestore.getInstance().collection(VISITS_COLLECTION)
                .whereEqualTo("institutionId", email)
                .whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Visits> visitsList = queryDocumentSnapshots.toObjects(Visits.class);
                    List<String> usersIds = new ArrayList<>(visitsList.size());
                    for ( Visits v : visitsList ) {
                        usersIds.add(v.getUserId());
                    }
                    if(usersIds.isEmpty()){
                        listener.onSuccess(new ArrayList<>());
                        return;
                    }

                    FirebaseFirestore.getInstance().collection(PERSONS_COLLECTION)
                            .whereIn("email", usersIds)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                List<Person> personList = queryDocumentSnapshots1.toObjects(Person.class);
                                Map<String, Person> personMap = new HashMap<>();
                                for ( Person p : personList ) {
                                    personMap.put(p.getEmail(), p);
                                }
                                List<VisitsData<Person>> personsVisited = new ArrayList<>(visitsList.size());

                                for ( Visits v : visitsList ) {
                                    VisitsData<Person> visitsData = new VisitsData<>();
                                    visitsData.setVisits(v);
                                    visitsData.setData(personMap.get(v.getUserId()));
                                    personsVisited.add(visitsData);
                                }
                                listener.onSuccess(personsVisited);
                            })
                            .addOnFailureListener(e -> {
                                listener.onFailure(e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e.getMessage());
                });
    }

    public static void fetchVisitedPlaces(String email, Listener<List<VisitsData<Institute>>> listener){
        FirebaseFirestore.getInstance().collection(VISITS_COLLECTION)
                .whereEqualTo("userId", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Visits> visitsList = queryDocumentSnapshots.toObjects(Visits.class);
                    List<String> ids = new ArrayList<>(visitsList.size());
                    for ( Visits v : visitsList ) {
                        ids.add(v.getInstitutionId());
                    }
                    if(ids.isEmpty()){
                        listener.onSuccess(new ArrayList<>());
                        return;
                    }

                    FirebaseFirestore.getInstance().collection(INSTITUTE_COLLECTION)
                            .whereIn("email", ids)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                List<Institute> instituteList = queryDocumentSnapshots1.toObjects(Institute.class);
                                Map<String, Institute> map = new HashMap<>();
                                for ( Institute p : instituteList ) {
                                    map.put(p.getEmail(), p);
                                }
                                List<VisitsData<Institute>> placesVisited = new ArrayList<>(visitsList.size());

                                for ( Visits v : visitsList ) {
                                    VisitsData<Institute> visitsData = new VisitsData<>();
                                    visitsData.setVisits(v);
                                    visitsData.setData(map.get(v.getInstitutionId()));
                                    placesVisited.add(visitsData);
                                }
                                listener.onSuccess(placesVisited);
                            })
                            .addOnFailureListener(e -> {
                                listener.onFailure(e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e.getMessage());
                });
    }

    public interface Listener<T>{
        void onSuccess(T t);
        void onFailure(String message);
        default void onDataPassed(String data){ }
    }
}
