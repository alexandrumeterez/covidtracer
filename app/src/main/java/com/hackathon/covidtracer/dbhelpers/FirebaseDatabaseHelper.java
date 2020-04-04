package com.hackathon.covidtracer.dbhelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.hackathon.covidtracer.R;
import com.hackathon.covidtracer.models.Meet;
import com.hackathon.covidtracer.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseHelper {
    private static final String TAG = "FirebaseDatabaseHelper";
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

    public void addUser(User user, final Context context, final DataStatus status) {
        final DocumentReference newUserRef = usersCollection.document();
        newUserRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(context.getString(R.string.UID), newUserRef.getId());
                        Log.d(TAG, "Added: " + newUserRef.getId());
                        editor.commit();
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
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

    public void getEncounteredUserInfo(String myUserID, String metUserUID) {

        DocumentReference meetToUpdate = meetingsCollection.document(myUserID).collection("meetings").document(metUserUID);
        Query query = usersCollection.whereEqualTo("users", metUserUID);

        meetToUpdate
                .whereEqualTo("users", metUserUID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
