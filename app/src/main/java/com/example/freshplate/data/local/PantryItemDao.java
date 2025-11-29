package com.example.freshplate.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    // (!! 新增) 用于编辑功能，获取单个物品
    @Query("SELECT * FROM pantry_items WHERE id = :itemId")
    LiveData<PantryItem> getItemById(int itemId);

    //
     @Update
     void update(PantryItem item);

     @Delete
     void delete(PantryItem item);

    // (!! 新增) 查找在 today 和 threeDaysLater 之间过期的物品
    // 注意：这里返回 List<PantryItem> 而不是 LiveData
    // Room 会自动将 LocalDate 转换为 String 进行比较 (前提是你的 Converters 设置正确)
    @Query("SELECT * FROM pantry_items WHERE expirationDate >= :today AND expirationDate <= :threeDaysLater")
    List<PantryItem> getItemsExpiringBetween(String today, String threeDaysLater);
}
