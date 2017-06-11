package pl.locationbasedgame;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;

/**
 * Created on 25.05.2017.
 */

class Circle extends Polygon {

    private int points = 360;
    private FloatBuffer vertexBuffer;

    Circle() {
        float vertices[] = new float[(points + 1) * 3];
        for (int i = 3; i < (points + 1) * 3; i += 3) {
            double rad = (i * 360 / points * 3) * (3.14 / 180);
            vertices[i] = (float) Math.cos(rad);
            vertices[i + 1] = (float) Math.sin(rad);
            vertices[i + 2] = 0;
        }
        vertexBuffer = createVertexBuffer(vertices);
    }

    void draw(GL10 gl, float size, float r, float g, float b) {
        gl.glPushMatrix();
        gl.glScalef(size, size, 1.0f);
        gl.glColor4f(r, g, b, 1.0f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, points / 2);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();
    }

}

