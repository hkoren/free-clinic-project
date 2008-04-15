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

/**
 * @author Iman
 *
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class FCPCube
{
    public FCPCube()
    {
        int one = 0x10000;

        int v[] =new int[24];
        for (int k=0;k<24;k++)
        	v[k]=one;
        
        v[0]=v[1]=-one;
        v[2]=-one;
        v[4]=-one;
        v[5]=-one;
        v[8]=-one;
        v[9]=-one;
        v[11]=-one;
        v[12]=-one;
        v[13]=-one;
        v[16]=-one;
        v[21]=-one;
        
        	
        int vertices[] = {
               -one, -one, -one,
                one, -one, -one,
                one,  one, -one,
               -one,  one, -one,
               -one, -one,  one,
                one, -one,  one,
                one,  one,  one,
               -one,  one,  one,
            };
            
        int colors[] = {
                  0,    0,    0,  one,
                one,    0,    0,  one,
                one,  one,    0,  one,
                  0,  one,    0,  one,
                  0,    0,  one,  one,
                one,    0,  one,  one,
                one,  one,  one,  one,
                  0,  one,  one,  one,
            };

        byte indices[] = {
                0, 4, 5,    0, 5, 1,
                1, 5, 6,    1, 6, 2,
                2, 6, 7,    2, 7, 3,
                3, 7, 4,    3, 4, 0,
                4, 7, 6,    4, 6, 5,
                3, 0, 1,    3, 1, 2
        };

	// Buffers to be passed to gl*Pointer() functions
	// must be direct, i.e., they must be placed on the
	// native heap where the garbage collector cannot
	// move them.
    //
    // Buffers with multi-byte datatypes (e.g., short, int, float)
    // must have their byte order set to native order

    ByteBuffer vbb = ByteBuffer.allocateDirect(v.length*4);
    vbb.order(ByteOrder.nativeOrder());
    mVertexBuffer = vbb.asIntBuffer();
	mVertexBuffer.put(v);
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
        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
//    	gl.glFinish();
    }
    
    private IntBuffer   mVertexBuffer;
    private IntBuffer   mColorBuffer;
    private ByteBuffer  mIndexBuffer;
}
