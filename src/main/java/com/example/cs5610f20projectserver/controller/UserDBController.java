package com.example.cs5610f20projectserver.controller;

import com.example.cs5610f20projectserver.Model.User;
import com.example.cs5610f20projectserver.reposervice.UserRepoService;
import com.example.cs5610f20projectserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = {"https://immense-temple-17196.herokuapp.com", "http://localhost:3000"})

@RestController
@CrossOrigin(origins = "*")
public class UserDBController {

    private UserRepoService userRepoService;

    @Autowired
    public UserDBController(UserRepository rep) {
        this.userRepoService = new UserRepoService(rep);
    }

    @PostMapping("/createuser")
    public @ResponseBody String createUser(@RequestBody User user) {
        userRepoService.createUser(user.getId(), user);
        return "{received: 'true'}";
    }
}
