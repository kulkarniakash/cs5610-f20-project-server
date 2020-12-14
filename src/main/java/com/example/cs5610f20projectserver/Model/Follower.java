package com.example.cs5610f20projectserver.Model;

import javax.persistence.*;

@Entity
@Table(name = "followers")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "followee")
    private User followee;
    @ManyToOne
    @JoinColumn(name = "follower")
    private User follower;

    public Follower() {

    }

    public Follower(User followee, User follower) {
        this.followee = followee;
        this.follower = follower;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
