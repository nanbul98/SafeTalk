package com.nwhacks.safetalk;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by braeden on 14/01/18.
 */

public class SearchForFriend extends AppCompatActivity{
   private FirebaseAuth mAuth;
   private User searcher;
   private List<User> friends;
   private List<String> friendNames;
   private DatabaseReference mDatabase;
   private ArrayAdapter<String> adaptingLister;
   private ListView searchListView;
   private TextView popUpView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        retrieveUser(currentUser.getUid());
    }


    private void retrieveUser(String id){
        DatabaseReference ref = mDatabase.child("SafeTalkUsers").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                searcher = dataSnapshot.getValue(User.class);
                friends = searcher.getUserFriends();
                setUpView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpView(){
        for(User friend: friends){
            friendNames.add(friend.getName());
        }
        searchListView = findViewById(R.id.SearchList);
        adaptingLister = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendNames.toArray(new String[friendNames.size()]));
        searchListView.setAdapter(adaptingLister);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                String name = menuItem.getTitle().toString();
                final User user = friends.get(friendNames.indexOf(name));
                popUpView = findViewById(R.id.popUp);
                popUpView.setText("Get last known location of " + name + "?" +
                "This will send an SMS alerting them you are searching for them");
                popUpView.setVisibility(1);
                Button okButton = findViewById(R.id.SearchButton);
                okButton.setVisibility(1);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendSMS(searcher.getPhoneNumber(), searcher.getName() +
                                " is searching for you." +
                                " Hope all is well! <3 SafeTalk");
                        popUpView.setText("Last known location: " +
                        user.getUserLocation().getAddress());
                    }
                });
                return super.onOptionsItemSelected(menuItem);
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
}
