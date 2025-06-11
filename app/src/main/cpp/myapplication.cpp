// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("myapplication");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("myapplication")
//      }
//    }
#include <jni.h>
#include <opencv2/opencv.hpp>

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_NativeProcessor_processFrame(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray input,
        jint width,
        jint height,
        jbyteArray output) {

    // Get raw data from Java byte arrays
    jbyte* inData = env->GetByteArrayElements(input, nullptr);
    jbyte* outData = env->GetByteArrayElements(output, nullptr);

    // Convert input byte array to OpenCV grayscale matrix
    cv::Mat src(height, width, CV_8UC1, reinterpret_cast<unsigned char*>(inData));
    cv::Mat blurred, edges;

    // ✅ Apply Gaussian Blur to reduce noise
    cv::GaussianBlur(src, blurred, cv::Size(5, 5), 0);

    // ✅ Apply Canny edge detection on the blurred image
    cv::Canny(blurred, edges, 100, 200);

    // Copy processed result into output buffer
    memcpy(outData, edges.data, width * height);

    // Release resources back to Java
    env->ReleaseByteArrayElements(input, inData, 0);
    env->ReleaseByteArrayElements(output, outData, 0);
}

