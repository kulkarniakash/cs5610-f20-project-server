package com.example.cs5610f20projectserver.controller;

// import com.example.cs5610f20projectserver.Model.AuthToken;
// import com.example.cs5610f20projectserver.Service.SpotifyServices;
import com.example.cs5610f20projectserver.Model.Follower;
import com.example.cs5610f20projectserver.Model.Post;
import com.example.cs5610f20projectserver.reposervice.FollowerRepoService;
import com.example.cs5610f20projectserver.reposervice.PostRepoService;
import com.example.cs5610f20projectserver.repositories.FollowerRepository;
import com.example.cs5610f20projectserver.repositories.PostRepository;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.cs5610f20projectserver.Model.User;
import com.example.cs5610f20projectserver.Service.*;
import com.example.cs5610f20projectserver.reposervice.UserRepoService;
import com.example.cs5610f20projectserver.repositories.UserRepository;
import com.mysql.cj.xdevapi.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

//change origins later!
@RestController
//change to heroku link
@CrossOrigin(origins = {"https://immense-temple-17196.herokuapp.com", "http://localhost:3000"})
public class SpotifyAuthController {
    private String clientId;
    private String clientSecret;
    private String adminCode;
    private UserRepoService userRepoService;
    private PostRepoService postRepoService;
    private FollowerRepoService followerRepoService;

    public SpotifyAuthController() {

    }

    @Autowired
    public SpotifyAuthController(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository) {
        clientId = "08b8c56399c848388c05769966c722e2";
        clientSecret = "d08df511bc6749b0b374749fb4c25321";
        adminCode = "xMwzzJnhKPVD9QULoDKn";
        userRepoService = new UserRepoService(userRepository);
        postRepoService = new PostRepoService(postRepository);
        followerRepoService = new FollowerRepoService(followerRepository);
    }


