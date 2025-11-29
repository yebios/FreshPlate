# 🧪 快速测试指南 - 过期提醒功能

## 立即测试步骤

### 1️⃣ 准备测试数据

打开应用，进入 **Pantry** 页面，添加以下测试数据：

```
食材 1: Milk
- Quantity: 1 L
- Expiration: 明天的日期
- Category: Dairy

食材 2: Eggs  
- Quantity: 12
- Expiration: 后天的日期
- Category: Dairy

食材 3: Bread
- Quantity: 1 loaf
- Expiration: 今天的日期
- Category: Grains
```

### 2️⃣ 授予通知权限

首次启动应用时：
- 系统会弹出通知权限请求
- 点击 **"Allow"** / **"允许"**
- 如果不小心拒绝了，可以在系统设置中手动开启

### 3️⃣ 手动触发测试

1. 在 **Pantry** 页面，点击右上角的 **⋮** (三个点菜单)
2. 选择 **"Test Expiration Check"**
3. 稍等 1-2 秒，应该会收到通知

### 4️⃣ 验证通知内容

通知应该显示：
```
标题: FreshPlate: 3 items expiring soon!
内容: Don't let them go to waste: Milk, Eggs, Bread
```

点击通知应该会打开应用。

### 5️⃣ 查看日志（可选）

在 Android Studio 的 Logcat 中过滤：
```
Tag: ExpirationWorker
```

你应该看到类似这样的日志：
```
D/ExpirationWorker: ExpirationWorker started
D/ExpirationWorker: Checking items expiring between 2025-11-29 and 2025-12-02
D/ExpirationWorker: Found 3 expiring items
D/ExpirationWorker: Notification posted with ID: 1001
D/ExpirationWorker: Notification sent successfully
```

---

## 📱 测试场景

### 场景 1: 没有即将过期的食材
- 清空所有即将过期的食材（或设置所有食材的过期日期为 7 天后）
- 触发测试
- **预期结果**: 不会收到通知，日志显示 "No items expiring soon"

### 场景 2: 只有 1 个即将过期的食材
- 只保留 1 个即将过期的食材
- 触发测试
- **预期结果**: 通知标题显示 "1 item expiring soon!"

### 场景 3: 超过 3 个即将过期的食材
- 添加 5 个即将过期的食材
- 触发测试
- **预期结果**: 通知内容显示前 3 个名字 + "and 2 more..."

### 场景 4: 通知权限被拒绝
- 在系统设置中关闭 FreshPlate 的通知权限
- 触发测试
- **预期结果**: 
  - 不会收到通知
  - 日志显示 "SecurityException: Missing notification permission"

---

## 🔍 故障排除

### 问题: 点击菜单没有反应
**解决方案**:
- 确保你在 **Pantry** 页面（不是其他页面）
- 检查菜单是否正确显示
- 查看 Logcat 是否有错误信息

### 问题: 没有收到通知
**检查清单**:
1. ✅ 是否授予了通知权限？
2. ✅ 是否有即将过期的食材（3天内）？
3. ✅ 手机是否开启了勿扰模式？
4. ✅ 是否在系统设置中关闭了 FreshPlate 的通知？

### 问题: 应用崩溃
**解决方案**:
1. 查看 Logcat 的错误堆栈
2. 清除应用数据并重新安装
3. 确保 Android 版本 >= 8.0 (API 26)

---

## ⏰ 自动检查测试

除了手动触发，系统会自动检查：

1. **首次启动后**: 1 分钟后运行首次检查
2. **之后**: 每 24 小时运行一次

**如何验证自动检查**:
- 等待 1 分钟，查看 Logcat
- 或者完全关闭并重启应用，等待 1 分钟

---

## 📊 成功标志

✅ **功能完全正常的标志**:
1. 可以手动触发检查
2. 有即将过期的食材时收到通知
3. 通知内容正确显示食材名称
4. 点击通知可以打开应用
5. 日志显示完整的执行流程
6. 没有崩溃或错误

---

## 🎉 完成！

如果以上测试都通过，说明过期提醒功能已经完美运行！

**下一步**:
- 在真实场景中使用
- 根据需要调整提醒时间（修改 FreshPlateApplication.java 中的时间参数）
- 自定义通知样式（修改 ExpirationWorker.java 中的通知构建代码）

