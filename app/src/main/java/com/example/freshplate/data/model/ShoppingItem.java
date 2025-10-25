package com.example.freshplate.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "shopping_list_items")
public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    // (!! 关键) 用于跟踪复选框的状态 [cite: 30]
    public boolean isBought;

    public ShoppingItem(String trim, boolean b) {
        this.name = trim;
        this.isBought = b;
    }
}
