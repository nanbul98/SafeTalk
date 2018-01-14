package com.nwhacks.safetalk;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;

import java.util.Set;

public class User {
    private FirebaseUser user;
    private Set<FirebaseUser> userFriends;
    private String phoneNumber;
    private Location userLocation;


    /**
     * Purpose: To create a new user object
     * @param user contains all Firebase info on the current user
     * @param userFriends contains all trusted friends of the current user
     * @param phoneNumber contains the phone number of the current user for texting
     * @param userLocation contains the user's current location
     */
    public User(FirebaseUser user, Set<FirebaseUser> userFriends, String phoneNumber, Location userLocation) {
        this.user = user;
        this.userFriends = userFriends;
        this.phoneNumber = phoneNumber;
        this.userLocation = userLocation;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public Set<FirebaseUser> getUserFriends() {
        return userFriends;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public void setUserFriends(Set<FirebaseUser> userFriends) {
        this.userFriends = userFriends;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Location getUserLocation() { return userLocation; }

    public void setUserLocation(Location userLocation) { this.userLocation = userLocation; }
}
