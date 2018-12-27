package com.example.husseiiny.e_commerce;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.husseiiny.e_commerce.Database.CommerceDBHelper;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_ID = 1;

    public static final String PREFERENCE_LOGIN_SAVE_KEY = "saveLogin";
    public static final String PREFERENCE_USERNAME_KEY = "username";
    public static final String PREFERENCE_PASSWORD_KEY = "password";

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private CommerceDBHelper mDbHelper;

    private String username;

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogIn;
    private TextView textViewSignUp, textViewForgetPassword;
    private CheckBox checkBoxRememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);
        buttonLogIn = findViewById(R.id.button_login);
        textViewSignUp = findViewById(R.id.textview_signup);
        textViewForgetPassword = findViewById(R.id.text_forget_password);
        checkBoxRememberMe = findViewById(R.id.checkBox_remember);

        mDbHelper = new CommerceDBHelper(this);


        //=================================REMEMBER ME PROCESS======================================
        loginPreferences = getSharedPreferences("rememberMe", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        if(loginPreferences.getBoolean(PREFERENCE_LOGIN_SAVE_KEY, false)){
            editTextEmail.setText(loginPreferences.getString(PREFERENCE_USERNAME_KEY, ""));
            editTextPassword.setText(loginPreferences.getString(PREFERENCE_PASSWORD_KEY, ""));
            checkBoxRememberMe.setChecked(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //=====================================LOGIN OPERATION======================================
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                    if(checkBoxRememberMe.isChecked()){
                        loginPrefsEditor.putBoolean(PREFERENCE_LOGIN_SAVE_KEY, true);
                        loginPrefsEditor.putString(PREFERENCE_USERNAME_KEY, username);
                        loginPrefsEditor.putString(PREFERENCE_PASSWORD_KEY, password);
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                    if(mDbHelper.checkCredentials(username, password)){
                        ShoppingCart.setCustomer(username);
                        Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                        startActivity(intent);
                    }
                    else showAlertDialog();
                }
                else {
                    showAlertDialog();
                }
            }
        });


        //====================================SIGN UP OPERATION=====================================
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //=======================================FORGET PASSWORD====================================
        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsernameDialog();
            }
        });
    }

    private void sendPasswordByNotification() {

        Context cActivity = MainActivity.this;
        // Generate the new password
        String newPassword = generatePassword();

        // Notify user by the new password generation
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("com.example.husseiiny.e_commerce"));
        PendingIntent pendingIntent = PendingIntent.getActivity(cActivity, 0, intent, 0);

        Notification builder = new NotificationCompat.Builder(cActivity, "1")
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifi_commerce))
                .setSmallIcon(R.drawable.ic_notification_password)
                .setContentTitle("E-Commerce System")
                .setContentText("Password Reset")
                .setSubText("Your password has been reset to " + newPassword)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder);

        // Update new password in database
        mDbHelper.updatePassword(username, newPassword);
    }

    private String generatePassword() {
        String[] alphabet = {"a", "b", "c", "d", "e", "f"};
        Random random = new Random();
        return alphabet[random.nextInt(5)] + String.valueOf(11 * random.nextInt(55)) +
                alphabet[random.nextInt(5)];
    }

    private void setUsernameDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_username, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setView(view);
        alertDialog.show();

        final EditText editTextUsername = view.findViewById(R.id.edit_dialog_username);
        Button buttonUpdateUsername = view.findViewById(R.id.button_dialog_username);

        buttonUpdateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDbHelper.checkCustomerUsername(editTextUsername.getText().toString().trim())){
                    sendPasswordByNotification();
                }else{
                    Toast.makeText(MainActivity.this,"Username is invalid\nTry again!", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });

    }

    private void showAlertDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Caution!");
        alertDialog.setMessage("Username or Password is Invalid!\nSign up to create a new account");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
