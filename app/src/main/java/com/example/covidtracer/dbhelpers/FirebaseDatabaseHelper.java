package com.example.covidtracer.dbhelpers;

import androidx.annotation.NonNull;

import com.example.covidtracer.models.Meet;
import com.example.covidtracer.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseHelper {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
    private CollectionReference usersCollection, meetingsCollection;

    private FirebaseDatabaseHelper() {
        usersCollection = db.collection("users");
        meetingsCollection = db.collection("users_meetings");
    }

    public static FirebaseDatabaseHelper getInstance() {
        return firebaseDatabaseHelper;
    }

    public interface DataStatus {
        void Success();

        void Fail();
    }

    public void addUser(String id, User user, final DataStatus status) {
        usersCollection.document(id).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                });
    }

    public void addMeeting(String myUserUID, String metUserUID, Meet meet, final DataStatus status) {
        meetingsCollection.document(myUserUID).collection("meetings").document(metUserUID).set(meet)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                });
    }

    public void updateMeetingEnding(String myUserID, String metUserUID, FieldValue endingTimestamp, final DataStatus status) {
        DocumentReference meetToUpdate = meetingsCollection.document(myUserID).collection("meetings").document(metUserUID);
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("lostTimestamp", endingTimestamp);
        updatedFields.put("status", "ended");
        meetToUpdate.update(updatedFields)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                });
    }

    public void updateUserStatus(String myUserID, String newStatus, final DataStatus status) {
        DocumentReference userToUpdate = usersCollection.document(myUserID);
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("status", newStatus);
        userToUpdate.update(updatedFields)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                });
    }

    public void updateDeviceToken(String myUserID, String deviceToken, final DataStatus status) {
        DocumentReference userToUpdate = usersCollection.document(myUserID);
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("token", deviceToken);
        userToUpdate.update(updatedFields)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                });

    }
}
