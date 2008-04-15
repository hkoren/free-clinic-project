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

import java.util.Map;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.OpenGLContext;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * An implementation of SurfaceView that uses the dedicated surface for
 * displaying an OpenGL animation.  This allows the animation to run in
 * a separate thread, without requiring that it be driven by the update
 * mechanism of the view hierarchy.
 */
public class GLChartView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    private GLThread mGLThread;
    private boolean mHasSurface;
 //   private Cube mCube;
 //   private float mAngle;
    
    private Cube            mCube;
    private Chart			mChart;
    private Chart			mChart2;
    
    private Axis			mAxis;
    private float 			mXpos;   
    private float           mAngle;
    private long            mNextTime;
    private boolean         mAnimate;
    private Color 			color;
    private boolean 		mRotate;
    private boolean 		mCurDown;
    private boolean			mResetCur;
    
    private int mCurX;
    private int mCurY;
    private float mCurPressure;
    private float mCurSize;
    private int mOldX;
    private int mOldY;
    private float mZoom;

     public GLChartView(Context context) {
        super(context);
        init();
    }
    public boolean onTouchEvent(MotionEvent event) {
    	int action = event.getAction();
        mCurDown = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;
        mCurX = (int)event.getX();
        mCurY = (int)event.getY();

        if (!mResetCur){
//       	mAngle += mCurX-mOldX;
        	mXpos += (mCurX-mOldX)*0.1;
        	mZoom += (mCurY-mOldY)*0.002; 
        	
        }
        
        mResetCur=false;
        
        if (!mCurDown) mResetCur=true;
        
        mCurPressure = event.getPressure();
        mCurSize = event.getSize();
    	
        if (mCurDown) mRotate=false;
        
        mOldX=mCurX;
        mOldY=mCurY;
        
    	return true;
    }
    public GLChartView(Context context, AttributeSet attrs, Map inflateParams) {
        super(context, attrs, inflateParams);
        init();
    }
    
    private void init() {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed 
        mHolder = getHolder();
        mHolder.addCallback(this);
        
        mCube = new Cube();
        int dates[]={ 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50 };
        int values[]={ 15,10,0,-6,6,18,-5,6,8,0,12};
        color =new Color();
        color.set(1, 0, 0);//red
        mChart = new Chart(10,dates,values,0,color);
 
        int dates2[]={ -4, 6, 9, 15, 23, 27, 32, 36, 41, 45, 50 };
        int values2[]={ 20,5,15,-12,11,-12,11,21,-5,12,-10};
        
        color.set(0, 1, 0);//green
        mChart2 = new Chart(10,dates2,values2,-1,color);
 
        mAxis=new Axis(-15,50,-40,40,0);

        mAnimate = false;
        mRotate = true;
        mZoom=1.0f;
        mXpos=0.0f;
        
        mResetCur=true;
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, start our drawing thread.
        mGLThread = new GLThread();
        mGLThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return
        mGLThread.requestExitAndWait();
        mGLThread = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Surface size or format has changed. This should not happen in this
        // example.
        mGLThread.onWindowResize(w, h);
    }

    // ----------------------------------------------------------------------

    class GLThread extends Thread {
        private boolean mDone;
        private int mWidth;
        private int mHeight;
        
        GLThread() {
            super();
            mDone = false;
            mWidth = 0;
            mHeight = 0;
            mCube = new Cube();
        }
    
        @Override
        public void run() {
            /* 
             * Create an OpenGL|ES context. This must be done only once, an
             * OpenGL context is a somewhat heavy object.
             */
            OpenGLContext glc = new OpenGLContext( OpenGLContext.DEPTH_BUFFER );
            
            /*
             * Before we can issue GL commands, we need to make sure 
             * the context is current and bound to a surface.
             */
            SurfaceHolder holder = mHolder;
            glc.makeCurrent(holder);
            
            /*
             * First, we need to get to the appropriate GL interface.
             * This is simply done by casting the GL context to either
             * GL10 or GL11.
             */
            GL10 gl = (GL10)(glc.getGL());

            /*
             * Some one-time OpenGL initialization can be made here
             * probably based on features of this particular context
             */
             gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);


            // This is our main acquisition thread's loop, we go until
            // asked to quit.
            while (!mDone) {
                // Update the asynchronous state (window size, key events)
                int w, h;
                synchronized(this) {
                    w = mWidth;
                    h = mHeight;
                }

                /* draw a frame here */
                drawFrame(gl, w, h);

                /*
                 * Once we're done with GL, we need to call post()
                 */
                glc.post();
            }
            
            glc.makeCurrent((SurfaceHolder)null);
        }
        
        private void drawFrame(GL10 gl, int w, int h) {
        
            gl.glViewport(0, 0, w, h);
        
            /*
             * Set our projection matrix. This doesn't have to be done
             * each time we draw, but usualy a new projection needs to be set
             * when the viewport is resized.
             */

            float ratio = (float)w / h;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1f, 1f, 0.1f, 10f);

            /*
             * By default, OpenGL enables features that improve quality
             * but reduce performance. One might want to tweak that
             * especially on software renderer.
             */
            gl.glDisable(GL10.GL_DITHER);
            gl.glActiveTexture(GL10.GL_TEXTURE0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

            /*
             * Usually, the first thing one might want to do is to clear
             * the screen. The most efficient way of doing this is to use
             * glClear(). However we must make sure to set the scissor
             * correctly first. The scissor is always specified in window
             * coordinates:
             */

            gl.glClearColor(0,0,0,1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            /*
             * Now we're ready to draw some 3D object
             */

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -5.0f);
            
            gl.glScalef(mZoom, 1, 1);
            gl.glTranslatef(mXpos, 0, 0);
            
 //           gl.glRotatef(mAngle,        0, 1, 0);
           // gl.glRotatef(mAngle*0.25f,  1, 0, 0);

            gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glEnable(GL10.GL_DEPTH_TEST);

//            mCube.draw(gl);
//
//            gl.glRotatef(mAngle*2.0f, 0, 1, 1);
//            gl.glTranslatef(0.5f, 0.5f, 0.5f);
//
//            mCube.draw(gl);
//            
//            mAngle += 1.2f;
 
            gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnable(GL10.GL_CULL_FACE);
//            gl.glEnable(gl.GL_DEPTH_TEST);
//            mCube.draw(gl);
            mChart.draw(gl);
            mChart2.draw(gl);
            mAxis.draw(gl);
         
            if (mRotate){
           // 	mAngle += 1.2f;
            }
            
        }

        public void onWindowResize(int w, int h) {
            synchronized(this) {
                mWidth = w;
                mHeight = h;
            }
        }
        
        public void requestExitAndWait() {
            // don't call this from GLThread thread or it a guaranteed
            // deadlock!
            mDone = true;
            try {
                join();
            } catch (InterruptedException ex) { }
        }
    }
}
