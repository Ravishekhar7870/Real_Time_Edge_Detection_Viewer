package com.example.myapplication;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private int textureId;
    private ByteBuffer imageData;
    private boolean dataReady = false;
    private int width = 640, height = 480;

    private int program;
    private int aPosition, aTexCoord, uTexture;

    private FloatBuffer vertexBuffer;

    private final float[] vertices = {
            // X, Y,    U, V
            -1f, 1f,   0f, 0f,
            -1f, -1f,  0f, 1f,
            1f, 1f,    1f, 0f,
            1f, -1f,   1f, 1f,
    };

    public void updateImage(byte[] data, int w, int h) {
        this.width = w;
        this.height = h;
        imageData = ByteBuffer.allocateDirect(data.length);
        imageData.put(data);
        imageData.position(0);
        dataReady = true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        textureId = createTexture();
        setupShaders();
        setupVertexBuffer();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        GLES20.glViewport(0, 0, w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (dataReady && imageData != null) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_LUMINANCE,
                    width,
                    height,
                    0,
                    GLES20.GL_LUMINANCE,
                    GLES20.GL_UNSIGNED_BYTE,
                    imageData
            );
            dataReady = false;
        }

        GLES20.glUseProgram(program);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aTexCoord);

        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aPosition, 2, GLES20.GL_FLOAT, false, 4 * 4, vertexBuffer);

        vertexBuffer.position(2);
        GLES20.glVertexAttribPointer(aTexCoord, 2, GLES20.GL_FLOAT, false, 4 * 4, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aTexCoord);
    }

    private int createTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int tex = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        return tex;
    }

    private void setupVertexBuffer() {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    private void setupShaders() {
        String vertexShaderCode =
                "attribute vec4 a_Position;" +
                        "attribute vec2 a_TexCoord;" +
                        "varying vec2 v_TexCoord;" +
                        "void main() {" +
                        "  gl_Position = a_Position;" +
                        "  v_TexCoord = a_TexCoord;" +
                        "}";

        String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform sampler2D u_Texture;" +
                        "varying vec2 v_TexCoord;" +
                        "void main() {" +
                        "  float g = texture2D(u_Texture, v_TexCoord).r;" +
                        "  gl_FragColor = vec4(g, g, g, 1.0);" +
                        "}";

        int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        aPosition = GLES20.glGetAttribLocation(program, "a_Position");
        aTexCoord = GLES20.glGetAttribLocation(program, "a_TexCoord");
        uTexture = GLES20.glGetUniformLocation(program, "u_Texture");
    }

    private int compileShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
