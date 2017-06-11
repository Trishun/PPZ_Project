package pl.locationbasedgame;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Polygon super class
 * @author PD
 */
class Polygon {

    FloatBuffer createVertexBuffer(float[] vertices) {
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        return vertexBuffer;
    }
    ShortBuffer createIndexBuffer(short[] indices) {
        ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(indices.length * 2);
        indexByteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = indexByteBuffer.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
        return indexBuffer;
    }

    /*This function draws our pointer on screen. @param gl*/
    void draw(GL10 gl, FloatBuffer vertexBuffer, ShortBuffer indexBuffer, short[] indices,
              float angle, float x, float y, float size, float r, float g, float b, float alpha) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glScalef(size, size, 1.0f);
        gl.glRotatef(angle, 0, 0, 1);
        gl.glColor4f(r, g, b, alpha);
        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);
        // Enabled the vertices buffer for writing and to be used during rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0,vertexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);
        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glPopMatrix();
    }

}
