package com.example.cs5610f20projectserver.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SpotifyServices {
    static final String spotifyAccountURL = "https://accounts.spotify.com";
    static final String redirectURL = "https://immense-temple-17196.herokuapp.com/";
    static final String spotify_web_api_url = "https://api.spotify.com";
    // static final String redirectURL = "https://immense-temple-17196.herokuapp.com/";

    public static String getTokens(String code, String encodedData) throws IOException, InterruptedException {
        String uri = spotifyAccountURL + "/api/token/";
        String bodyData = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + URLEncoder.encode(redirectURL, StandardCharsets.UTF_8.toString()) ;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encodedData)
                .POST(BodyPublishers.ofString(bodyData))
                .build();
        HttpClient client = HttpClient.newBuilder().build();

        HttpResponse<?> response = client.send(request, BodyHandlers.ofString());
        return response.body().toString();
    }

    public static String getUserProfile(String access_token) throws IOException, InterruptedException {
        String uri = spotify_web_api_url + "/v1/me";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Authorization", "Bearer " + access_token)
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();

        HttpResponse<?> response  = client.send(request, BodyHandlers.ofString());
        return response.body().toString();
    }
}
