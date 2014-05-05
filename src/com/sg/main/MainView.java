package com.sg.main;

import com.sg.controller.UnitController;
import com.sg.inputHandler.StrokeCollector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MainView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {

	private SurfaceHolder mHolder;
	private boolean mLoop;
    private StrokeCollector sc;
    private UnitController uc;
    
    GestureDetector mGesture = null;  

	
	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
		setFocusable(true);
		mLoop = true; // 循环画图
		sc = new StrokeCollector();
		uc = UnitController.getInstance();
		
		mGesture = new GestureDetector(context, new LongPressGestureListener());
	}
	
	class LongPressGestureListener extends SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.v("sc", "onLongPress");
			sc.onLongPress(e);
		}
		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.v("sc", "onDoubleTap");
			sc.onDoubleTap(e);
			return true;
		}
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mLoop = true; // 循环画图
		new Thread(this).start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mLoop = false;
	}
	
	@Override
	public void run() {
		while(mLoop) {
			long startTime = System.currentTimeMillis();
			onDraw();
			long endTime= System.currentTimeMillis();
			//帧数 1000/16=62.5
			if (endTime - startTime < 16) {
				try {
					Thread.sleep(16 - (endTime - startTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		}
	}
	
	//绘图函数
	protected void onDraw() {
		if(mHolder == null) {
			return;
		}

		Canvas tCanvas = mHolder.lockCanvas();  // 锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了
		if(tCanvas == null) {
			return;
		}

		tCanvas.drawColor(Color.WHITE);         //画背景色
		//画图
		uc.draw(tCanvas);

		mHolder.unlockCanvasAndPost(tCanvas); // 结束锁定画图，并提交改变
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//isFirstUndoDelete = true;
		boolean handled = mGesture.onTouchEvent(event);
		Log.v("sc", "mGesture.onTouchEvent:"+handled);
		if(!handled)
			sc.collect(event);
		return true;
	}

}
