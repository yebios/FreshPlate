# 🍽️ FreshPlate

<div align="center">

![FreshPlate Logo](app/src/main/res/mipmap-anydpi-v26/dish.png)

**智能厨房管理，化繁为简**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=26)
[![Version](https://img.shields.io/badge/Version-1.1-blue.svg)](https://github.com/yourusername/FreshPlate)
[![License](https://img.shields.io/badge/License-MIT-orange.svg)](LICENSE)

*您的私人厨房助手，减少食物浪费，发现美味食谱*

[功能特性](#-功能特性) • [下载](#-下载) • [截图展示](#-截图展示) • [技术栈](#-技术栈) • [快速开始](#-快速开始)

</div>

---

## 📖 关于项目

**FreshPlate** 是一款智能 Android 应用，旨在帮助您管理厨房储藏室、跟踪食物保质期、根据现有食材发现食谱，并维护有条理的购物清单。告别食物浪费，迎接高效的膳食规划！

### 🎯 核心亮点

- 📦 **智能储藏室管理** - 跟踪所有食材及其保质期
- 🔔 **过期提醒** - 在食物过期前收到通知
- 📷 **条码扫描** - 扫描商品条码快速添加物品
- 🍳 **食谱发现** - 根据现有食材寻找食谱
- 🛒 **购物清单** - 直接从食谱添加食材到清单
- 🎨 **现代 iOS 风格界面** - 精美的液态玻璃磨砂设计

---

## ✨ 功能特性

### 🗄️ 储藏室管理
- 添加、编辑和删除储藏室物品
- 跟踪数量和保质期
- 物品新鲜度可视化指示器（新鲜/即将过期/已过期）
- 搜索和筛选储藏室物品
- 条码扫描快速录入，通过 [OpenFoodFacts API](https://world.openfoodfacts.org/) 自动查询产品名称

### ⏰ 智能过期提醒
- 每日自动后台检查 3 天内即将过期的物品
- 推送通知显示详细的过期警告
- 一键测试功能，立即检查过期情况
- 使用 WorkManager 实现电池优化的后台处理
- 支持 Android 13+ 通知权限

### 🍲 食谱集成
- 浏览来自 [Spoonacular API](https://spoonacular.com/) 的精选食谱
- 查看详细的烹饪说明和分步指导
- 显示食材列表及用量
- 一键将食谱食材添加到购物清单
- 精美的食谱图片和分类

### 🛍️ 购物清单
- 维护持久化购物清单
- 手动添加或从食谱添加物品
- 标记已购买物品，带有流畅动画
- 滑动手势删除不需要的物品
- 简洁有序的界面

### 🎨 现代化 UI/UX
- iOS 风格的液态玻璃磨砂设计
- 流畅的动画和过渡效果
- Material Design 3 组件
- 直观的底部导航栏
- 适配所有屏幕尺寸的响应式布局

---

## 📥 下载

### 最新版本
**版本 1.1** - [下载 APK](app/release/FreshPlate.apk)

### 系统要求
- Android 8.0 (API 26) 或更高版本
- 约 20 MB 存储空间
- 浏览食谱和条码查询需要互联网连接

---

## 📱 截图展示

<img src="img/Pantry.png" alt="Pantry" style="zoom: 25%;" />

<img src="img/Add item.png" alt="Add item" style="zoom: 25%;" />

<img src="img/Recipes.png" alt="Recipes" style="zoom: 25%;" />

<img src="img/ShoppingList.png" alt="ShoppingList" style="zoom: 25%;" />

<img src="img/Notice.png" alt="Notice" style="zoom: 25%;" />


---

## 🛠️ 技术栈

### 架构
- **架构模式**: MVVM (Model-View-ViewModel)
- **开发语言**: Java
- **最低 SDK**: API 26 (Android 8.0)
- **目标 SDK**: API 34 (Android 14)

### 主要库和框架

#### 核心 Android
- **AndroidX** - 现代 Android 开发组件
- **Material Design 3** - Google 最新设计系统
- **Navigation Component** - Fragment 导航与 SafeArgs
- **LiveData & ViewModel** - 生命周期感知的数据管理
- **Data Binding** - 声明式 UI 绑定

#### 数据库
- **Room** - SQLite 抽象层，用于本地数据持久化
  - `PantryItem` - 食材物品及保质期跟踪
  - `ShoppingItem` - 购物清单管理
  - `Recipe` - 缓存的食谱数据

#### 网络请求
- **Retrofit** - 类型安全的 HTTP 客户端
- **Gson** - JSON 序列化/反序列化
- **OkHttp** - 带拦截器的 HTTP 客户端

#### 后台处理
- **WorkManager** - 可延迟的可靠后台任务
  - 每日过期检查
  - 电池优化调度

#### UI 与视觉效果
- **RecyclerView** - 高效的列表渲染
- **BlurView** (Dimezis) - iOS 风格磨砂玻璃效果
- **Material Components** - 底部表单、对话框、浮动按钮

#### 条码扫描
- **ML Kit Barcode Scanning** - Google ML 驱动的条码扫描器
- **CameraX** - 现代相机 API

#### 工具库
- **Core Library Desugaring** - 在旧版 Android 上支持 Java 8+ API
- **ThreeTenABP** - 现代日期/时间 API 回退 (java.time)

### 外部 API
- **[Spoonacular API](https://spoonacular.com/food-api)** - 食谱数据和图片
- **[OpenFoodFacts API](https://world.openfoodfacts.org/data)** - 条码产品信息

---

## 🚀 快速开始

### 普通用户

1. **下载** [发布版本](app/release/FreshPlate.apk)中的 APK 文件
2. **启用**"允许安装未知来源应用"（在 Android 设置中）
3. **安装** APK 到您的设备上
4. **授予**通知权限以接收过期提醒
5. **开始**像专业人士一样管理您的厨房！

### 开发者

#### 前置要求
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 11 或更高版本
- Android SDK 34
- Gradle 7.5+

#### 设置步骤

1. **克隆仓库**
```bash
git clone https://github.com/yourusername/FreshPlate.git
cd FreshPlate
```

2. **在 Android Studio 中打开**
   - 打开 Android Studio
   - 选择"Open an existing project"
   - 导航到克隆的目录

3. **同步 Gradle**
   - Android Studio 将自动同步 Gradle 依赖
   - 等待构建完成

4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击"Run"按钮或按 `Shift + F10`

#### 构建配置

```groovy
android {
    compileSdk 34
    
    defaultConfig {
        applicationId "com.example.freshplate"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.1"
    }
}
```

---

## 📂 项目结构

```
FreshPlate/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/freshplate/
│   │       │   ├── data/
│   │       │   │   ├── local/          # Room 数据库、DAO、实体
│   │       │   │   ├── remote/         # API 服务、模型
│   │       │   │   └── repository/     # 数据仓库
│   │       │   ├── ui/
│   │       │   │   ├── additem/        # 添加储藏室物品界面
│   │       │   │   ├── pantry/         # 储藏室列表界面
│   │       │   │   ├── recipes/        # 食谱浏览和详情
│   │       │   │   └── shopping/       # 购物清单界面
│   │       │   ├── util/               # 工具类
│   │       │   ├── worker/             # 后台 Worker
│   │       │   ├── MainActivity.java
│   │       │   └── FreshPlateApplication.java
│   │       └── res/
│   │           ├── layout/             # XML 布局
│   │           ├── drawable/           # 图片和图标
│   │           ├── navigation/         # 导航图
│   │           └── values/             # 字符串、颜色、主题
│   └── build.gradle
├── gradle/
├── build.gradle
└── settings.gradle
```

---

## 🔧 配置说明

### API 配置

应用使用两个外部 API：

1. **Spoonacular API**
   - Base URL: `https://spoonacular.com/food-api/`
   - 需要API Key

2. **OpenFoodFacts API**（开放数据库）
   - Base URL: `https://world.openfoodfacts.org/`
   - 需要 User-Agent 请求头（已自动添加）

### 通知配置

过期提醒每日运行。若要修改计划，请编辑：

```java
// FreshPlateApplication.java
PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
    ExpirationWorker.class,
    24, TimeUnit.HOURS // 在此更改间隔
    // 1, TimeUnit.DAYS
).build();
```

---

## 🧪 测试

### 手动测试

应用内置了过期提醒的测试功能：

1. 打开**储藏室**界面
2. 点击右上角的**菜单图标**（⋮）
3. 选择**"Test Expiration Check"**
4. 如果有 3 天内过期的物品，将立即显示通知

### 测试数据

要测试通知功能，请添加保质期为今天起 1-3 天的储藏室物品。

---

## 🤝 贡献指南

欢迎贡献！您可以通过以下方式提供帮助：

1. **Fork** 本仓库
2. **创建**功能分支 (`git checkout -b feature/AmazingFeature`)
3. **提交**您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. **推送**到分支 (`git push origin feature/AmazingFeature`)
5. **打开** Pull Request

### 开发指南

- 遵循标准 Java 代码规范
- 使用有意义的变量和方法名
- 为复杂逻辑添加注释
- 在多个 Android 版本上测试您的更改
- 根据需要更新文档

---

## 🐛 已知问题

- BlurView 依赖需要 JitPack 仓库（已配置）
- 部分 UI 元素可能需要针对平板设备进行调整
- 食谱图片需要互联网连接

---

## 📜 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

---

## 👨‍💻 作者

**FreshPlate 团队**

- GitHub: [@yebios](https://github.com/yebios)

---

## 🙏 致谢

- [Spoonacular API](https://spoonacular.com/) - 食谱 API
- [OpenFoodFacts](https://world.openfoodfacts.org/) - 开放食品产品数据库
- [Material Design](https://material.io/) - 设计系统和组件
- [BlurView by Dimezis](https://github.com/Dimezis/BlurView) - 磨砂玻璃效果
- 所有开源贡献者

---

## 📮 联系与支持

有问题或建议？欢迎：

- 在 GitHub 上**提交 Issue**
- 如果觉得有用，请为本仓库**加星标**！

---

<div align="center">

**用 ❤️ 制作，为了减少食物浪费**

⭐ 如果觉得有用，请为本仓库加星！

[English Version](README.md)

</div>

