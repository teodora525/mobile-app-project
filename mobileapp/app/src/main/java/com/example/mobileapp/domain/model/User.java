package com.example.mobileapp.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id; // Firebase Auth UID
    private String username;
    private String email;
    private String role; // "USER" ili "ADMIN"
    private String profileImageUrl;
    private String region;
    private String bio;
    private int recipeCount;
    private List<String> favoriteRecipeIds;

    public User() {
        this.favoriteRecipeIds = new ArrayList<>();
    }

    public User(String id, String username, String email, String role, String region) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.region = region;
        this.bio = "";
        this.profileImageUrl = "";
        this.recipeCount = 0;
        this.favoriteRecipeIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
    }

    public List<String> getFavoriteRecipeIds() {
        return favoriteRecipeIds;
    }

    public void setFavoriteRecipeIds(List<String> favoriteRecipeIds) {
        this.favoriteRecipeIds = favoriteRecipeIds;
    }
}
