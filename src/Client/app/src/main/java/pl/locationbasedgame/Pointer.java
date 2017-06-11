package pl.locationbasedgame;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created on 25.05.2017.
 */
class Pointer extends Polygon {

    private short[] indices = { 0, 1, 2, 0, 2, 3 };
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;

    Pointer() {
        float vertices[] = {
                 0.0f,  0.622008459f, 0.0f, // top
                -1f,   -0.311004243f, 0.0f, // bottom left
                 1f,   -0.311004243f, 0.0f  // bottom right
        };
        vertexBuffer = createVertexBuffer(vertices);
        indexBuffer = createIndexBuffer(indices);
    }

    void draw(GL10 gl, float angle, float x, float y) {
        super.draw(gl, vertexBuffer, indexBuffer, indices, angle, x, y, 1, 1, 0, 0, 0);
    }
}
