package com.example.freshplate.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// (你需要先创建 AppDatabase.java，这里假设你已经创建了)
import com.example.freshplate.data.local.AppDatabase;
import com.example.freshplate.data.local.PantryItemDao;
import com.example.freshplate.data.model.PantryItem;

public class PantryRepository {
    private PantryItemDao pantryItemDao;
    private LiveData<List<PantryItem>> allItems;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    // (使用 Application 来获取数据库实例)
    public PantryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pantryItemDao = db.pantryItemDao();
        allItems = pantryItemDao.getAllItems();
    }

    // 从 DAO 向 ViewModel 暴露 LiveData
    public LiveData<List<PantryItem>> getAllItems() {
        return allItems;
    }
    // 数据库操作必须在后台线程上执行
    public void insert(PantryItem item) {
        databaseWriteExecutor.execute(() -> {
            pantryItemDao.insert(item);
        });
    }
}
