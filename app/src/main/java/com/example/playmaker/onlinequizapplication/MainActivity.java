package com.example.playmaker.onlinequizapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.playmaker.onlinequizapplication.BroadCastReceiver.AlermReceiver;
import com.example.playmaker.onlinequizapplication.Common.Common;
import com.example.playmaker.onlinequizapplication.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtNewUser, edtNewPassword;//edtNewEmail;//for sign up
    MaterialEditText edtUser, edtPassword; //for sign in
    int signInOrUp = 0; // if 0 then singn In if 1 sign Up

    Button btnSignUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;
    DateFormat df = new SimpleDateFormat("HH:mm:ss");
    Calendar calobj = Calendar.getInstance();

    private boolean InternetCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Log.e("", "onCreate:" +df.format(calobj.getTime()));

        //registerAlarm();

        //Firebase
        database = FirebaseDatabase.getInstance();

        users = database.getReference("Users");

        edtUser = (MaterialEditText) findViewById(R.id.edUser);
        edtPassword = (MaterialEditText) findViewById(R.id.edPassword);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(edtUser.getText().toString(), edtPassword.getText().toString());

            }
        });
    }

    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(calendar.MINUTE, 30);
        calendar.set(calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, AlermReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void signIn(final String Par_user, final String Par_pass) {
        if (checkConnection()) {

            if (edtUser.length() != 0 && edtPassword.length() != 0) {
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(Par_user).exists()) {
                            if (!Par_user.isEmpty()) {
                                User login = dataSnapshot.child(Par_user).getValue(User.class);
                                if (login.getPassword().equals(Par_pass)) {
                                    //Toast.makeText(MainActivity.this, "Login Ok", Toast.LENGTH_SHORT).show();

                                    Intent homeActivity = new Intent(MainActivity.this, Home.class);
                                    Common.currentUser = login;
                                    startActivity(homeActivity);
                                    finish();
                                } else
                                    Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(MainActivity.this, "Please enter your user name", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity.this, "User is not exist", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else
                Toast.makeText(this, "please enter all information", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "please check internet", Toast.LENGTH_SHORT).show();
            InternetCheck = true;
            signInOrUp = 0;
            DialogAppear(signInOrUp);

        }
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("please Enter full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        edtNewUser = (MaterialEditText) sign_up_layout.findViewById(R.id.edNewUserName);
        //edtNewEmail = (MaterialEditText) sign_up_layout.findViewById(R.id.edNewEmail);
        edtNewPassword = (MaterialEditText) sign_up_layout.findViewById(R.id.edNewPassword);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                if (checkConnection()) {

                    if (edtNewUser.length() > 0 && edtNewPassword.length() > 0) {
                        final User user = new User(edtNewUser.getText().toString(),
                                edtNewPassword.getText().toString());

                        //,edtNewEmail.getText().toString());

                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(user.getUserName()).exists())
                                    Toast.makeText(MainActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
//                            else if (dataSnapshot.child(user.getEmail()).exists())
//                                Toast.makeText(MainActivity.this, "Email already used!", Toast.LENGTH_SHORT).show();
                                else {
                                    users.child(user.getUserName()).setValue(user);
                                    Toast.makeText(MainActivity.this, "User registration success!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        dialogInterface.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "please enter all information ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //Toast.makeText(MainActivity.this, "please check internet", Toast.LENGTH_SHORT).show();
                    InternetCheck = true;
                    signInOrUp = 1;
                    DialogAppear(signInOrUp);
                }
            }
        });
        alertDialog.show();
    }

    //Check Internet status of the mobile
    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    //Return Internet Status of the Mobile
    public boolean checkConnection() {
        if (isOnline()) {
            return InternetCheck;
            //Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        } else {
            InternetCheck = false;
            return InternetCheck;
            // Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * public void PostDelayedMethod()
     * {
     * <p>
     * new Handler().postDelayed(new Runnable() {
     * <p>
     * <p>
     * <p>
     * // Showing splash screen with a timer. This will be useful when you
     * // want to show case your app logo / company
     *
     * @Override public void run() {
     * <p>
     * // This method will be executed once the timer is over
     * // Start your app main activity
     * <p>
     * //                boolean InternetResult = checkConnection();
     * //                if(InternetResult){
     * //
     * //                    //open Activity when internet is connected
     * Intent intent=new Intent(MainActivity.this,Home.class);
     * intent.addCategory(Intent.CATEGORY_HOME);
     * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     * startActivity(intent);
     * finish();
     * //
     * //                }
     * //                else {
     * //                    spinner.setVisibility(View.VISIBLE);
     * //                    spinner.setVisibility(View.GONE);
     * //
     * //
     * //                    //Dialog Box show when internet is not connected
     * //                    DialogAppear();
     * //}
     * }
     * }, SPLASH_TIME_OUT);
     * }
     **/

    //DialogBox Main Function
    public void DialogAppear(final int InOrUp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);

        builder.setTitle("Network Error!");   //Title
        builder.setMessage("No Internet Connectivity");   //Message


        //Negative Message
        builder.setNegativeButton("Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        // close this activity When Exit is clicked
                        finish();
                    }
                });

        //Positive Message
        builder.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        //Check internet again when click on Retry by calling function
                        //run is not working there due to runnable method
                        // run();
                        //PostDelayedMethod();
                        if (InOrUp == 0)
                            signIn(edtUser.getText().toString(), edtPassword.getText().toString());
                        else
                            showSignUpDialog();
                    }
                });
        builder.show();
    }

}
//uk.co.chrisjenx