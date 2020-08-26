package com.example.financialportfoliomanagement.Auth;

import androidx.annotation.NonNull;

import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

public class Auth {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    public User user = new User();

    public Auth() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = firebaseAuth.getCurrentUser();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void getUser(final AuthOnCompleteRetreiveInterface authOnCompleteRetreiveInterface) {
        if (firebaseUser != null) {
            firebaseFirestore.collection("users").document(firebaseUser.getUid())
                    .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        user = task.getResult().toObject(User.class);
                        if (user != null) {
                            authOnCompleteRetreiveInterface.onFireBaseUserRetrieveSuccess();
                        } else {
                            authOnCompleteRetreiveInterface.onFireBaseUserRetrieveFailure();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    authOnCompleteRetreiveInterface.onFireBaseUserRetrieveFailure();
                }
            });
        } else {
            authOnCompleteRetreiveInterface.onFireBaseUserRetrieveFailure();
        }
    }

//    public void setUser(final AuthOnCompleteUpdateInterface authOnCompleteUpdateInterface, User new_user) {
//        if (firebaseUser != null) {
//            firebaseFirestore.collection("User").document(firebaseUser.getUid())
//                    .update("current_time", new_user.getCurrent_time(),
//                            "current_mall_id", new_user.getCurrent_mall_id(),
//                            "current_plot_id", new_user.getCurrent_plot_id(),
//                            "current_duration", new_user.getCurrent_duration(),
//                            "has_taken", new_user.isHas_taken(),
//                            "current_end_time", new_user.getCurrent_end_time(),
//                            "transaction_history", new_user.getTransaction_history());
//            authOnCompleteUpdateInterface.onFireBaseUserUpdateSuccess();
//        } else {
//            authOnCompleteUpdateInterface.onFireBaseUserUpdateFailure();
//        }
//    }

    public void signOut() {
        firebaseAuth.signOut();
    }

}
