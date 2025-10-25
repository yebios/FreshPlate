package com.example.freshplate.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * 代表一个食谱中的单一成分 (来自 Spoonacular API)
 */
@Getter
public class Ingredient {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
