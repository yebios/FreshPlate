package com.example.freshplate.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

// (你需要先创建 AppDatabase.java，这里假设你已经创建了)
import com.example.freshplate.data.local.AppDatabase;
import com.example.freshplate.data.local.PantryItemDao;
import com.example.freshplate.data.model.PantryItem;

public class PantryRepository {
    private PantryItemDao pantryItemDao;

    // 从 DAO 向 ViewModel 暴露 LiveData
    private LiveData<List<PantryItem>> allItems;

    public LiveData<List<PantryItem>> getAllItems() {
        return allItems;
    }

    // (使用 Application 来获取数据库实例)
    public PantryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pantryItemDao = db.pantryItemDao();
        allItems = pantryItemDao.getAllItems();
    }

    // 数据库操作必须在后台线程上执行
    public void insert(PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.insert(item);
        });
    }

    // update 和 delet
    public void update(PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.update(item);
        });
    }

    public void delete(PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.delete(item);
        });
    }

    public LiveData<PantryItem> getItemById(int itemId) {
        return pantryItemDao.getItemById(itemId);
    }

}
