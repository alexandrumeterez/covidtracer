package com.example.covidtracer.dbhelpers;
import androidx.annotation.NonNull;
import com.example.covidtracer.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabaseHelper {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
    private CollectionReference usersCollection;

    private FirebaseDatabaseHelper() {
        usersCollection = db.collection("users");
    }

    public static FirebaseDatabaseHelper getInstance() {
        return firebaseDatabaseHelper;
    }

    public interface DataStatus {
        void InsertSuccess();

        void InsertFail();
    }

    public void addUser(String id, User user, final DataStatus status) {
        usersCollection.document(id).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status.InsertSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.InsertFail();
                    }
                });
    }

}
