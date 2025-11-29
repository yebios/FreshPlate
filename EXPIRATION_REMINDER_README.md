# Pantry Item 过期提醒功能 - 使用指南

## 功能概述
FreshPlate 现在已经集成了智能过期提醒功能，可以自动检测即将过期的食材并通过系统通知提醒您。

---

## ✅ 已实现的功能

### 1. **自动定时检查**
- 应用启动后，WorkManager 会自动安排每 24 小时运行一次过期检查
- 检查即将在 3 天内过期的食材
- 后台运行，不影响应用性能

### 2. **通知提醒**
- 当发现即将过期的食材时，发送系统通知
- 通知内容包含食材名称和数量
- 点击通知可直接打开应用
- 支持 Android 8.0+ 通知渠道
- 包含震动反馈

### 3. **权限管理**
- Android 13+ 自动请求通知权限
- 用户可以选择允许或拒绝
- 权限状态有友好的 Toast 提示

### 4. **测试功能**
- 在 Pantry 页面的菜单中添加了"Test Expiration Check"选项
- 点击可立即触发一次过期检查（无需等待 24 小时）
- 方便开发和调试

---

## 📝 技术实现细节

### 核心组件

1. **ExpirationWorker.java**
   - 继承自 Worker，执行后台检查任务
   - 查询数据库中即将过期的食材
   - 构建并发送系统通知
   - 包含完整的日志记录

2. **FreshPlateApplication.java**
   - 在应用启动时初始化 WorkManager
   - 使用 PeriodicWorkRequest 创建周期性任务
   - 设置电池优化约束

3. **ExpirationTestHelper.java**
   - 提供手动触发检查的工具方法
   - 支持查看 Worker 状态
   - 支持取消所有任务

4. **PantryItemDao.java**
   - 新增 `getItemsExpiringBetween()` 方法
   - 根据日期范围查询食材

5. **MainActivity.java**
   - 处理 Android 13+ 的通知权限请求
   - 显示权限结果提示

---

## 🧪 如何测试

### 方法 1: 使用测试菜单（推荐）

1. 打开应用，进入 **Pantry** 页面
2. 点击右上角的菜单按钮（三个点）
3. 选择 **"Test Expiration Check"**
4. 立即触发检查，如果有即将过期的食材，会马上收到通知

### 方法 2: 添加测试数据

1. 进入 Pantry 页面
2. 添加一些食材，设置过期日期为：
   - 今天
   - 明天
   - 后天
   - 3 天后
3. 使用测试菜单触发检查
4. 查看通知内容

### 方法 3: 等待自动检查

- 应用启动后，首次检查会在 **1 分钟后**运行
- 之后每 **24 小时**运行一次
- 查看 Logcat 可以看到详细日志：
  ```
  Tag: ExpirationWorker
  Tag: FreshPlateApp
  ```

---

## 🔔 通知渠道配置

- **渠道 ID**: `EXPIRATION_ALERTS`
- **渠道名称**: "Expiration Alerts"
- **重要性**: HIGH（会显示在通知栏顶部）
- **震动模式**: 500ms - 200ms - 500ms
- **可自定义**: 用户可以在系统设置中修改通知行为

---

## 📱 权限说明

### 需要的权限

1. **POST_NOTIFICATIONS** (Android 13+)
   - 用于发送系统通知
   - 首次启动应用时会自动请求
   - 已在 AndroidManifest.xml 中声明

2. **WAKE_LOCK** (自动)
   - WorkManager 自动处理
   - 用于在设备休眠时执行任务

---

## 🐛 调试技巧

### 查看 WorkManager 状态

在 Android Studio 中打开 **App Inspection > WorkManager**：
- 查看所有已调度的任务
- 查看任务执行历史
- 手动运行任务

### 查看日志

在 Logcat 中过滤以下 TAG：
```
ExpirationWorker
FreshPlateApp
```

### 常见问题排查

1. **没有收到通知？**
   - 检查是否授予了通知权限
   - 检查 Pantry 中是否有即将过期的食材（3天内）
   - 查看 Logcat 确认 Worker 是否运行

2. **Worker 没有运行？**
   - 确认应用已启动过一次
   - 检查电池优化设置（某些设备可能限制后台任务）
   - 使用测试菜单手动触发

3. **通知没有显示食材名称？**
   - 检查数据库中的数据
   - 查看 Logcat 中的 ExpirationWorker 日志

---

## 🚀 未来改进方向

可以考虑添加以下功能：

1. **自定义提醒时间**
   - 让用户选择提前几天提醒
   - 自定义每天的提醒时间

2. **通知分组**
   - 按类别分组显示食材
   - 批量操作（标记为已用完等）

3. **提醒频率控制**
   - 每天多次检查
   - 或每周检查

4. **智能提醒**
   - 根据用户使用习惯调整提醒
   - 临近过期时增加提醒频率

---

## 📋 文件清单

### 新增文件
- `ExpirationWorker.java` - Worker 实现
- `ExpirationTestHelper.java` - 测试工具类
- `ic_notification.xml` - 通知图标
- `pantry_menu.xml` - Pantry 菜单

### 修改文件
- `FreshPlateApplication.java` - 初始化 WorkManager
- `MainActivity.java` - 请求通知权限
- `PantryFragment.java` - 添加测试菜单
- `PantryItemDao.java` - 添加日期查询方法
- `build.gradle` - 升级 compileSdk 到 34，添加 WorkManager 依赖
- `AndroidManifest.xml` - 添加通知权限声明

---

## ✨ 总结

过期提醒功能现已完全实现并可以正常工作！

**核心特性：**
- ✅ 自动后台检查
- ✅ 系统通知提醒
- ✅ 权限管理
- ✅ 手动测试功能
- ✅ 完整的日志记录
- ✅ 电池优化

**测试方法：**
1. 使用 Pantry 菜单中的"Test Expiration Check"立即测试
2. 添加即将过期的食材，触发检查
3. 查看 Logcat 日志确认运行状态

如有任何问题，请查看日志或使用测试菜单进行调试！

