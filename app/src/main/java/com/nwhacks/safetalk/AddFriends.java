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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class AddFriends extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar search_toolbar;
    private ListView search_listView;
    private String TAG = AddFriends.class.getSimpleName();
    private ArrayAdapter<String> adaptingLister;
    private String names[] = {"Test", "another test", "oh"};
    private String locations[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        searchDatabaseForUsers();
        initViews();

    }

    //Want to find all users
    private void searchDatabaseForUsers(){

        //Set both names and locations
    }

    private void initViews(){
        search_toolbar = (Toolbar) findViewById(R.id.SearchBar);
        search_listView = (ListView) findViewById(R.id.SearchList);
        adaptingLister = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        search_listView.setAdapter(adaptingLister);
        setSupportActionBar(search_toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_SearchBar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Enter user's name here");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public boolean onQueryTextSubmit(String submitString){
        return true;
    }

    public boolean onQueryTextChange(String changedString){
        adaptingLister.getFilter().filter(changedString);
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


}
