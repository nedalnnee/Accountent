package com.msnit.accountent.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FirebaseUtils {
    private  static FirebaseUtils instance = null;
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;

    private FirebaseUtils() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseUtils getInstance() {
        FirebaseUtils object = Objects.requireNonNullElse(instance, new FirebaseUtils());
        instance= object;
        return object;
    }

    // Method to save email and UID to Realtime Database
    public void saveEmailToUid(String email) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("emails").child(email.replace(".", "_")).setValue(uid);
        }
    }

    // Method to query UID based on email
    public void queryUidByEmail(String email, final OnUidQueryListener listener) {
        mDatabase.child("emails").child(email.replace(".", "_")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue(String.class);
                if (uid != null) {
                    listener.onUidFound(uid);
                } else {
                    listener.onUidNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onUidNotFound();
            }
        });
    }

    // Listener interface for querying UID
    public interface OnUidQueryListener {
        void onUidFound(String uid);

        void onUidNotFound();
    }
}
