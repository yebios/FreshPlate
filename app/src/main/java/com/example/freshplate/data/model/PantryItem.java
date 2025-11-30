package com.example.freshplate.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

import lombok.Data;

@Data
@Entity(tableName = "pantry_items")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int quantity;
    public LocalDate expirationDate;
    public boolean isIngredient;

    public PantryItem(String name, int quantity, LocalDate expirationDate, boolean isIngredient) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.isIngredient = isIngredient;
    }

}

