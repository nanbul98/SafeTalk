package com.nwhacks.safetalk;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class User {
    private String userId;
    private String email;
    private List<User> userFriends;
    private String phoneNumber;
    private MyLocation userLocation;
    private String name;

    /**
     * Purpose: To create a new user object
     * @param user contains all Firebase info on the current user
     * @param userFriends contains all trusted friends of the current user
     * @param phoneNumber contains the phone number of the current user for texting
     * @param userLocation contains the user's current location
     */
    public User(){
        String userId = UUID.randomUUID().toString();
        this.userId = userId;
        this.userFriends = new ArrayList<>();
        this.phoneNumber = new String();
        this.userLocation = new MyLocation();
        this.email = "";
        this.name = "";
    }

    public User(FirebaseUser user, List<User> userFriends, String phoneNumber, Location userLocation, String name) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        this.userId = user.getUid();
        this.email = new String(user.getEmail());
        this.name = name;
        this.userFriends = userFriends;
        this.phoneNumber = phoneNumber;
        if(userLocation == null){
            this.userLocation = new MyLocation();
        }else {
            this.userLocation = new MyLocation(userLocation);
        }
        mDatabase.child("SafeTalkUsers").child(userId).setValue(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.name = name;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> nameUpdates = new HashMap<String, Object>();
        nameUpdates.put("email", email);
        mDatabase.updateChildren(nameUpdates);
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> nameUpdates = new HashMap<String, Object>();
        nameUpdates.put("name", name);
        mDatabase.updateChildren(nameUpdates);
    }

    public List<User> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<User> friends) {
        this.userFriends = friends;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> friendUpdates = new HashMap<String, Object>();
        friendUpdates.put("userFriends", friends);
        mDatabase.updateChildren(friendUpdates);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserId() {return userId; }

    public void addUserFriend(User friend) {
        this.userFriends.add(friend);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> friendUpdates = new HashMap<String, Object>();
        friendUpdates.put("userFriends", this.userFriends);
        mDatabase.updateChildren(friendUpdates);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> phoUpdates = new HashMap<String, Object>();
        phoUpdates.put("phoneNumber", phoneNumber);
        mDatabase.updateChildren(phoUpdates);
    }

    public MyLocation getUserLocation() { return userLocation; }

    public void setUserLocation(MyLocation userLocation) {
        this.userLocation = userLocation;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> locUpdates = new HashMap<String, Object>();
        locUpdates.put("userLocation", userLocation);
        mDatabase.updateChildren(locUpdates);
    }
}
