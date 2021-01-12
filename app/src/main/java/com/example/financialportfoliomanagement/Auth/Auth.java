//package com.example.financialportfoliomanagement.Auth;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
//import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteUpdateInterface;
//import com.example.financialportfoliomanagement.Models.User;
//import com.google.android.gms.tasks.OnCanceledListener;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Source;
//
//public class Auth {
//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser firebaseUser;
//    private FirebaseFirestore firebaseFirestore;
//    public User user = new User();
//
//    public Auth() {
//        this.firebaseAuth = FirebaseAuth.getInstance();
//        this.firebaseUser = firebaseAuth.getCurrentUser();
//        this.firebaseFirestore = FirebaseFirestore.getInstance();
//    }
//
//    public void getUser(final AuthOnCompleteRetreiveInterface authOnCompleteRetreiveInterface) {
//        if (firebaseUser != null) {
//            firebaseFirestore.collection("users").document(firebaseUser.getUid())
//                    .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        user = task.getResult().toObject(User.class);
//                        if (user != null) {
//                            authOnCompleteRetreiveInterface.onFireBaseUserRetrieveSuccess();
//                        } else {
//                            authOnCompleteRetreiveInterface.onFireBaseUserRetrieveFailure();
//                        }
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    authOnCompleteRetreiveInterface.onFireBaseUserRetrieveFailure();
//                }
//            });
//        } else {
//            authOnCompleteRetreiveInterface.onFireBaseUserRetrieveFailure();
//        }
//    }
//
//    public void setUser(final AuthOnCompleteUpdateInterface authOnCompleteUpdateInterface, final User new_user) {
//        if (firebaseUser != null) {
//
//            firebaseFirestore.collection("users").document(firebaseUser.getUid())
//                    .update("watch_list_symbols", new_user.getWatch_list_symbols()
//                            ,"transactions",new_user.getTransactions()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol added ");
//                        user = new_user;
//                        authOnCompleteUpdateInterface.onFireBaseUserUpdateSuccess();
//                    } else {
//                        Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol not added ");
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol not added " + e);
//                }
//            }).addOnCanceledListener(new OnCanceledListener() {
//                @Override
//                public void onCanceled() {
//                    Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol not added ");
//                }
//            });
//
//        } else {
//            authOnCompleteUpdateInterface.onFireBaseUserUpdateFailure();
//            Log.i("TAG", ">>>>>>>>>>>>>>>>>>>>>>>>>>>symbol not added");
//        }
//    }
//
//    public void signOut() {
//        firebaseAuth.signOut();
//    }
//
//}
