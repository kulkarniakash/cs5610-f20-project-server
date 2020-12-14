package com.example.cs5610f20projectserver.reposervice;

import com.example.cs5610f20projectserver.Model.Post;
import com.example.cs5610f20projectserver.Model.User;
import com.example.cs5610f20projectserver.repositories.PostRepository;
import com.example.cs5610f20projectserver.repositories.UserRepository;

import java.util.Date;
import java.util.List;

public class PostRepoService {

    private PostRepository repository;

    public PostRepoService() {

    }

    public PostRepoService(PostRepository repository) {
        this.repository = repository;
    }

    public Post createPost(Post post) {
        post.setPost_date(new Date());
        return this.repository.save(post);
    }

    public List<Post> getAllPosts() {
        return (List<Post>) repository.findAll();
    }

    public Post getPostById(Integer id) { return repository.findPostById(id);}

    public void deletePostById(Integer id) {
        repository.deleteById(id);
    }

    public void updatePostById(Integer id, Post newPost) {
        repository.save(newPost);
    }
}
