package com.example.freshplate.data.model;

// 这个类代表 API 返回的完整食谱详情

import com.google.gson.annotations.SerializedName; // 使用 Gson 来自动解析 API 的 JSON 响应
import java.util.List;

import lombok.Getter;

@Getter
public class RecipeDetail {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("summary") // 描述
    private String description;

    @SerializedName("readyInMinutes")
    private int cookingTime; // 烹饪时间

    // (!! 关键) 完整的食材列表
    // 我们可以复用之前的 Ingredient.java
    @SerializedName("extendedIngredients")
    private List<Ingredient> ingredients;

    // (!! 关键) 烹饪步骤
    @SerializedName("analyzedInstructions")
    private List<AnalyzedInstruction> instructions;

    // --- Getters ---
    public String getCookingTimeString() {
        return cookingTime + " min";
    }

}