package com.nwhacks.safetalk;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private User user;
    private String userId;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback callback;
    private LocationRequest mLocationRequest;
    private AddressResultReceiver mResultReceiver;

    private FloatingActionButton addFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("Whoops", "Whoops");
        Intent intent = getIntent();
        if(intent.getExtras() == null){
            Log.d("CREATION", "null intent");
            if(savedInstanceState == null){
                Intent launchSignUpPage = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(launchSignUpPage);
            }else {
                userId = savedInstanceState.getString("id");
                mDatabase = FirebaseDatabase.getInstance().getReference();
                locationSetup();
                retrieveUser();
            }
        }else {
            userId = intent.getExtras().getString("id");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            locationSetup();
            retrieveUser();
            Log.d("CREATION", userId);
            savedInstanceState = new Bundle();
            savedInstanceState.putString("id", userId);
        }
        mResultReceiver = new AddressResultReceiver(new Handler());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationRequest = createDangerLocationRequest();
                sendGroupSMS(user);
            }
        });
        Button logoutButton = findViewById(R.id.Logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Log.d("CREATION", "logout");
                Intent launchSignUpPage = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(launchSignUpPage);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent launchSignUpPage = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(launchSignUpPage);
        }else {
            try {
                startLocationUpdates();
            }catch(RuntimeException e){

            }
        }
    }
    private void retrieveUser(){
        Query findUser = mDatabase.child("SafeTalkUsers").orderByChild("userId").equalTo(userId);
        findUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String string) {
                Log.d("RETRIEVAL", "Worked");
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String string) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String string) {
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permissions are required!", Toast.LENGTH_SHORT).show();
                    Log.d("CREATION", "permission problems");
                    finish();
                    moveTaskToBack(true);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private LocationRequest createDangerLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private LocationRequest createStandardLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(600000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    private void startLocationUpdates() {
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    callback,
                    null /* Looper */);
        }catch (SecurityException e){

        }
    }
    private void locationSetup(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });
        }catch(SecurityException e){
            Toast.makeText(getApplicationContext(), "Enable location permissions to use" +
                    "SafeChat", Toast.LENGTH_LONG).show();
            Log.d("CREATION", "location problems");
            finish();
            moveTaskToBack(true);
        }
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                mDatabase.child("SafeTalkUsers").child(userId).child("userLocation")
                        .setValue(locationResult.getLastLocation());
                user.setUserLocation(new MyLocation(locationResult.getLastLocation()));
                startIntentService(locationResult.getLastLocation());
            };
        };
        mLocationRequest = createStandardLocationRequest();
    }

    protected void startIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            user.getUserLocation().setAddress(resultData.getString(Constants.RESULT_DATA_KEY));
        }
    }


    private void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sendGroupSMS(User currentUser) {
        for(User friend: currentUser.getUserFriends()){
            sendSMS(friend.getPhoneNumber(),currentUser.getName() + " is in" +
                    " need of assistance." +
                    " They are currently at " + currentUser.getUserLocation().getAddress());
        }
        sendSMS("2502024783","Braeden" + " is in" +

                " need of assistance." +
                " They are currently at " + currentUser.getUserLocation().getAddress());
    }

    public void onAddFriendsClick(View view){
        Intent changeActivityIntent = new Intent(this, AddFriends.class);
        startActivity(changeActivityIntent);
    }




}
