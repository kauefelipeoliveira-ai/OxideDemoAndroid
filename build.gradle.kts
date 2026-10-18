plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.oxide.demo"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.oxide.demo"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0-demo"
    }
}
