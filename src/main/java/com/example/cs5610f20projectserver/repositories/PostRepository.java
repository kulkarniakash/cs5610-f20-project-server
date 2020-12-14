package com.example.cs5610f20projectserver.repositories;

import com.example.cs5610f20projectserver.Model.Post;
import com.example.cs5610f20projectserver.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends CrudRepository<Post, Integer> {
    @Query("SELECT p FROM Post p where p.id = :id")
    public Post findPostById(@Param("id")Integer id);
}
