package com.nwhacks.safetalk;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AddFriends extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private DatabaseReference mDatabase;
    private Toolbar search_toolbar;
    private ListView search_listView;
    private TextView hint_message;
    private String TAG = AddFriends.class.getSimpleName();
    private ArrayAdapter<String> adaptingLister;
    private List<User> allUsers;
    private List<String> allNames;
    private String names[] = {"Test", "another test", "oh"};
    private String locations[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        searchDatabaseForUsers();



    }

    //Want to find all users
    private void searchDatabaseForUsers() {
        //final boolean dataPullComplete[] = {false};
        allUsers = new ArrayList<User>();
        allNames = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final CountDownLatch waitLatch = new CountDownLatch(1);

        Query findUser = mDatabase.child("SafeTalkUsers").orderByChild("userId");
        findUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    allUsers.add(userSnapshot.getValue(User.class));
                    allNames.add(userSnapshot.getValue(User.class).getName());
                }
                initViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initViews() {
        search_toolbar = (Toolbar) findViewById(R.id.SearchBar);
        search_listView = (ListView) findViewById(R.id.SearchList);
        adaptingLister = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allNames.toArray(new String[allNames.size()]));
        search_listView.setAdapter(adaptingLister);

        hint_message = (TextView) findViewById(R.id.HintText);

        setSupportActionBar(search_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Making list invisible initially to keep UI clean
        search_listView.setAlpha(0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_SearchBar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Enter user's name here");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public boolean onQueryTextSubmit(String submitString) {
        return true;
    }

    public boolean onQueryTextChange(String changedString) {
        if (changedString.length() == 0) {
            search_listView.setAlpha(0);
            hint_message.setAlpha(1);
        } else {
            search_listView.setAlpha(1);
            hint_message.setAlpha(0);
            adaptingLister.getFilter().filter(changedString);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }


}
