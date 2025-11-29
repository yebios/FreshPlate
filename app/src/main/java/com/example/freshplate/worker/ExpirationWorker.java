package com.example.freshplate.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.freshplate.MainActivity;
import com.example.freshplate.R;
import com.example.freshplate.data.local.AppDatabase;
import com.example.freshplate.data.model.PantryItem;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExpirationWorker extends Worker {

    private static final String TAG = "ExpirationWorker";
    private static final String CHANNEL_ID = "EXPIRATION_ALERTS";
    private static final int NOTIFICATION_ID = 1001;

    public ExpirationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "ExpirationWorker started");

        try {
            // 1. 获取数据库实例
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

            // 2. 计算日期范围 (今天 ~ 3天后)
            LocalDate today = LocalDate.now();
            LocalDate threeDaysLater = today.plusDays(3);

            Log.d(TAG, "Checking items expiring between " + today + " and " + threeDaysLater);

            // 3. 查询数据库 (注意：我们需要将 LocalDate 转为 String，因为 Room Converter 是这样存的)
            List<PantryItem> expiringItems = db.pantryItemDao().getItemsExpiringBetween(
                    today.toString(),
                    threeDaysLater.toString()
            );

            Log.d(TAG, "Found " + (expiringItems != null ? expiringItems.size() : 0) + " expiring items");

            // 4. 发送通知
            if (expiringItems != null && !expiringItems.isEmpty()) {
                sendNotification(expiringItems);
                Log.d(TAG, "Notification sent successfully");
            } else {
                Log.d(TAG, "No items expiring soon");
            }

            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error in ExpirationWorker", e);
            return Result.failure();
        }
    }

    private void sendNotification(List<PantryItem> items) {
        Context context = getApplicationContext();

        // 1. 创建通知渠道 (Android 8.0+ 必需)
        createNotificationChannel(context);

        // 2. 构建通知内容
        String title;
        if (items.size() == 1) {
            title = "FreshPlate: 1 item expiring soon!";
        } else {
            title = "FreshPlate: " + items.size() + " items expiring soon!";
        }

        // 生成类似 "Milk, Eggs, Bread" 的字符串
        String contentText = items.stream()
                .map(item -> item.name)
                .limit(3) // 最多显示3个名字
                .collect(Collectors.joining(", "));

        if (items.size() > 3) {
            contentText += " and " + (items.size() - 3) + " more...";
        }

        // 3. 设置点击意图 (打开 MainActivity 并导航到 Pantry 页面)
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // 可以添加额外数据来指示打开特定页面
        intent.putExtra("navigate_to", "pantry");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 4. 构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText("Don't let them go to waste: " + contentText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Don't let them go to waste: " + contentText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 200, 500});

        // 5. 显示通知 (需要检查权限，但在 Worker 中我们通常假设如果没权限就不发)
        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
            Log.d(TAG, "Notification posted with ID: " + NOTIFICATION_ID);
        } catch (SecurityException e) {
            // Android 13+ 如果没有 POST_NOTIFICATIONS 权限会抛出异常
            Log.e(TAG, "SecurityException: Missing notification permission", e);
        }
    }

    private void createNotificationChannel(Context context) {
        // minSdk is 26, so we always have O+
        CharSequence name = "Expiration Alerts";
        String description = "Notifications for items expiring soon in your pantry";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{0, 500, 200, 500});

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "Notification channel created");
        }
    }
}
