package com.example.marauders_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Declare Variables
    Button buttonstart;
    FirebaseAuth auth;
    Button button1;
    TextView textView;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // get the button view and set an onClickListener
        buttonstart = (Button) findViewById(R.id.button);
        buttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextPage();
            }
        });
        auth = FirebaseAuth.getInstance();
        button1 = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


//        if(user == null){
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            startActivity(intent);
//            finish();
//        }
//        else{
//            textView.setText(user.getEmail());
//
//        }

    }

    public void goToNextPage() {
        if (user != null) {
            String uid = user.getUid();
            DocumentReference userRef = db.collection("drivers").document(uid);

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // Get the user data as a map
                        Map<String, Object> userData = documentSnapshot.getData();

                        // Get the user's name and display it
                        String firstName = (String) userData.get("firstName");
                        String accountStatus = (String) userData.get("accountStatus");

                        textView.setText("Welcome, " + firstName + "!");

                        // Get the user's birthday
                        String birthday = (String) userData.get("birthday");
                        // Do something with the birthday
                        Intent intent;
                        if (accountStatus.equals("unverified")){
                            intent = new Intent(MainActivity.this, RegistrationPendingActivity.class);
                        } else{
                            intent = new Intent(MainActivity.this, Register.class);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            // Create a new bundle for the transition options
                            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();

                            // Create a handler to post a delayed message to start the activity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Start the activity with the transition options bundle after the delay
                                    startActivity(intent, bundle);
                                }
                            }, 500); // Delay the message by 500 milliseconds (0.5 seconds)
                        } else {
                            // For older versions of Android, start the activity without the transition animation
                            startActivity(intent);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the error
                }
            });
        }
    }



}