    @PostMapping("/authaccess")
    public @ResponseBody String getToken(@RequestParam("code") String code) throws IOException, InterruptedException {
        String encodedData = Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes(StandardCharsets.UTF_8));
        return SpotifyServices.getTokens(code, encodedData);
    }

    @PostMapping("/register")
    public @ResponseBody String getToken(@RequestParam("code") String code, @RequestHeader("adminCode") String adminCode, @RequestBody User user) throws IOException, InterruptedException, JSONException {
        String encodedData = Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes(StandardCharsets.UTF_8));
        if(userRepoService.findUserByUsername(user.getUsername()) != null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error_username", "username already exists");
            return jsonResp.toString();
        }

        String jsonTokens = SpotifyServices.getTokens(code, encodedData);
        JSONObject jsonObjectToken = new JSONObject(jsonTokens);
        String accessToken = jsonObjectToken.getString("access_token");

        String userObject = SpotifyServices.getUserProfile(accessToken);
        JSONObject jsonObjectUserId = new JSONObject(userObject);
        String userId = jsonObjectUserId.getString("id");

        if(userRepoService.findUserBySpotifyId(userId) != null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error_spotify_id", "spotify id already registered");
            return jsonResp.toString();
        } else {
            if(this.adminCode.equals(adminCode)) {
                user.setIs_admin(true);
            }
            this.userRepoService.createUser(userId, user);
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("success", "user successfully registered");
            return jsonResp.toString();
        }
    }

    @GetMapping("/login/{uid}/{username}")
    public String verifyUsername(@PathVariable("uid") String uid, @PathVariable("username") String username) throws JSONException {
        if (username.equals(userRepoService.findUserBySpotifyId(uid).getUsername())) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("success", "user verified");
            return jsonResp.toString();
        } else {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "user combination does not exist");
            return jsonResp.toString();
        }
    }

    @PostMapping("/posts/{authorId}")
    public @ResponseBody String createPost(@RequestParam("access_token") String accessToken, @PathVariable("authorId") String authorId, @RequestBody Post post) throws IOException, InterruptedException, JSONException {
        String userObject = SpotifyServices.getUserProfile(accessToken);
        JSONObject jsonObjectUserId = new JSONObject(userObject);

        if(jsonObjectUserId.has("error") || userRepoService.findUserBySpotifyId(jsonObjectUserId.getString("id")) == null ||
        !jsonObjectUserId.getString("id").equals(authorId)) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "could not validate user when posting");
            return jsonResp.toString();
        }

        User user = userRepoService.findUserBySpotifyId(authorId);
        if (user == null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "could not validate user when posting");
            return jsonResp.toString();
        }

        post.setUser(user);
        postRepoService.createPost(post);
        // postRepoService.createPost(post);
        JSONObject jsonResp = new JSONObject("{}");
        jsonResp.put("success", "post successfully created");
        return jsonResp.toString();
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        List<Post> posts = postRepoService.getAllPosts();
        List<Follower> emptyFollower = new ArrayList<>();
        List<Post> emptyPosts = new ArrayList<>();
        for(Post post : posts) {
            post.getUser().setFollowees(emptyFollower);
            post.getUser().setFollowers(emptyFollower);
            post.getUser().setPosts(emptyPosts);
        }
        return postRepoService.getAllPosts();
    }

    @PostMapping("/followers")
    public @ResponseBody String follow(@RequestParam("access_token") String accessToken, @RequestBody User followee) throws IOException, InterruptedException, JSONException {
        String userObject = SpotifyServices.getUserProfile(accessToken);
        JSONObject jsonObjectUserId = new JSONObject(userObject);

        if(jsonObjectUserId.has("error") || userRepoService.findUserBySpotifyId(jsonObjectUserId.getString("id")) == null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "could not validate user when posting");
            return jsonResp.toString();
        }

        User user = userRepoService.findUserBySpotifyId(jsonObjectUserId.getString("id"));
        if (user == null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "could not validate user when posting");
            return jsonResp.toString();
        }

        if (userRepoService.findUserBySpotifyId(followee.getId()) == null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "could not validate followee when posting");
            return jsonResp.toString();
        }

        followerRepoService.createFollower(followee, user);

        JSONObject jsonResp = new JSONObject("{}");
        jsonResp.put("success", "follower relationship created");
        return jsonResp.toString();
    }

    @GetMapping("/find_followers/{uid}")
    public String getFollowers(@PathVariable("uid") String uid) throws JSONException {
        if (userRepoService.findUserBySpotifyId(uid) == null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "user not found");
            return jsonResp.toString();
        }

        List<User> followers = followerRepoService.getFollowers(uid);
        List<Follower> emptyList = new ArrayList<>();
        List<Post> emptyPosts = new ArrayList<>();
        for(User user : followers) {
            user.setFollowees(emptyList);
            user.setFollowers(emptyList);
            for(Post post : user.getPosts()) {
                post.getUser().setFollowers(emptyList);
                post.getUser().setFollowees(emptyList);
                post.getUser().setPosts(emptyPosts);
            }
        }
        JSONObject jsonResp = new JSONObject("{}");
        jsonResp.put("followers", followers);
        return jsonResp.toString();
    }

    @GetMapping("/find_followees/{uid}")
    public String getFollowees(@PathVariable("uid") String uid) throws JSONException {
        if (userRepoService.findUserBySpotifyId(uid) == null) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "user not found");
            return jsonResp.toString();
        }

        List<User> followees = followerRepoService.getFollowees(uid);
        // set followers, followees and posts to empty list to avoid circular referencing
        List<Follower> emptyList = new ArrayList<>();
        List<Post> emptyPosts = new ArrayList<>();
        for(User user : followees) {
            user.setFollowees(emptyList);
            user.setFollowers(emptyList);
            for(Post post : user.getPosts()) {
                post.getUser().setFollowers(emptyList);
                post.getUser().setFollowees(emptyList);
                post.getUser().setPosts(emptyPosts);
            }
        }
        System.out.println(followees.size());
        JSONObject jsonResp = new JSONObject("{}");
        jsonResp.put("followees", followees);
        return jsonResp.toString();
    }

}
