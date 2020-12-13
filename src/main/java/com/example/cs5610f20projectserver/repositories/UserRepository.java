package com.example.cs5610f20projectserver.repositories;

import com.example.cs5610f20projectserver.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT u.id FROM User u where u.username = :username")
    public String findUserByUsername(@Param("username")String username);

    @Query("SELECT u FROM User u where u.id = :uid")
    public User findUserBySpotifyId(@Param("uid")String uid);
}
