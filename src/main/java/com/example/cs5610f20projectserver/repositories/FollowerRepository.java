package com.example.cs5610f20projectserver.repositories;

import com.example.cs5610f20projectserver.Model.Follower;
import com.example.cs5610f20projectserver.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowerRepository extends CrudRepository<Follower, Integer> {

    @Query("SELECT f.follower FROM Follower f WHERE f.followee.id = :id")
    public List<User> getFollowers(@Param("id") String id);

    @Query("SELECT f.followee FROM Follower f WHERE f.follower.id = :id")
    public List<User> getFollowees(@Param("id") String id);
}
