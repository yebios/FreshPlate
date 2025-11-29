package com.example.freshplate;

import android.app.Application;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.freshplate.worker.ExpirationWorker;

import java.util.concurrent.TimeUnit;

public class FreshPlateApplication extends Application {

    private static final String TAG = "FreshPlateApp";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application created");
        setupExpirationWorker();
    }

    private void setupExpirationWorker() {
        // 设置约束条件：设备充电或电池电量不低时运行
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        // 创建一个周期性请求：每 24 小时运行一次
        // 注意：WorkManager 的最小重复间隔是 15 分钟
        PeriodicWorkRequest expirationWorkRequest =
                new PeriodicWorkRequest.Builder(ExpirationWorker.class, 24, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .setInitialDelay(1, TimeUnit.MINUTES) // 首次运行延迟 1 分钟，方便测试
                        .build();

        // 提交任务
        // 使用 KEEP 策略：如果任务已经存在，就不替换它（避免每次启动 app 都重置计时器）
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "DailyExpirationCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                expirationWorkRequest
        );

        Log.d(TAG, "Expiration worker scheduled successfully");
    }
}