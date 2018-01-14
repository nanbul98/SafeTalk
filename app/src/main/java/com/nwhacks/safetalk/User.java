package com.nwhacks.safetalk;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private FirebaseUser user;
    private String userId;
    private Set<User> userFriends;
    private String phoneNumber;
    private Location userLocation;


    /**
     * Purpose: To create a new user object
     * @param user contains all Firebase info on the current user
     * @param userFriends contains all trusted friends of the current user
     * @param phoneNumber contains the phone number of the current user for texting
     * @param userLocation contains the user's current location
     */
    public User(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = UUID.randomUUID().toString();
        mDatabase.child("users").child(userId).setValue(user);
        this.userId = userId;
        this.userFriends = new HashSet<User>();
        this.phoneNumber = new String();
        this.userLocation = new Location("");
    }

    public User(FirebaseUser user, Set<User> userFriends, String phoneNumber, Location userLocation) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = UUID.randomUUID().toString();
        this.user = user;
        this.userId = userId;
        this.userFriends = userFriends;
        this.phoneNumber = phoneNumber;
        this.userLocation = userLocation;
        mDatabase.child("users").child(userId).setValue(this);
    }

    public FirebaseUser getUser() {
        return user;
    }

    public Set<User> getUserFriends() {
        return userFriends;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserId() {return userId; }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public void addUserFriend(User friend) {
        this.userFriends.add(friend);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Location getUserLocation() { return userLocation; }

    public void setUserLocation(Location userLocation) { this.userLocation = userLocation; }
}
