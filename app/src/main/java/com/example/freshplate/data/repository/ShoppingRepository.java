package com.example.freshplate.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.freshplate.data.local.AppDatabase;
import com.example.freshplate.data.local.ShoppingItemDao;
import com.example.freshplate.data.model.ShoppingItem;

import java.util.List;

public class ShoppingRepository {

    private ShoppingItemDao shoppingItemDao;
    private LiveData<List<ShoppingItem>> allItems;

    public ShoppingRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        shoppingItemDao = db.shoppingItemDao();
        allItems = shoppingItemDao.getAllItems();
    }

    public LiveData<List<ShoppingItem>> getAllItems() {
        return allItems;
    }

    // 所有数据库写入操作都必须在后台线程上！
    public void insert(ShoppingItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            shoppingItemDao.insert(item);
        });
    }

    // 在 ShoppingRepository 中检查是否已存在
    public void insertIfNotExists(String name) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            ShoppingItem existing = shoppingItemDao.getByName(name);
            if (existing == null) {
                shoppingItemDao.insert(new ShoppingItem(name, false));
            }
        });
    }

    public void update(ShoppingItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            shoppingItemDao.update(item);
        });
    }

    public void delete(ShoppingItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            shoppingItemDao.delete(item);
        });
    }
}
