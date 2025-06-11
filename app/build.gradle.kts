plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 👇 Configure native build (OpenCV support)
        externalNativeBuild {
            cmake {
                // Pass flags to the C++ compiler
                cppFlags += listOf("-std=c++11", "-frtti", "-fexceptions")

                // Provide the path to OpenCV's CMake config
                arguments += listOf(
                    "-DOpenCV_DIR=${projectDir}/../sdk/OpenCV-android-sdk/sdk/native/jni"
                )
            }
        }

        // Optional: Limit to supported ABIs (better build speed)
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}