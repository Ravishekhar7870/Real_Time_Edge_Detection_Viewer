# 👁️ Vision — Real-Time Edge Detection with OpenCV & OpenGL

**Vision** is a real-time Android app that captures live camera frames, processes them using native C++ (via OpenCV), and renders the result using OpenGL ES 2.0 for high-performance display. It supports front/back camera switching and shows both raw and processed views.

---

## ✅ Features

- 📸 Real-time camera feed using Camera2 API
- 🔄 Toggle between front and back cameras
- 🧠 Canny edge detection with Gaussian blur preprocessing (OpenCV)
- ⚙️ Native processing using C++ and JNI
- 🎮 GPU-based rendering with OpenGL ES 2.0
- 🔁 Dual-view UI: Raw + processed output stacked vertically
- 🎯 Lightweight and responsive

---

## 📸 Screenshots

| Raw View                         | Processed Output (Canny + Blur)        |
|----------------------------------|----------------------------------------|
| ![Raw](screenshots/raw_view.png) | ![Processed](screenshots/processed.png) |

> Replace the above with real screenshots or a GIF demo when available.

---

## ⚙️ Setup Instructions

### 🛠 Prerequisites

- Android Studio (latest version)
- Android NDK + CMake (install via SDK Manager)
- OpenCV Android SDK ([Download here](https://opencv.org/releases/))

### 📦 Installation Steps

1. **Clone this repo:**

   ```bash
   git clone https://github.com/your-username/vision-app.git
   cd vision-app
   ```
2.  **Add OpenCV SDK:** <br>
     Download the OpenCV Android SDK ZIP <br>
     Extract it into the project at:
    
   ```bash
   sdk/OpenCV-android-sdk/
   ```
3. **Ensure NDK and CMake are installed:** <br>
Open Android Studio → SDK Manager <br>
Go to SDK Tools tab <br>
Check ✅ NDK, CMake, and LLDB

4. **Set native build path in build.gradle.kts:**
   ```bash
   externalNativeBuild {
    cmake {
        path = file("src/main/cpp/CMakeLists.txt")
        version = "3.22.1"
    }
   }
   ```
5. **Run the app:** <br>
Grant camera permission <br>
Test on a physical Android device (camera input on emulators may be limited)

## 🧠 Architecture Overview

```plaintext
+---------------------+
| Java UI (Activity)  |
+---------------------+
          ↓
  Camera2 API captures frame
          ↓
  TextureView updates frame
          ↓
  Bitmap extracted → Grayscale Byte Array
          ↓
      JNI (Native C++)
          ↓
  OpenCV (GaussianBlur + Canny)
          ↓
  Result copied back to Java
          ↓
  Render via OpenGL ES (GLSurfaceView)
```
   
