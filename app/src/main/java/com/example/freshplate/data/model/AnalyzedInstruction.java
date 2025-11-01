package com.example.freshplate.data.model;

// Spoonacular 将步骤包装在这个对象中

import com.google.gson.annotations.SerializedName;
import java.util.List;

import lombok.Getter;

@Getter
public class AnalyzedInstruction {
    @SerializedName("name")
    private String name;

    @SerializedName("steps")
    private List<Step> steps;
}
