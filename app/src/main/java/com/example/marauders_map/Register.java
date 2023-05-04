package com.example.marauders_map;

// import class, packages

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {
    // delares variables
    private TextInputEditText editTextEmail, editTextPassword, birthdayEditText, confirmTextPassword;
    private TextInputEditText editFirstName, editLastName, editMiddleName, editPlateNumber, editDriverLicense;
    private RadioGroup editSex;
    private TextInputLayout birthdayInputLayout;
    private Button buttonReg;
    private CheckBox checkBox;
    private String CustomerOnlineID;
    private DatabaseReference CustomerDatabaseRef;
    private ImageView idImageView;

    private Button uploadIdButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    //    CollectionReference usersRef = db.collection("drivers");
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://para-5564c-default-rtdb.firebaseio.com/");
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        confirmTextPassword = findViewById(R.id.confirmPassword);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        birthdayInputLayout = findViewById(R.id.birthdayInputLayout);
        editFirstName = findViewById(R.id.firstName);
        editLastName = findViewById(R.id.lastName);
        editMiddleName = findViewById(R.id.middleName);
        editSex = findViewById(R.id.sex);
        editPlateNumber = findViewById(R.id.plateNumber);
        editDriverLicense = findViewById(R.id.driverLicense);
        loadingbar = new ProgressDialog(this);
        checkBox = findViewById(R.id.terms);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_terms();
            }
        });
        //        date
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                Log.d("TAG", "Selected date: " + selectedDate);
                birthdayEditText.setText(selectedDate);
                birthdayInputLayout.setHint("");
            }


        };

        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,
                        dateSetListener, year, month, dayOfMonth);
                datePickerDialog.show();

            }
        });
        //

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // declare variables
                progressBar.setVisibility(View.VISIBLE);
                String email, password, firstName, lastName, middleName, birthday, sex, plateNumber, driverLicense, confirmPassword;
                int selectedId = editSex.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                firstName = String.valueOf(Objects.requireNonNull(editFirstName.getText()).toString());
                lastName = String.valueOf(Objects.requireNonNull(editLastName.getText()).toString());
                middleName = String.valueOf(Objects.requireNonNull(editMiddleName.getText()).toString());
                birthday = String.valueOf(Objects.requireNonNull(birthdayEditText.getText()).toString());
                sex = selectedRadioButton.getText().toString();
                plateNumber = String.valueOf(Objects.requireNonNull(editPlateNumber.getText()).toString());
                driverLicense = String.valueOf(Objects.requireNonNull(editDriverLicense.getText()).toString());
                confirmPassword = String.valueOf(Objects.requireNonNull(confirmTextPassword.getText()).toString());
                email = String.valueOf(Objects.requireNonNull(editTextEmail.getText()).toString());
                password = String.valueOf(Objects.requireNonNull(editTextPassword.getText()).toString());
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(sex) || TextUtils.isEmpty(plateNumber) || TextUtils.isEmpty(driverLicense) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(Register.this, "Please Fill in All Fields!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else if (password.equals(confirmPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                assert currentUser != null;
                                String uid = currentUser.getUid();

                                CollectionReference usersRef = db.collection("drivers");
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("firstName", firstName);
                                userMap.put("lastName", lastName);
                                userMap.put("middleName", middleName);
                                userMap.put("birthday", birthday);
                                userMap.put("sex", sex);
                                userMap.put("accountStatus", "unverified");
                                userMap.put("plateNumber", plateNumber);
                                userMap.put("driverLicense", driverLicense);
                                userMap.put("uid", uid);
                                usersRef.document(uid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            String message = Objects.requireNonNull(task.getException()).getMessage();
                                            Toast.makeText(Register.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                String message = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(Register.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Register.this, "Password not match!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }

    private void check_terms() {
        buttonReg.setEnabled(checkBox.isChecked());

    }
}