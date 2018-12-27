package com.example.husseiiny.e_commerce;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.husseiiny.e_commerce.Database.CommerceDBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private CommerceDBHelper mDbHelper;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String name;
    private String email;
    private String password;
    private String birthday;
    private String job;
    private String gender;

    private EditText editTextName;
    private EditText editTextSignEmail;
    private EditText editTextSignPassword;
    private EditText editTextBirthday;
    private EditText editTextJob;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private Button buttonCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextName = findViewById(R.id.edit_name);
        editTextSignEmail  = findViewById(R.id.edit_sign_email);
        editTextSignPassword  = findViewById(R.id.edit_sign_password);
        editTextBirthday  = findViewById(R.id.edit_birthdate);
        editTextJob  = findViewById(R.id.edit_job);
        radioButtonMale = findViewById(R.id.radio_male);
        radioButtonFemale = findViewById(R.id.radio_female);
        buttonCreate = findViewById(R.id.button_create);

        mDbHelper = new CommerceDBHelper(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user.sendEmailVerification();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        //=====================================DATE PICKER==========================================
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                editTextBirthday.setText(sdf.format(calendar.getTime()));
            }
        };

        editTextBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //=================================SIGN IN OPERATION========================================
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextName.getText().toString().trim();
                email = editTextSignEmail.getText().toString().trim();
                password = editTextSignPassword.getText().toString().trim();
                birthday = editTextBirthday.getText().toString().trim();
                job = editTextJob.getText().toString().trim();

                // Check Validation of input data
                if(checkValidationData()) {
                    // Insert data into table customer
                    mDbHelper.createNewUser(name, email, password, gender, birthday, job);
                    // Send verification mail to user
                    mAuth.createUserWithEmailAndPassword(email, password);
                    mAuth.addAuthStateListener(mAuthListener);

                    // back to Login Activity
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkValidationData() {
        // Check Input Name..
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter Valid Name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check Input Email..
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter Valid Email!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check Input Password..
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Valid Password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check Input Birthday..
        if(TextUtils.isEmpty(birthday)){
            Toast.makeText(this, "Enter Valid Birthday!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check Input Job..
        if(TextUtils.isEmpty(job)){
            Toast.makeText(this, "Enter Valid Job!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(radioButtonMale.isChecked()) gender = "male";
        else if(radioButtonFemale.isChecked()) gender = "female";
        else{
            Toast.makeText(this, "Select Specific Gender!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
