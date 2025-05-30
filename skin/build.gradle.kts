plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrainsKotlinParcelize)
    `maven-publish`
}

android {
    namespace = "top.wkbin.skin"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.hiddenapibypass)
    implementation(libs.google.material)
}

tasks.register("makeJar", Copy::class) {
    val jarName = "skin"
    delete("build/libs/$jarName.jar")
    delete("build/intermediates/aar_main_jar")
    from("build/intermediates/aar_main_jar/release/syncReleaseLibJars/classes.jar")
    into("build/libs/")
    include("classes.jar")
    rename("classes.jar", "$jarName.jar")
    dependsOn("build")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "top.wkbin"
                artifactId = "skin"
                version = "1.0.0"

                from(components["release"])
            }
        }
        
        repositories {
            maven {
                name = "local"
                url = uri("${rootProject.projectDir}/maven-repo")
            }
        }
    }
}