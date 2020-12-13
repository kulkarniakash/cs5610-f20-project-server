package com.example.cs5610f20projectserver.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // private String author_id;
    private String post;
    private Date post_date;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public String getAuthor_id() {
        return author_id;
    }*/

    /*public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }*/

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    public Post(int id, String author_id, String post, Date post_date) {
        this.id = id;
        // this.author_id = author_id;
        this.post = post;
        this.post_date = post_date;
    }
}
