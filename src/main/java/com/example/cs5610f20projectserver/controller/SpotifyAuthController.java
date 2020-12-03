package com.example.cs5610f20projectserver.controller;

// import com.example.cs5610f20projectserver.Model.AuthToken;
// import com.example.cs5610f20projectserver.Service.SpotifyServices;
import com.example.cs5610f20projectserver.Model.Post;
import com.example.cs5610f20projectserver.reposervice.PostRepoService;
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
    private UserRepoService userRepoService;
    private PostRepoService postRepoService;

    public SpotifyAuthController() {

    }

    @Autowired
    public SpotifyAuthController(UserRepository userRepository, PostRepository postRepository) {
        clientId = "08b8c56399c848388c05769966c722e2";
        clientSecret = "d08df511bc6749b0b374749fb4c25321";
        userRepoService = new UserRepoService(userRepository);
        postRepoService = new PostRepoService(postRepository);
    }

    @PostMapping("/authaccess")
    public @ResponseBody String getToken(@RequestParam("code") String code) throws IOException, InterruptedException {
        String encodedData = Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes(StandardCharsets.UTF_8));
        return SpotifyServices.getTokens(code, encodedData);
    }

    @PostMapping("/register")
    public @ResponseBody String getToken(@RequestParam("code") String code, @RequestBody User user) throws IOException, InterruptedException, JSONException {
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
            this.userRepoService.createUser(userId, user);
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("success", "user successfully registered");
            return jsonResp.toString();
        }
    }

    @GetMapping("/login/{uid}/{username}")
    public String verifyUsername(@PathVariable("uid") String uid, @PathVariable("username") String username) throws JSONException {
        if (username.equals(userRepoService.findUserBySpotifyId(uid))) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("success", "user verified");
            return jsonResp.toString();
        } else {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "user combination does not exist");
            return jsonResp.toString();
        }
    }

    @PostMapping("/posts")
    public @ResponseBody String createPost(@RequestParam("access_token") String accessToken, @RequestBody Post post) throws IOException, InterruptedException, JSONException {
        String userObject = SpotifyServices.getUserProfile(accessToken);
        JSONObject jsonObjectUserId = new JSONObject(userObject);

        if(jsonObjectUserId.has("error") || userRepoService.findUserBySpotifyId(jsonObjectUserId.getString("id")) == null ||
        !jsonObjectUserId.getString("id").equals(post.getAuthor_id())) {
            JSONObject jsonResp = new JSONObject("{}");
            jsonResp.put("error", "could not validate user when posting");
            return jsonResp.toString();
        }

        postRepoService.createPost(post);
        JSONObject jsonResp = new JSONObject("{}");
        jsonResp.put("success", "post successfully created");
        return jsonResp.toString();
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepoService.getAllPosts();
    }

}
