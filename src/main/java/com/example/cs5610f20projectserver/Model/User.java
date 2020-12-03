package com.example.cs5610f20projectserver.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    private String id;
    private String username;
    private String first_name;
    private String last_name;
    private boolean is_admin;

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String firstName) {
        this.first_name = firstName;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }

    public List getFollowers() {
        return followers;
    }

    public void setFollowers(List followers) {
        this.followers = followers;
    }

    public List getFollowing() {
        return following;
    }

    public void setFollowing(List following) {
        this.following = following;
    }

    @OneToMany (targetEntity=User.class)
    private List followers;
    @OneToMany (targetEntity = User.class)
    private List following;

    public User(String id, String username, String firstName, String lastName) {
        super();
        this.id = id;
        this.username = username;
        this.first_name = firstName;
        this.last_name = lastName;
    }

}
