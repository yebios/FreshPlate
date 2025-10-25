package com.example.freshplate.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 包装来自 complexSearch API 调用的响应
 */
public class RecipeResponse {
    @SerializedName("results")
    private List<Recipe> recipes;

    @SerializedName("number")
    private int number;

    // ... (还有 offset, totalResults 等，但我们只需要 'results') ...

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
