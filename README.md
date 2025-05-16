# Skin

一个轻量级的 Android 动态换肤库，支持动态加载皮肤包，实现应用主题的实时切换。

## 特性

- 支持动态加载皮肤包
- 支持多种主题切换（默认、蓝色、红色、绿色等）
- 支持自定义皮肤包
- 支持实时预览
- 支持保存皮肤设置
- 支持 AndroidX
- 支持 Material Design 组件

## 效果展示

[docs/sample.mp4](docs/sample.mp4)

## 快速开始

### 1. 添加依赖

在项目根目录的 `settings.gradle.kts` 中添加模块：

```kotlin
include(":skin")
```

在应用模块的 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation(project(":skin"))
}
```

### 2. 初始化

在 Application 类中初始化：

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}
```

### 3. 使用示例

```kotlin
// 切换皮肤
SkinManager.get().changeSkin(skin)

// 预览皮肤
SkinManager.get().previewSkin(skin)

// 读取已切换的皮肤并更新（第一次启动使用）
SkinManager.get().updateSkin(activity)
```

## 皮肤包制作

### 1. 创建皮肤包模块

1. 在项目中创建新的 Android Library 模块，例如 `skin-blue`
2. 在模块的 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation(project(":skin"))
}
```

### 2. 配置皮肤资源

在皮肤包模块中创建以下资源文件：

1. 颜色资源 (`res/values/colors.xml`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="colorPrimary">#2196F3</color>
    <color name="colorPrimaryDark">#1976D2</color>
    <color name="colorAccent">#03A9F4</color>
    <!-- 添加其他颜色资源 -->
</resources>
```

2. 主题资源 (`res/values/themes.xml`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.Skin" parent="Theme.MaterialComponents.Light.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <!-- 添加其他主题属性 -->
    </style>
</resources>
```

3. 图片资源：
- 将需要替换的图片资源放在 `res/drawable` 目录下
- 图片资源名称必须与主应用中的资源名称相同

### 3. 构建皮肤包

1. 在皮肤包模块的 `build.gradle.kts` 中配置构建类型：

```kotlin
android {
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

2. 执行构建命令生成 APK：
```bash
./gradlew :skin-blue:assembleRelease
```

3. 皮肤包 APK 将生成在 `skin-blue/build/outputs/apk/release/` 目录下

### 4. 使用皮肤包

1. 将生成的 APK 文件复制到应用的 assets 目录或下载到本地存储
2. 使用 SkinManager 加载皮肤包：

```kotlin
// 从 assets 加载
val skin = SkinManager.get().loadSkinFromAssets("skin-blue.apk")

// 从文件加载
val skin = SkinManager.get().loadSkinFromFile(File(context.getExternalFilesDir(null), "skin-blue.apk"))

// 应用皮肤
SkinManager.get().changeSkin(skin)
```

## 皮肤包配置

皮肤包是一个独立的 APK 文件，包含以下资源：

- colors.xml：颜色资源
- themes.xml：主题资源
- drawable：图片资源
- 其他自定义资源

### 创建皮肤包

1. 创建新的 Android Library 模块
2. 添加需要的资源文件
3. 构建 APK 文件

## 示例代码

```kotlin
// 设置默认皮肤
findViewById<MaterialButton>(R.id.btnResetSkin).setOnClickListener {
    loadSkin(SkinConfiguration.skinDefault)
}

// 设置蓝色皮肤
findViewById<MaterialButton>(R.id.btnBlueSkin).setOnClickListener {
    loadSkin(SkinConfiguration.skinBlue)
}

// 设置红色皮肤
findViewById<MaterialButton>(R.id.btnRedSkin).setOnClickListener {
    loadSkin(SkinConfiguration.skinRed)
}

// 设置绿色皮肤
findViewById<MaterialButton>(R.id.btnGreenSkin).setOnClickListener {
    loadSkin(SkinConfiguration.skinGreen)
}
```

## 支持的属性

库支持以下属性的动态换肤：

- background
- src
- textColor
- textColorHint
- drawableStart/Top/End/Bottom
- backgroundTint
- tint
- buttonTint
- trackTint
- thumbTint
- strokeColor
- rippleColor
- boxStrokeColor
- boxBackgroundColor
- indicatorColor
- trackColor
- cursorColor
- statusBarColor
- navigationBarColor

## 注意事项

1. 确保在 Application 中初始化 SkinManager
2. 皮肤包必须包含完整的资源文件
3. 皮肤包的包名必须与应用不同
4. 建议在切换皮肤时添加加载动画
5. 皮肤包中的资源名称必须与主应用中的资源名称保持一致
6. 皮肤包 APK 必须签名，建议使用与主应用相同的签名配置

## 许可证

Apache License 2.0

## 贡献

欢迎提交 Issue 和 Pull Request

## 作者

[wkbin](https://github.com/wkbin)
