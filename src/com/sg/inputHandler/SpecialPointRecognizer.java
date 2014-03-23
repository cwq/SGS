package com.sg.inputHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
import com.sg.property.common.ThresholdProperty;

/**
 * 点处理
 * 特征点识别
 * @author DELL
 *
 */
public class SpecialPointRecognizer {

	public static SpecialPointRecognizer instance = new SpecialPointRecognizer();
	
	private SpecialPointRecognizer() {
		
	}
	
	public static SpecialPointRecognizer getInstance() {
		return instance;
	}
	
	/**
	 * a[i] = 0.7*a[i] + 0.3*a[i+1] / a[i] = 0.7*a[i] + 0.3*a[i-1]
	 * 高斯处理消噪
	 * @param pList
	 */
	public void gaussProcessing(List<Point> pList) {
		int n = pList.size();
		//从前到后处理一遍
		float x = 0.0f;
		float y = 0.0f;
		for(int i = 0; i < n-1; i++) {
			Point info = pList.get(i);
			x = 0.7f * info.getX() + 0.3f * pList.get(i+1).getX();
			y = 0.7f * info.getY() + 0.3f * pList.get(i+1).getY();
			info.setX(x);
			info.setY(y);
		}
		
		//从后到前处理一遍
		for(int i = n-1; i > 0; i--) {
			Point info = pList.get(i);
			x = 0.7f * info.getX() + 0.3f * pList.get(i-1).getX();
			y = 0.7f * info.getY() + 0.3f * pList.get(i-1).getY();
			info.setX(x);
			info.setY(y);
		}
	}
	
	/**
	 * 速度过滤方法：低于平均值的一定百分比算是特征点
	 * @param pList
	 */
	private void speed(List<Point> pList, int[] total) {
		int n = pList.size();
		double[] speed = new double[n];
		speed[0] = 0;   // 第一点和最后一点速度置为0.0
		speed[n-1] = 0;

		double average = 0.0;
		for (int i = 1; i < n - 1; i++) {
			double tempSpeed = CommonFunction.distance(pList.get(i), pList.get(i - 1))
					+ CommonFunction.distance(pList.get(i + 1), pList.get(i));
			speed[i] = tempSpeed;
			average += tempSpeed;
		}
		average /= n;
		for (int i = 1; i < n-1; i++) {
			if (speed[i] < average * 0.42) {
				total[i] += 1;;
			}
		}
		total[0] += 1;
		total[n-1] += 1;
	}
	
	/**
	 * 方向过滤方法 对于点i；边<i-1,i>和边<i,i+1>之间的夹角来判断是否平滑过渡 夹角大于170度时，平滑过渡，否则，点i为一个转折点
	 * 运用余弦定理求解角度
	 * @param pList
	 * @param total 
	 */
	private void direction(List<Point> pList, int[] total) {
		int n = pList.size();
		double[] direction = new double[n];
		for (int i = 1; i < n - 1; i++) { // 余弦定理
			double A = CommonFunction.distance(pList.get(i - 1), pList.get(i + 1));
			double B = CommonFunction.distance(pList.get(i), pList.get(i + 1));
			double C = CommonFunction.distance(pList.get(i - 1), pList.get(i));

			double tmp = B * B + C * C - A * A;
			tmp = tmp / (2 * B * C + 0.00001);
			double ridian = Math.acos(tmp);
			direction[i] = (ridian * 180.0 / Math.PI);
		}


		for (int i = 1; i < n - 1; i++) {
			if (direction[i] <= 170.0) {
                total[i] += 1;
			}
		}
		total[0] += 1;
		total[n-1] += 1;
		
	}
	
