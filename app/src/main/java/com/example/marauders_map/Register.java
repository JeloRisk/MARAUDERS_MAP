package com.example.marauders_map;

// import class, packages

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

    private ProgressDialog loadingbar;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://para-5564c-default-rtdb.firebaseio.com/");
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
                // delare vairbales
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
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(middleName) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(plateNumber) ||TextUtils.isEmpty(driverLicense)||TextUtils.isEmpty(confirmPassword)) {
                    // If any required field is empty, show error message
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)){
                    // If passwords do not match, show error message
                    Toast.makeText(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                }else{
                    loadingbar.setTitle("Customer Registration");
                    loadingbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loadingbar.setIndeterminate(true);
                    loadingbar.setCancelable(false);
                    loadingbar.setMessage("Please Wait..");
                    loadingbar.show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        CustomerOnlineID = mAuth.getCurrentUser().getUid();
                                        CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child("Customers").child(CustomerOnlineID);
                                        CustomerDatabaseRef.setValue(true);
                                        HashMap<String, Object> userMap = new HashMap<>();
                                        userMap.put("firstName", firstName);
                                        userMap.put("lastName", lastName);
                                        if(!TextUtils.isEmpty(middleName)) {
                                            userMap.put("middleName", middleName);
                                        }
                                        userMap.put("sex", sex);
                                        userMap.put("birthday", birthday);
                                        userMap.put("plateNumber", plateNumber);
                                        userMap.put("accountStatus", "unverified");
                                        userMap.put("driverLicense", driverLicense);
                                        userMap.put("password", password);
                                        userMap.put("email", email);
                                        CustomerDatabaseRef.updateChildren(userMap);
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(Register.this, "Passenger Registered.",
                                                Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();


                                    } else {
                                        Toast.makeText(Register.this, "Customer Registration Error." + task.getException().toString(),
                                                Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                }
                            });
                }





//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressBar.setVisibility(View.GONE);
//                                if (task.isSuccessful()) {
//
//                                    Toast.makeText(Register.this, "Account Created.",
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(Register.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });

            }
        });

    }
    private void check_terms() {
        buttonReg.setEnabled(checkBox.isChecked());

    }
}