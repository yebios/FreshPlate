package com.example.freshplate.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.data.model.ShoppingItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 应用程序的 Room 数据库主类。
 * * - entities: 列出所有需要被此数据库管理的实体类 (数据模型)
 * - version: 数据库版本号。每次你修改了表的结构，都必须增加这个数字。
 * - TypeConverters: 注册我们创建的 Converters.java 类，以便 Room 知道如何处理 Date 类型。
 */
@Database(entities = {PantryItem.class, ShoppingItem.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    // 1. DAO 接口
    public abstract PantryItemDao pantryItemDao();
    public abstract ShoppingItemDao shoppingItemDao();

    // 2. (单例) 创建一个 volatile 实例，确保所有线程都能看到最新的实例
    private static volatile AppDatabase INSTANCE;

    // 3. (单例) 创建一个固定大小的线程池，用于在后台执行所有数据库写入操作
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * (单例) 获取数据库实例的静态方法
     * * @param context 应用程序上下文
     *
     * @return 数据库的单例
     */
    public static AppDatabase getDatabase(final Context context) {
        // 第一次检查 (避免不必要的同步锁)
        if (INSTANCE == null) {
            // 同步锁，确保在多线程环境下也只有一个实例被创建
            synchronized (AppDatabase.class) {
                // 第二次检查 (在锁内部)
                if (INSTANCE == null) {
                    // 创建数据库实例
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "freshplate_database")
                            // 开发阶段：若 schema 改动导致版本不匹配，直接重建数据库以避免崩溃
                            .fallbackToDestructiveMigration()
                            // .addCallback(sRoomDatabaseCallback) // (可选) 添加回调
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}