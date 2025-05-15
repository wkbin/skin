// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrainsKotlinParcelize) apply false
}

// 定义打包皮肤包的任务
tasks.register("packageSkins") {
    group = "skin"
    description = "Package all skin modules into APKs and calculate MD5"

    dependsOn(":skins:skin_blue:assembleRelease")
    dependsOn(":skins:skin_red:assembleRelease")
    dependsOn(":skins:skin_green:assembleRelease")
    
    doLast {
        // 创建输出目录
        val outputDir = File(rootProject.projectDir, "build/skins")
        outputDir.mkdirs()
        
        // 复制所有皮肤包APK到输出目录并计算MD5
        val skinModules = listOf("skin_blue", "skin_red", "skin_green")
        skinModules.forEach { moduleName ->
            val apkFile = File(rootProject.projectDir, "skins/$moduleName/build/outputs/apk/release/$moduleName-release-unsigned.apk")
            if (apkFile.exists()) {
                val targetFile = File(outputDir, "$moduleName.apk")
                apkFile.copyTo(targetFile, overwrite = true)
                
                // 计算MD5
                val md5 = java.security.MessageDigest.getInstance("MD5")
                    .digest(targetFile.readBytes())
                    .joinToString("") { "%02x".format(it) }
                
                println("Packaged $moduleName to ${targetFile.absolutePath}")
                println("MD5: $md5")
            } else {
                println("Warning: Could not find APK for $moduleName at ${apkFile.absolutePath}")
            }
        }
        
        println("\nAll skin packages have been copied to: ${outputDir.absolutePath}")
    }
}

// 定义清理皮肤包的任务
tasks.register("cleanSkins") {
    group = "skin"
    description = "Clean all skin packages"
    
    doLast {
        val outputDir = File(rootProject.projectDir, "build/skins")
        if (outputDir.exists()) {
            outputDir.deleteRecursively()
            println("Cleaned skin packages directory")
        }
    }
}