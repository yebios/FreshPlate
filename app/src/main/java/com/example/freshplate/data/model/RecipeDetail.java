package com.example.freshplate.data.model;

// 这个类代表 API 返回的完整食谱详情

import com.google.gson.annotations.SerializedName;

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

    // 完整的食材列表
    @SerializedName("extendedIngredients")
    private List<Ingredient> ingredients;

    // 烹饪步骤
    @SerializedName("analyzedInstructions")
    private List<AnalyzedInstruction> instructions;

    // --- Getters ---
    public String getCookingTimeString() {
        return cookingTime + " min";
    }

}