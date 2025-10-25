package com.example.freshplate.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.freshplate.data.model.ShoppingItem;

import java.util.List;

@Dao
public interface ShoppingItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ShoppingItem item);

    @Update
    void update(ShoppingItem item); // 用于更新 'isBought' 状态

    @Query("SELECT * FROM shopping_list_items ORDER BY id DESC")
    LiveData<List<ShoppingItem>> getAllItems();

    @Delete
    void delete(ShoppingItem item);
}
