package com.example.cs5610f20projectserver.controller;


import com.example.cs5610f20projectserver.Model.User;
import com.example.cs5610f20projectserver.reposervice.UserRepoService;
import com.example.cs5610f20projectserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//change origins later!
@RestController
//change to heroku link
@CrossOrigin(origins = {"https://immense-temple-17196.herokuapp.com", "http://localhost:3000"})
public class MCAuthController {

    private UserRepoService userRepoService;

    @Autowired
    public MCAuthController(UserRepository rep) {
        this.userRepoService = new UserRepoService(rep);
    }

    //Testing purposes only!
    @PostMapping("/create_user")
    public @ResponseBody String createUser(@RequestBody User user, @RequestBody String access_token) {
        userRepoService.createUser(user.getId(), user);
        return "{created: 'true'}";
    }

    @PostMapping("/mcauth/verify_username_unique")
    public @ResponseBody String usernameExists(@RequestBody String username) {
        if (userRepoService.findUserByUsername(username) == null) {
            return "{usernameExists: 'false'}";
        } else {
            return "{usernameExists: 'true'}";
        }
    }

}
