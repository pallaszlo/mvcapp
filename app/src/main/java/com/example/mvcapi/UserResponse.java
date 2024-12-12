package com.example.mvcapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("users")
    @Expose
    private List<User> users;

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}
