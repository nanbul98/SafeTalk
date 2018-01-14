package com.nwhacks.safetalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_search_for_friend);
        if(id != null) {
            retrieveUser(id);
        }else{
            Intent launchLoginPage = new Intent(SearchForFriend.this, loginActivity.class);
            startActivity(launchLoginPage);
        }
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
        if(friends.isEmpty()){
            popUpView = findViewById(R.id.popUp);
            popUpView.setText("You don't have any connected friends yet!");
            popUpView.setVisibility(View.VISIBLE);
            return;
        }
        adaptingLister = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendNames.toArray(new String[friendNames.size()]));
        searchListView.setAdapter(adaptingLister);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final User user = friends.get(i);
                popUpView = findViewById(R.id.popUp);
                popUpView.setText("Get last known location of " + user.getName() + "?" +
                        "This will send an SMS alerting them you are searching for them");
                popUpView.setVisibility(View.VISIBLE);
                Button okButton = findViewById(R.id.SearchButton);
                okButton.setVisibility(View.VISIBLE);
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
            }
        })
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
