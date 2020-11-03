package com.example.cs5610f20projectserver.controller;

import com.example.cs5610f20projectserver.Model.AuthToken;
import com.example.cs5610f20projectserver.Service.SpotifyServices;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

//change origins later!
@RestController
//change to heroku link
@CrossOrigin(origins = "https://aqueous-brushlands-34349.herokuapp.com/")
public class SpotifyAuthController {
    private String clientId;
    private String clientSecret;

    public SpotifyAuthController() {
        clientId = "08b8c56399c848388c05769966c722e2";
        clientSecret = "d08df511bc6749b0b374749fb4c25321";
    }

    @PostMapping("/authaccess")
    public @ResponseBody String getToken(@RequestParam("code") String code) throws IOException, InterruptedException {
        String encodedData = Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes(StandardCharsets.UTF_8));
        return SpotifyServices.getTokens(code, encodedData);
    }
}
