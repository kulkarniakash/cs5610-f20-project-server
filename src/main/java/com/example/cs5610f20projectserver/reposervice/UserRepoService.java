package com.example.cs5610f20projectserver.reposervice;

import com.example.cs5610f20projectserver.Model.User;
import com.example.cs5610f20projectserver.repositories.UserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public class UserRepoService {

    private UserRepository repository;

    public UserRepoService() {

    }

    public UserRepoService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(String id, User user) {
        user.setId(id);
        return this.repository.save(user);
    }

    public User updateUser(User user) {
        return this.repository.save(user);
    }

    public String findUserByUsername(String username) {
        return this.repository.findUserByUsername(username);
    }

    public User findUserBySpotifyId(String uid) {return this.repository.findUserBySpotifyId(uid);}

    public List<User> findAllUsers() {
        return (List<User>) this.repository.findAll();
    }
}