	/**
	 * 一：将五个点平移，使得i点在原点
     * 二：将五个点绕原点旋转[0,180)，记录五个点|y|的绝对值的和
     * 三：使得绝对值和最小的角度为i点切线与x轴的夹角
     * 旋转矩阵：cosA   -sinA
     *        sinA    cosA
     * 原来坐标(x,y),旋转后(xcosA+ysinA,-xsinA+ycosA);
     * 所以只需枚举[0,180)，计算所有-xsinA+ycosA的和，就是切线与x轴夹角
     * 求出夹角后，求曲率：
     * double rate = System.Math.Sin(30*pi/180.0);
	 * @param total 
	 * */
	private void curvity(List<Point> pList, int[] total) {
		int n = pList.size();
		double[] curvity = new double[n];
//		double[] sita = new double[n];

		curvity[0] = curvity[n - 1] = 0;
		for (int i = 1; i < n-1; i++) {
			//角度计算错误
			double sinA = Math.sin(Math.abs(CommonFunction.VectorToAngle(pList.get(i-1), pList.get(i)) -
					CommonFunction.VectorToAngle(pList.get(i+1), pList.get(i))));
			curvity[i] = 2 * sinA / CommonFunction.distance(pList.get(i-1), pList.get(i+1));
			Log.v("curvity", (float)curvity[i] + " , "+i+" , " + pList.get(i).toString());
		}
		
		
//		for (int i = 2; i < n - 2; i++) {
//			double mint = 0.0;
//
//			for (int k = 0; k < 4; k++) {     //有改动 lmc,for (int k = 0; k <= 4; k++)
//				mint += Math.abs(pList.get(i - 2 + k).getY()
//						- pList.get(i).getY());
//			}
//			sita[i] = 0.0;
//
//			for (int j = 1; j < 180; j++) {
//				double tmp = 0.0;
//
//				for (int k = 0; k < 4; k++) {   //有改动 lmc,for (int k = 0; k <= 4; k++)
//					tmp += Math.abs((pList.get(i - 2 + k).getY() - pList.get(i).getY()) * Math.cos(j * Math.PI / 180.0)
//							- (pList.get(i - 2 + k).getX() - pList.get(i).getX()) * Math.sin(j * Math.PI / 180.0));
//				}
//				
//				if(tmp < mint) {
//					mint = tmp;
//					sita[i] = j;
//				}
//			}
//		}
//		Log.v("time121", new Date().getTime()+"");
//		
//		for(int i = 2; i < n-2; i++) {
//			double tmp = 0.0;
//			for(int k = 1; k < 4; k++) {
//				tmp += Math.abs(sita[i - 2 + k] - sita[i - 3 + k]);
//			}
//			
//			curvity[i] = (tmp / CommonFunction.distance(pList.get(i-2), pList.get(i+2)));
//			
//			if(Math.abs(curvity[i]) > 100) {    //处理可能为异常抖动的点
//				curvity[i] = 0;
//				total[i] -= 1;
//				if(total[i] >= 2) {
//					total[i] += 1;
//				}
//			}
//		}
		
		double average = 0.0;
		for(int i = 0; i < n; i++) {
			average += curvity[i];
		}
		average /= n;
		Log.v("curvity", " average "+average);
		
		for(int i = 1; i < n-1; i++) {
			if(curvity[i] > average * 1.0) {
				total[i] += 1;
			} else if(total[i] >= 2) {
				total[i] += 1;
			}
		}
		total[0] += 1;
		total[n-1] += 1;
	}
	
	/**
	 * 对系列电进一步处理，去除相离很近的重复特征点
	 * @param total 
	 * */
	private void space(List<Point> pList, int[] total) {
		int n = pList.size();
		
			for(int i = 1; i < n-1; i++) {
				if( total[i] >= 2 ) {
					//去除该点前附近的噪点
					for(int j = i-1; j >= 0; j--) {
						if(total[j] >= 2 && CommonFunction.distance(pList.get(i),pList.get(j)) <= 
								ThresholdProperty.TWO_POINT_IS_CLOSED) {
							total[i] -= 1;
						}
					}
				}
				if((total[i] >= 2) && (CommonFunction.distance(pList.get(i),pList.get(n -1)) <=
						ThresholdProperty.TWO_POINT_IS_CLOSED)){
					//处理末尾附近点的冗余
					total[i] -= 1;
				}
			}
	}
	
	/**
	 * 获取特征点下标
	 * @param pList
	 * @return
	 */
	public List<Integer> getSpecialPointIndex(List<Point> pList) {
		//gaussProcessing(pList);
		int[] total = new int[pList.size()];
		speed(pList, total);
		direction(pList, total);
//		curvity(pList, total);
		space(pList, total);

		List<Integer> specialPointIndexs = new ArrayList<Integer>();
		int n = pList.size();
		for(int i=0; i < n; i++) {
			if(total[i] >= 2) {    //特征值大于等于4的点为特征点
				specialPointIndexs.add(i);
			}
		}
		
		return specialPointIndexs;
	}
}
