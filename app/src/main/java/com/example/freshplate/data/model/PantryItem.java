package com.example.freshplate.data.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "pantry_items")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int quantity;
    public LocalDate expirationDate;

    public PantryItem(String name, int quantity, LocalDate expirationDate) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }
}

