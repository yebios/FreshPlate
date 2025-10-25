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

    // (!! 关键) Spoonacular 会告诉我们哪些食材匹配了
    @SerializedName("usedIngredients")
    private List<Ingredient> usedIngredients;

    // (!! 关键) 以及我们还缺哪些食材
    @SerializedName("missedIngredients")
    private List<Ingredient> missedIngredients;

    // (!! 瞬态字段) 这是一个非 API 字段，用于在 ViewModel 中进行排序
    // transient 关键字告诉 Gson 和 Room (如果使用) 忽略此字段
    private transient LocalDate soonestExpirationDate = LocalDate.MAX;

    // --- Getters ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public List<Ingredient> getUsedIngredients() { return usedIngredients; }
    public List<Ingredient> getMissedIngredients() { return missedIngredients; }

    // --- 用于排序的 Getter/Setter ---
    public void setSoonestExpirationDate(LocalDate date) {
        this.soonestExpirationDate = date;
    }
    public LocalDate getSoonestExpirationDate() {
        return this.soonestExpirationDate;
    }
}
