package com.example.freshplate.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(tableName = "shopping_list_items")
public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    // 用于跟踪复选框的状态
    public boolean isBought;

    // 自定义构造函数用于创建新项目（不包含 id）
    public ShoppingItem(String name, boolean isBought) {
        this.name = name;
        this.isBought = isBought;
    }
}
