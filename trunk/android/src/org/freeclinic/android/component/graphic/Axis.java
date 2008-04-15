/*	
 
  	Free Clinic Project
 
 	Copyright (c) 2007, 2008  Free Clinic Project  
 		A project by students of the University of California San Diego.  All rights reserved.
	
	This file is part of the Free Clinic Project.

    The Free Clinic Project is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The Free Clinic Project is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Free Clinic Project.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeclinic.android.component.graphic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Iman
 *
 */

public class Axis
{
    public Axis(int xMin, int xMax, int yMin, int yMax, int mdepth)
    {
        int one = 0x10000;

        depth=mdepth;
        x_max=xMax;
        x_min=xMin;
        y_max=yMax;
        y_min=yMin;
        	
        int vertices[] = {
               one*x_min, 0, one*depth,
               one*x_max, 0, one*depth,
               0, one*y_min, one*depth,
               0, one*y_max, one*depth,
               one*x_max-one, one, one*depth,
               one*x_max-one, -one, one*depth,
               one, one*y_max-one, one*depth,
               -one, one*y_max-one, one*depth,
            };
            
        int colors[] = {
                one,  one,  one,  one,
                one,  one,  one,  one,
                one,  one,  one,  one,
                one,  one,  one,  one,
                one,  one,  one,  one,
                one,  one,  one,  one,
                one,  one,  one,  one,
                one,  one,  one,  one
            };

        byte indices[] = {
                0, 1, 
                2, 3,
                2, 3,
                1, 5,
                1, 4,   
                3, 6,
                3, 7,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0
        };

	// Buffers to be passed to gl*Pointer() functions
	// must be direct, i.e., they must be placed on the
	// native heap where the garbage collector cannot
	// move them.
    //
    // Buffers with multi-byte datatypes (e.g., short, int, float)
    // must have their byte order set to native order

    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
    vbb.order(ByteOrder.nativeOrder());
    mVertexBuffer = vbb.asIntBuffer();
	mVertexBuffer.put(vertices);
	mVertexBuffer.position(0);

    ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
    cbb.order(ByteOrder.nativeOrder());
	mColorBuffer = cbb.asIntBuffer();
	mColorBuffer.put(colors);
	mColorBuffer.position(0);

	mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	mIndexBuffer.put(indices);
	mIndexBuffer.position(0);
    }
    
    public void draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_LINES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
//    	gl.glFinish();
    }
    
    public void setX(int m, int M){
    	this.x_min=m;
    	this.x_max=M;
    	return;
    }
    
    public void setY(int m, int M){
    	this.y_min=m;
    	this.y_max=M;
    	return;
    }
    
    public void setXmin(int m){
    	this.x_min=m;
    	return;
    }

    public void setXmax(int M){
    	this.x_max=M;
    	return;
    }
    public void setYmin(int m){
    	this.y_min=m;
    	return;
    }

    public void setYmax(int M){
    	this.y_max=M;
    	return;
    }
    public void setDepth(int d){
    	this.depth=d;
    	return;
    }    
    
    private IntBuffer   mVertexBuffer;
    private IntBuffer   mColorBuffer;
    private ByteBuffer  mIndexBuffer;
    private int x_max;
    private int x_min;
    private int y_max;
    private int y_min;
    private int depth;    
    
}
