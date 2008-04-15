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

public class Chart
{
	protected int num_values;
	protected final int MAX_VALUES=100;
	protected int Date[]= new int[MAX_VALUES];
	protected int Value[]= new int[MAX_VALUES];
	protected int depth;
	protected Color mColor;
	
	
    public Chart(int num, int[] dates, int[] values, int d, Color c)
    {
        int one = 0x10000;
        num_values=num;

        depth =d;
        mColor=c;
        
        for (int i=0;i<num_values;i++){
        	Date[i]=dates[i];
        	Value[i]=values[i];
        }
        
        int vertices[]=new int[3*4*num_values];
        
        for (int i=0;i<num_values;i++){
        	vertices[12*i+0]=one*Date[i];	vertices[12*i+1]=one*Value[i];		vertices[12*i+2]=one*depth;
        	vertices[12*i+3]=one*Date[i];	vertices[12*i+4]=one*(Value[i]-1);	vertices[12*i+5]=one*depth;
        	vertices[12*i+6]=one*Date[i];	vertices[12*i+7]=one*(Value[i]-1);	vertices[12*i+8]=one*(depth-1);
        	vertices[12*i+9]=one*Date[i];	vertices[12*i+10]=one*Value[i];		vertices[12*i+11]=one*(depth-1);
        }
            
        int colors[]=new int[4*4*num_values];

        for (int i=0;i<4*num_values;i++){
        	colors[4*i]=(mColor.r+(i%2))*one;
//        	colors[4*i]=(mColor.r)*one;
        	colors[4*i+1]=(mColor.g)*one;
        	colors[4*i+2]=(mColor.b+((i+1)%2))*one;
        	colors[4*i+2]=(mColor.b)*one;
        	colors[4*i+3]=one;
        }

        int normals[]=new int[3*4*num_values];

        for (int i=0;i<num_values;i++){
        	int xNormal=0;
        	if (i<num_values-1){
        		if (Value[i]<Value[i+1]){
        			xNormal=-1;
        		}else{
        			xNormal=1;
        		}
        	}else{
        		if (Value[i-1]<Value[i]){
        			xNormal=-1;
        		}else{
        			xNormal=1;
        		}
        	}
/* 
         	normals[12*i]=-xNormal;
        	normals[12*i+1]=-one;
        	normals[12*i+2]=one;
 */       	
         	normals[12*i]=one;
        	normals[12*i+1]=0;
        	normals[12*i+2]=0;
/*
        	normals[12*i+3]=-xNormal;
        	normals[12*i+4]=-one;
        	normals[12*i+5]=-one;
*/        	
        	normals[12*i+3]=-one;
        	normals[12*i+4]=0;
        	normals[12*i+5]=0;
        	
        	normals[12*i+6]=xNormal;
        	normals[12*i+7]=one;
        	normals[12*i+8]=-one;
   /*     
        	normals[12*i+9]=xNormal;
        	normals[12*i+10]=one;
        	normals[12*i+11]=one;
 */
        	normals[12*i+9]=-one;
        	normals[12*i+10]=0;
        	normals[12*i+11]=0;
       }

        
        byte indices[]=new byte[24*(num_values-1)+12];
        
        for(int i=0;i<(num_values-1);i++){
        	indices[24*i+0]=(byte)(4*i); 
        	indices[24*i+1]=(byte)(4*i+3); 
        	indices[24*i+2]=(byte)(4*i+4); 

        	indices[24*i+3]=(byte)(4*i+4); 
        	indices[24*i+4]=(byte)(4*i+3); 
        	indices[24*i+5]=(byte)(4*i+7); 
        
        	indices[24*i+6]=(byte)(4*i+1); 
        	indices[24*i+7]=(byte)(4*i); 
        	indices[24*i+8]=(byte)(4*i+5); 

        	indices[24*i+9]=(byte)(4*i+5); 
        	indices[24*i+10]=(byte)(4*i); 
        	indices[24*i+11]=(byte)(4*i+4); 

        	indices[24*i+12]=(byte)(4*i+2); 
        	indices[24*i+13]=(byte)(4*i+1); 
        	indices[24*i+14]=(byte)(4*i+6); 

        	indices[24*i+15]=(byte)(4*i+6); 
        	indices[24*i+16]=(byte)(4*i+1); 
        	indices[24*i+17]=(byte)(4*i+5); 
        	
        	indices[24*i+18]=(byte)(4*i+3); 
        	indices[24*i+19]=(byte)(4*i+2); 
        	indices[24*i+20]=(byte)(4*i+7); 

        	indices[24*i+21]=(byte)(4*i+7); 
        	indices[24*i+22]=(byte)(4*i+2); 
        	indices[24*i+23]=(byte)(4*i+6); 
        }
        indices[24*(num_values-1)+0]=(byte)(0); 
        indices[24*(num_values-1)+1]=(byte)(1); 
        indices[24*(num_values-1)+2]=(byte)(2); 
        indices[24*(num_values-1)+3]=(byte)(0); 
        indices[24*(num_values-1)+4]=(byte)(2); 
        indices[24*(num_values-1)+5]=(byte)(3); 
        indices[24*(num_values-1)+6]=(byte)(4*(num_values-1)+0); 
        indices[24*(num_values-1)+7]=(byte)(4*(num_values-1)+2); 
        indices[24*(num_values-1)+8]=(byte)(4*(num_values-1)+1); 
        indices[24*(num_values-1)+9]=(byte)(4*(num_values-1)+0); 
        indices[24*(num_values-1)+10]=(byte)(4*(num_values-1)+3); 
        indices[24*(num_values-1)+11]=(byte)(4*(num_values-1)+2); 
                      

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

	ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
    nbb.order(ByteOrder.nativeOrder());
	mNormalBuffer = nbb.asIntBuffer();
	mNormalBuffer.put(normals);
	mNormalBuffer.position(0);

	mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	mIndexBuffer.put(indices);
	mIndexBuffer.position(0);
    }
    
    public void draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glNormalPointer(3, GL10.GL_FIXED, mNormalBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, 24*(num_values-1)+12, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
     
    }
    
    private IntBuffer   mVertexBuffer;
    private IntBuffer   mColorBuffer;
    private IntBuffer  	mNormalBuffer;
    
    private ByteBuffer  mIndexBuffer;
}
