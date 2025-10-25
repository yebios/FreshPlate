package com.example.freshplate.data.local;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.freshplate.data.model.PantryItem;

import java.util.List;

@Dao
public interface PantryItemDao {
    // (关键) 返回 LiveData，这样 UI 就可以自动更新
    @Query("SELECT * FROM pantry_items ORDER BY expirationDate ASC")
    LiveData<List<PantryItem>> getAllItems();

    // 插入新物品
    @Insert
    void insert(PantryItem item);


    // (你以后会需要)
    // @Update
    // void update(PantryItem item);

    // @Delete
    // void delete(PantryItem item);
}
