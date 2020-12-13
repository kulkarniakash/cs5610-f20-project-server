package com.example.cs5610f20projectserver.reposervice;

import com.example.cs5610f20projectserver.Model.Follower;
import com.example.cs5610f20projectserver.Model.User;
import com.example.cs5610f20projectserver.repositories.FollowerRepository;

import java.util.List;

public class FollowerRepoService {
    private FollowerRepository repository;

    public FollowerRepoService() {

    }

    public FollowerRepoService(FollowerRepository repo) {
        this.repository = repo;
    }

    public Follower createFollower(User followee, User follower) {
        return this.repository.save(new Follower(followee, follower));
    }

    public List<User> getFollowers(String uid) {
        return this.repository.getFollowers(uid);
    }

    public List<User> getFollowees(String uid) {
        return this.repository.getFollowees(uid);
    }
}
