package com.example.freshplate.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.freshplate.worker.ExpirationWorker;

import java.util.concurrent.ExecutionException;

/**
 * 测试工具类，用于手动触发过期提醒检查
 */
public class ExpirationTestHelper {

    private static final String TAG = "ExpirationTestHelper";
    private static final String TEST_WORK_NAME = "TestExpirationCheck";

    /**
     * 立即触发一次过期检查（用于测试）
     */
    public static void triggerImmediateCheck(Context context) {
        Log.d(TAG, "Triggering immediate expiration check");

        // 创建一次性工作请求
        OneTimeWorkRequest testWorkRequest = new OneTimeWorkRequest.Builder(ExpirationWorker.class)
                .build();

        // 提交工作
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(testWorkRequest);

        Toast.makeText(context, "Checking for expiring items...", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Test work request enqueued with ID: " + testWorkRequest.getId());
    }

    /**
     * 检查 WorkManager 的状态
     */
    public static void checkWorkerStatus(Context context) {
        WorkManager workManager = WorkManager.getInstance(context);

        try {
            // 获取所有工作的状态
            workManager.getWorkInfosByTag("DailyExpirationCheck")
                    .get()
                    .forEach(workInfo -> {
                        Log.d(TAG, "Work ID: " + workInfo.getId());
                        Log.d(TAG, "Work State: " + workInfo.getState());
                        Log.d(TAG, "Work Tags: " + workInfo.getTags());
                    });
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Error checking worker status", e);
        }
    }

    /**
     * 取消所有过期检查任务
     */
    public static void cancelAllExpirationChecks(Context context) {
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.cancelUniqueWork("DailyExpirationCheck");
        Log.d(TAG, "All expiration checks cancelled");
        Toast.makeText(context, "Expiration checks cancelled", Toast.LENGTH_SHORT).show();
    }
}

