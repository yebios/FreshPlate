package com.example.freshplate.data.model;

// Spoonacular 将每个步骤表示为这个对象

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class Step {
    @SerializedName("number")
    private int number;

    @SerializedName("step")
    private String stepText;
}
