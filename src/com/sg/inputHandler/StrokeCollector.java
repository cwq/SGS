package com.sg.inputHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;

import com.sg.constraint.ConstraintHandler;
import com.sg.controller.UnitController;
import com.sg.object.Point;
import com.sg.property.common.ThresholdProperty;
import com.sg.unit.SketchUnit;

/**
 * 触屏事件流程处理
 * 草图点收集
 * @author DELL
 *
 */
public class StrokeCollector {
	
	private List<Point> points1;
	private List<Point> points2;
	private SketchUnit drawingSketch;
	private long downTime;
	private long TIME = ThresholdProperty.PRESS_TIME;
	private int state;
	private static final int INITIAL = 0;
	private static final int GESTURE = 1;
	private static final int UNIT = 2;
	
	public StrokeCollector() {
		points1 = new ArrayList<Point>();
		points2 = new ArrayList<Point>();
		drawingSketch = UnitController.getInstance().getSketchUnit();
	}
	
	public void collect(MotionEvent event) {
		int action = event.getAction();
		if (MotionEvent.ACTION_CANCEL == action) {
			return;
		}
		

		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			points1.clear();
			points2.clear();
			downTime = new Date().getTime();
			Point startPoint = new Point(event.getX(0), event.getY(0));
			points1.add(startPoint);
			drawingSketch.setPointList(points1);
			Log.v("sc", "down: "+downTime);
			break;
		case MotionEvent.ACTION_MOVE:
			//new Date().getTime();
			Point movePoint = new Point(event.getX(0), event.getY(0));
			points1.add(movePoint);
			
			GestureRecognizer ins = GestureRecognizer.getInstance();
			
			switch(state) {
			case INITIAL: //无特殊状态

				//多点 或者 起 始点点中选中的图元
				if (event.getPointerCount() > 1){
					//多点为手势
					points2.add(new Point(event.getX(1), event.getY(1)));
					ins.recognize(movePoint, new Point(event.getX(1), event.getY(1)));
					state = 1;

					Log.v("sc", "state0 : 多点为手势");
					break;

				}
				else if (ins.isInSelects(points1.get(0))) {
					Log.v("sc", "state0 : 当前有图元选中  识别手势");
					//如果当前有图元选中  并且起 始点中选择的图元  识别手势
					ins.recognize(points1);
					state = 1;
					break;
				}
				else if((new Date().getTime() - downTime) > TIME) {
					Log.v("sc", "state0 : 时间超过" + (new Date().getTime() - downTime));
					//时间超过阀值
					if (ins.isSelectOneUnit(points1)) {
						Log.v("sc", "state0 : 选中手势");
						//识别选中手势
						state = 1;

						break;
					} else {
						Log.v("sc", "state0 : 图元识别");
						state = 2;
						UnitRecognizer.getInstance().recognizeFirstPart(points1);
						break;
					}
				}
				break;
			case GESTURE: //手势识别状态
				drawingSketch.setPointList(new ArrayList<Point>());
				if (event.getPointerCount() > 1){
					Log.v("sc", "state1 : 多点为手势");
					points2.add(new Point(event.getX(1), event.getY(1)));
					ins.recognize(movePoint, new Point(event.getX(1), event.getY(1)));
				} else {
					Log.v("sc", "state1 : 单点手势");
					ins.recognize(points1);
				}
				break;
			case UNIT: //图元识别
				UnitRecognizer.getInstance().recognizeUnitOnMove(movePoint);
				//drawingSketch.setPointList(UnitRecognizer.getInstance().recognizeUnitOnMove(movePoint));
				Log.v("sc", "state2 : 图元识别");
				break;
			default:
					break;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			Log.v("sc", "up1");
			
			GestureRecognizer.getInstance().init();
			
			//只有手势识别的up要识别约束
			if (state == GESTURE) {
				ConstraintHandler.constraintRecognize(UnitController.getInstance().getSelects());
			} else {
				UnitRecognizer.getInstance().recognizeUnitOnUp(points1, state);
			}
			//识别是否删除（采用android型 拖动删除）
//			points1.clear();
			points2.clear();
			drawingSketch.clear();
//			drawingSketch.setPointList(new ArrayList<Point>());
			state = 0;
			//if 弹起来 {
		//	规整
		//	用户意图推测
		//}
			break;
		default:
			return;
		}
	}
	
	/**
	 * 识别选中单个图元手势
	 * @param e
	 */
	public void onLongPress(MotionEvent e) {
		Log.v("sc", "press: "+new Date().getTime());
		Point p = new Point(e.getX(0), e.getY(0));
		if(UnitController.getInstance().selectOneUnit(p))
			state = 1;
		else
			UnitController.getInstance().setNoSelect();
	}
	
	/**
	 * 识别选中所有有约束的图元手势
	 * @param e
	 */
	public void onDoubleTap(MotionEvent e) {
		Point p = new Point(e.getX(0), e.getY(0));
		UnitController.getInstance().selectGroup(p);
	}

}
