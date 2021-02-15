package com.example.financialportfoliomanagement.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.financialportfoliomanagement.Models.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AuthViewModel extends ViewModel {
    //    Auth auth;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    public User user = new User();
    MutableLiveData<User> userData;

    public AuthViewModel() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = firebaseAuth.getCurrentUser();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        setFirebaseFirestore();
    }

    public MutableLiveData<User> getAuthData() {
        if (userData == null) {
            userData = new MutableLiveData<User>();
        }
        return userData;
    }

    public void setFirebaseFirestore() {
        if (firebaseUser != null) {
            firebaseFirestore.collection("users").document(firebaseUser.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot != null) {
                                user = documentSnapshot.toObject(User.class);
                                userData.setValue(user);
                            } else {
                                Log.i("TAG", ">>>>>>>>>>>>>>>>>" + e.toString());
                            }

                        }
                    });
        }
    }

    public void updateUser(final User new_user) {
        if (firebaseUser != null) {

            firebaseFirestore.collection("users").document(firebaseUser.getUid())
                    .update("watch_list_symbols", new_user.getWatch_list_symbols(),
                            "recommendation_list", new_user.getRecommendation_list()
                            , "riskScore", new_user.getRiskScore()
                            , "category", new_user.getCategory()
                            , "transactions", new_user.getTransactions()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol added ");
                        user = new_user;
                        userData.setValue(user);
                    } else {
                        Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol not added ");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol not added " + e);
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol not added ");
                }
            });

        }
    }

    public void SignOut() {
        userData.setValue(null);
        firebaseAuth.signOut();
    }


}
