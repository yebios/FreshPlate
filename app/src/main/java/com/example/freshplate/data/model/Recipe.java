package com.example.freshplate.data.model;

// 使用 Gson 来自动解析 API 的 JSON 响应
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.List;

public class Recipe {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    // (!! 关键) complexSearch 在设置 addRecipeInformation=true 时会返回这些字段
    @SerializedName("summary")
    private String description; // 描述

    @SerializedName("readyInMinutes")
    private int cookingTime; // 烹饪时间 (分钟)

    // (!! 更改) complexSearch 返回的是这个字段
    @SerializedName("extendedIngredients")
    private List<Ingredient> ingredients;

    // (!! 瞬态字段) 这是一个非 API 字段，用于在 ViewModel 中进行排序
    // transient 关键字告诉 Gson 和 Room (如果使用) 忽略此字段
    private transient LocalDate soonestExpirationDate = LocalDate.MAX;

    // --- Getters ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public List<Ingredient> getIngredients() { return ingredients; }

    // (!! 关键) 添加回这两个 Getter
    public String getDescription() { return description; }
    public String getCookingTimeString() { return cookingTime + " min"; }

    // --- 用于排序的 Getter/Setter ---
    public void setSoonestExpirationDate(LocalDate date) {
        this.soonestExpirationDate = date;
    }
    public LocalDate getSoonestExpirationDate() {
        return this.soonestExpirationDate;
    }
}
