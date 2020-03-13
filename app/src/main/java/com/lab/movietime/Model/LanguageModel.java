package com.lab.movietime.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LanguageModel implements Serializable {
    @SerializedName("iso_639_1")
    private String id;
    @SerializedName("english_name")
    private String originalLanguage;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }
}
