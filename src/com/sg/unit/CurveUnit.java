package com.sg.unit;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Path;
import com.sg.constraint.IChangable;
import com.sg.constraint.UnitChangeArgs;
import com.sg.object.Point;
import com.sg.object.SGObject;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;

public class CurveUnit extends BaseUnit implements IChangable {
	
	/**
	 * x^2/a^2+y^2/b^2=1
	 */
    private double a;
    
    /**
	 * x^2/a^2+y^2/b^2=1
	 */
    private double b;
    
    private PointUnit center;
    
    /**
     * 标准方程时 以x轴正方向（a，0）坐标上逆时针计算的曲线的起始角度
     */
    private double startAngle;
    
    /**
     * 坐标上顺时针为负 逆时钟为正   实际上(y轴正方向朝下)屏幕看到的效果顺时针为正
     */
    private double sweepAngle;
    
    /**
     * 以center为中心的旋转，坐标上顺时针为负 逆时钟为正,[-pi..pi]. 实际上屏幕看到的效果顺时针为正
     */
    private double rotateAngle;

    private Point[] ctlPoint;   //贝塞尔曲线控制点
    
    public CurveUnit() {
        this(new Point(0, 0), 0, 0, 0, 0, 0);
    }

    public CurveUnit(Point c, double a, double b, double startAngle, double sweepAngle, double rotateAngle)
    {
        center = new PointUnit(c);
        this.a = a;
        this.b = b;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.rotateAngle = rotateAngle;
        this.center.addUnitListener(this);
    }
    
    public void OnChange(UnitChangeArgs e) {
    	notifies(e.Next(this));
    }
    
	@Override
	public void draw(Canvas canvas, Painter painter) {
		// TODO Auto-generated method stub
		if(Math.abs(sweepAngle - 0) < ThresholdProperty.FLOAT_OFFSET) return;
		
		CtlPointUpdate();
		Path path = new Path();
		path.moveTo(ctlPoint[0].getX(),ctlPoint[0].getY());
		for(int i=1; i<ctlPoint.length; i=i+3) {
			path.cubicTo(ctlPoint[i].getX(), ctlPoint[i].getY(), 
					ctlPoint[i+1].getX(), ctlPoint[i+1].getY(), ctlPoint[i+2].getX(), ctlPoint[i+2].getY());
		}
		canvas.drawPath(path, painter.getPaint());
	}

    // 更新控制点
    private synchronized void CtlPointUpdate() {
        while (this.startAngle > 2 * Math.PI) this.startAngle -= 2 * Math.PI;
        while (this.startAngle < -2 * Math.PI) this.startAngle += 2 * Math.PI;
        while (this.rotateAngle > 2 * Math.PI) this.rotateAngle -= 2 * Math.PI;
        while (this.rotateAngle < -2 * Math.PI) this.rotateAngle += 2 * Math.PI;
        if (this.sweepAngle > 2 * Math.PI) this.sweepAngle = 2 * Math.PI;
        if (this.sweepAngle < -2 * Math.PI) this.sweepAngle = -2 * Math.PI;

        double sweepAngle = this.sweepAngle;
        if (sweepAngle < 0)
        {
            sweepAngle = Math.atan2(Math.sin(sweepAngle) * a, Math.cos(sweepAngle) * b);
            if (sweepAngle >= 0) sweepAngle -= 2 * Math.PI;
        }
        else
        {
            sweepAngle = Math.atan2(Math.sin(sweepAngle) * a, Math.cos(sweepAngle) * b);
            if (sweepAngle <= 0) sweepAngle += 2 * Math.PI;
        }

        if (sweepAngle > Math.PI * 3 / 2)
        {
            ctlPoint = new Point[13];
            ArcToBezier(startAngle, Math.PI / 2, 0);
            ArcToBezier(startAngle + Math.PI / 2, Math.PI / 2, 3);
            ArcToBezier(startAngle + Math.PI, Math.PI / 2, 6);
            ArcToBezier(startAngle + Math.PI * 3 / 2, sweepAngle - Math.PI * 3 / 2, 9);
        }
        else if (sweepAngle > Math.PI)
        {
            ctlPoint = new Point[10];
            ArcToBezier(startAngle, Math.PI / 2, 0);
            ArcToBezier(startAngle + Math.PI / 2, Math.PI / 2, 3);
            ArcToBezier(startAngle + Math.PI, sweepAngle - Math.PI, 6);
        }
        else if (sweepAngle > Math.PI / 2)
        {
            ctlPoint = new Point[7];
            ArcToBezier(startAngle, Math.PI / 2, 0);
            ArcToBezier(startAngle + Math.PI / 2, sweepAngle - Math.PI / 2, 3);
        }
        else if (sweepAngle >= -Math.PI / 2)
        {
            ctlPoint = new Point[4];
            ArcToBezier(startAngle, sweepAngle, 0);
        }
        else if (sweepAngle < -Math.PI * 3 / 2)
        {
            ctlPoint = new Point[13];
            ArcToBezier(startAngle, -Math.PI / 2, 0);
            ArcToBezier(startAngle - Math.PI / 2, -Math.PI / 2, 3);
            ArcToBezier(startAngle - Math.PI, -Math.PI / 2, 6);
            ArcToBezier(startAngle - Math.PI * 3 / 2, sweepAngle + Math.PI * 3 / 2, 9);
        }
        else if (sweepAngle < -Math.PI)
        {
            ctlPoint = new Point[10];
            ArcToBezier(startAngle, -Math.PI / 2, 0);
            ArcToBezier(startAngle - Math.PI / 2, -Math.PI / 2, 3);
            ArcToBezier(startAngle - Math.PI, sweepAngle + Math.PI, 6);
        }
        else
        {
            ctlPoint = new Point[7];
            ArcToBezier(startAngle, -Math.PI / 2, 0);
            ArcToBezier(startAngle - Math.PI / 2, sweepAngle + Math.PI / 2, 3);
        }

        Point[] rPoint = new Point[ctlPoint.length];
        for (int i = 0; i < ctlPoint.length; i++)
        {
            rPoint[i] = CommonFunction.RotatePoint(ctlPoint[i], rotateAngle, center.toPoint());
        }
        ctlPoint = rPoint;
    }

    private void ArcToBezier(double startAngle, double sweepAngle, int stNum)
    {
        double x0 = Math.cos(sweepAngle / 2.0);
        double y0 = Math.sin(sweepAngle / 2.0);
        double tx = (1.0 - x0) * 4.0 / 3.0;
        double ty = y0 - tx * x0 / y0;

        double sn = Math.sin(startAngle + sweepAngle / 2.0);
        double cs = Math.cos(startAngle + sweepAngle / 2.0);

        ctlPoint[stNum] = new Point((float)(center.getX() + a * (x0 * cs + y0 * sn)),
        							(float)(center.getY() + b * (x0 * sn - y0 * cs)));
        ctlPoint[stNum + 1] = new Point((float)(center.getX() + a * ((x0 + tx) * cs + ty * sn)),
        							(float)(center.getY() + b * ((x0 + tx) * sn - ty * cs)));
        ctlPoint[stNum + 2] = new Point((float)(center.getX() + a * ((x0 + tx) * cs - ty * sn)),
        							(float)(center.getY() + b * ((x0 + tx) * sn + ty * cs)));
        ctlPoint[stNum + 3] = new Point((float)(center.getX() + a * (x0 * cs - y0 * sn)),
        							(float)(center.getY() + b * (x0 * sn + y0 * cs)));
    }

    public boolean Adapt(List<Point> pList)
    {
        double[] fact = new double[6];
        fact[5] = 1000000;  //系数放大
        if (!FirstRec_LastSquare(pList, fact)) return false;

        double a = fact[0];
        double b = fact[1];
        double c = fact[2];
        double d = fact[3];
        double e = fact[4];
        double f = fact[5];

        if (!(b * b - 4 * a * c < -0.01)) return false;

        double y0 = (2 * a * e - b * d) / (b * b - 4 * a * c);
        double x0 = (-(d + b * y0)) / (2 * a);
        center.Set(x0, y0);
        rotateAngle = Math.atan2(b, a - c) / 2;
        double Sin = Math.sin(rotateAngle);
        double Cos = Math.cos(rotateAngle);

        // 移轴，旋转
        double sf = a * x0 * x0 + b * x0 * y0 + c * y0 * y0 + d * x0 + e * y0 + f;
        this.a = Math.sqrt(-sf / (a * Cos * Cos + b * Sin * Cos + c * Sin * Sin));
        this.b = Math.sqrt(-sf / (a * Sin * Sin - b * Sin * Cos + c * Cos * Cos));

//        double pSweepAngle = sweepAngle;
//        startAngle = CommonFunction.VectorToAngle(pList.get(0), center.toPoint()) - rotateAngle;
//        sweepAngle = CommonFunction.VectorToAngle(pList.get(pList.size()-1), center.toPoint()) - startAngle - rotateAngle;
//        if (Math.abs(pSweepAngle - 0) > 1e-9)
//        {
//            if (pSweepAngle < 0 && sweepAngle > 0) sweepAngle -= 2 * Math.PI;
//            if (pSweepAngle > 0 && sweepAngle < 0) sweepAngle += 2 * Math.PI;
//        }
        
		double sum = 0;
		for (int i = 0; i < pList.size() - 1; i++) {
			sum += Math.abs(CommonFunction.VectorToAngle(pList.get(i + 1), center.toPoint())
					- CommonFunction.VectorToAngle(pList.get(i), center.toPoint()));
		}
		if (Math.abs(sum) > 3.85 * Math.PI) {
			//曲线闭合
			startAngle = 0;
			sweepAngle = 2 * Math.PI;
		} else {
			//曲线不闭合
	        startAngle = CommonFunction.VectorToAngle(pList.get(0), center.toPoint()) - rotateAngle;
	        sweepAngle = CommonFunction.VectorToAngle(pList.get(pList.size()-1), center.toPoint()) - startAngle - rotateAngle;

	    	//是否逆时针排列	
	        boolean ccw = false;
			Point v1 = new Point(pList.get(0).getX() - center.getX(), pList.get(0).getY() - center.getY());
			Point v2 = new Point(pList.get(4).getX() - center.getX(), pList.get(4).getY() - center.getY());
			if((v1.getX()*v2.getY() - v1.getY()*v2.getX()) > 0) ccw = true;
			
			//由于屏幕坐标y轴正方向朝下
			if (ccw) {
				//逆时针
				if (sweepAngle < 0) {
					sweepAngle += 2 * Math.PI;
				}
			} else {
				//顺时针
				if (sweepAngle > 0) {
					sweepAngle -= 2 * Math.PI;
				}
			}
		}

        return true;
    }
    
    // 初步识别——最小二乘法
    private boolean FirstRec_LastSquare(List<Point> pList, double[] fact)
    {

        // aX^2 + bXY + cY^2 + dX + eY + 1 = 0
        // 根据最小二乘法，先构造矩阵
        // | ∑x(4)y(0) ∑x(3)y(1) ∑x(2)y(2) ∑x(3)y(0) ∑x(2)y(1) ∑x(2)y(0) |
        // | ∑x(3)y(1) ∑x(2)y(2) ∑x(1)y(3) ∑x(2)y(1) ∑x(1)y(2) ∑x(1)y(1) |
        // | ∑x(2)y(2) ∑x(1)y(3) ∑x(0)y(4) ∑x(1)y(2) ∑x(0)y(3) ∑x(0)y(2) |
        // | ∑x(3)y(0) ∑x(2)y(1) ∑x(1)y(2) ∑x(2)y(0) ∑x(1)y(1) ∑x(1)y(0) |
        // | ∑x(2)y(1) ∑x(1)y(2) ∑x(0)y(3) ∑x(1)y(1) ∑x(0)y(2) ∑x(0)y(1) |

        final int n = 5;
        final double standard_deviation = 0.1;	// 二次曲线标准差
        double[][] mEle = new double[n][n]; // 取前 n 个点作高斯消元求系数
        double[][] powNum = new double[n][2]; // 每行公共的 x, y 系数次方

        // x
        powNum[0][0] = 2;
        powNum[1][0] = 1;
        powNum[2][0] = 0;
        powNum[3][0] = 1;
        powNum[4][0] = 0;

        // y
        powNum[0][1] = 0;
        powNum[1][1] = 1;
        powNum[2][1] = 2;
        powNum[3][1] = 0;
        powNum[4][1] = 1;

        // 构造矩阵
        for (int j = 0; j < n; j++)
        {	// row
            fact[j] = 0;	// fact init
            for (int k = 0; k < n; k++)
            {	// col
                mEle[j][k] = 0;	//matrix element init
                for (int i = 0; i < pList.size(); i++)
                {	// each point
                    mEle[j][k] += Math.pow(pList.get(i).getX(), powNum[j][0] + powNum[k][0])
                                * Math.pow(pList.get(i).getY(), powNum[j][1] + powNum[k][1]);
                    if (k == 0)
                        fact[j] -= Math.pow(pList.get(i).getX(), powNum[j][0])
                                 * Math.pow(pList.get(i).getY(), powNum[j][1]) * fact[5];
                }
            }
        }

        if (CommonFunction.Gauss(mEle, fact) == false) 	// 高斯消元出现非唯一解
            return false;

        if (calSD(pList, fact) >= standard_deviation) 	//标准差过大，非二次曲线	
            return false;
        return true;
    }
    
    //计算标准差
    private double calSD(List<Point> pList, double[] fact)
    {

        // 对所有点求标准差以分析拟合度
        // 二次曲线中心点为
        // Y = (2ae - bd) / (b^2 - 4ac)
        // X = -d/(2a) - bY/(2a)
        // d = f - (aX^2 + bXY + cY^2)
        // Q(x,y) = a(x-x0)^2 + b(x-x0)(y-y0) + c(y-y0)^2 + d;

        if (fact[1] * fact[1] > 4 * fact[0] * fact[2])	//if (b*b > 4*a*c)
        {
            //此时为双曲线的情况
            return 1;
        }

        int number = pList.size();
        double y0 = (2 * fact[0] * fact[4] - fact[1] * fact[3]) / (fact[1] * fact[1] - 4 * fact[0] * fact[2]);	// ( 2 * a * e - b * d ) / ( b^2 - 4 * a * c )
        double x0 = (-(fact[3] + fact[1] * y0)) / (2 * fact[0]);	// ( -( d + b * y0 ) ) / ( 2 * a )
        double d = fact[5] - (fact[0] * x0 * x0 + fact[1] * x0 * y0 + fact[2] * y0 * y0); 	// f - ( aX0^2 + bX0*Y0 + cY0^2 )
        double t;
        double sum = 0;//标准差

        for (int i = 0; i < number; i++)
        {
            double tx = pList.get(i).getX();
            double ty = pList.get(i).getY();
            // t = a(X-X0)^2 + b(X-X0)(Y-Y0) + c(Y-Y0)^2
            t = fact[0] * (tx - x0) * (tx - x0) +
                fact[1] * (tx - x0) * (ty - y0) +
                fact[2] * (ty - y0) * (ty - y0);
            t /= -d;
            t -= 1;
            sum += Math.signum(t) * t;
        }
        return sum / number;

    }
    
	@Override
	public synchronized boolean isInObject(Point point) {
		// TODO Auto-generated method stub
		//如果曲线不闭合
		if (sweepAngle != 2 * Math.PI) {
			double start = CommonFunction.VectorToAngle(ctlPoint[0],
					center.toPoint());
			double end = CommonFunction.VectorToAngle(
					ctlPoint[ctlPoint.length - 1], center.toPoint());
			double angleOffset = 0;

			double angle1 = CommonFunction.VectorToAngle(point,
					center.toPoint());
			// 如果点在范围外
			if (sweepAngle > 0) {
				// 逆时针 start - end
				if (end < start)
					end += 2 * Math.PI;
				if (angle1 < start)
					angle1 += 2 * Math.PI;
				if (!((angle1 > (start - angleOffset)) && (angle1 < (end + angleOffset)))) {
					return false;
				}
			} else {
				// 顺时针 end - start
				if (end > start)
					end -= 2 * Math.PI;
				if (angle1 > start)
					angle1 -= 2 * Math.PI;
				if (!((angle1 > (end - angleOffset)) && (angle1 < (start + angleOffset)))) {
					return false;
				}
			}
		}
		
		double x = (point.getX() - center.getX()) * Math.cos(rotateAngle)
				+ (point.getY() - center.getY()) * Math.sin(rotateAngle);
		double y = -(point.getX() - center.getX()) * Math.sin(rotateAngle)
				+ (point.getY() - center.getY()) * Math.cos(rotateAngle);
		//相对标准方程的坐标  
		Point p = new Point(x, y);
		Point p1, p2;
		//以x坐标获取一个点
		if (x > a) {
			p1 = new Point(a, 0);
		} else if (x < -a) {
			p1 = new Point(-a, 0);
		} else {
			double temp = Math.sqrt((1-x*x/a/a)*b*b);
			if (y < 0) {
				temp = -temp;
			}
			p1 = new Point(x, temp);
		}
		//以y坐标获取一个点
		if (y > b) {
			p2 = new Point(0, b);
		} else if (y < -b) {
			p2 = new Point(0, -b);
		} else {
			double temp = Math.sqrt((1-y*y/b/b)*a*a);
			if (x < 0) {
				temp = -temp;
			}
			p2 = new Point(temp, y);
		}
		
//		//原曲线起始、终止角
//		double start = startAngle;
//		double end = start + sweepAngle;
//		double angleOffset = 0;
//        
//        double angle1 = CommonFunction.VectorToAngle(p1);
//        double angle2 = CommonFunction.VectorToAngle(p2);
//        //如果两点都点在范围外
//        if (sweepAngle > 0) {
//			//逆时针  start - end
//        	if (angle1 < start) angle1 += 2 * Math.PI;
//        	if (angle2 < start) angle2 += 2 * Math.PI;
//			if (!((angle1 > (start - angleOffset)) && (angle1 < (end + angleOffset)))
//					&& !((angle2 > (start - angleOffset)) && (angle2 < (end + angleOffset)))) {
//				return false;
//			}
//		} else {
//			//顺时针  end - start
//			if (angle1 > start) angle1 -= 2 * Math.PI;
//        	if (angle2 > start) angle2 -= 2 * Math.PI;
//			if (!((angle1 > (end - angleOffset)) && (angle1 < (start + angleOffset)))
//					&& !((angle2 > (end - angleOffset)) && (angle2 < (start + angleOffset)))) {
//				return false;
//			}
//		}
		
		if (CommonFunction.distance(p1, p) < ThresholdProperty.GRAPH_CHECKED_DISTANCE
				|| CommonFunction.distance(p2, p) < ThresholdProperty.GRAPH_CHECKED_DISTANCE) {
			return true;
		}
		
		return false;
	}
	
	public boolean contains(SGObject object) {
		return super.contains(object) || this.center.contains(object);
	}

	public void setAB(double a, double b, UnitChangeArgs e) {
        if (Math.abs(a - this.a) < ThresholdProperty.FLOAT_OFFSET
        		&& Math.abs(b - this.b) < ThresholdProperty.FLOAT_OFFSET)
            return;

        if (e != null && e.constains(this)) return;
        if (e == null) e = new UnitChangeArgs(this, (float)this.a, (float)this.b);
        if (e.isHandled()) return;
        
        this.a = a;
        this.b = b;
        
        OnChange(e);
	}
	public void setAB(double a, double b) {
        this.setAB(a, b, null);
	}
	
	public void setA(double a) {
		this.setAB(a, this.b);
	}
	
	public void setB(double b) {
		this.setAB(this.a, b);
	}
	
	public void setCenter(Point p) {
		this.center.Set(p);
	}
	
	public double getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}

	public double getSweepAngle() {
		return sweepAngle;
	}

	public void setSweepAngle(double sweepAngle) {
		this.sweepAngle = sweepAngle;
	}

	public double getRotateAngle() {
		return rotateAngle;
	}

	public void setRotateAngle(double rotateAngle) {
		this.rotateAngle = rotateAngle;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public PointUnit getCenter() {
		return center;
	}

	@Override
	public void translate(Point vector, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		center.Set(center.getX() + vector.getX(), center.getY() + vector.getY(), e);
	}

	@Override
	public void scale(Point vector, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		a = a * vector.getX();
		b = b * vector.getY();
	}

	@Override
	public void rotate(double rotateAngle, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		this.rotateAngle = this.rotateAngle + rotateAngle;
	}
	
	/**
	 * 判断是否为圆
	 * @return
	 */
	public boolean isCircle() {
        double circleJude = ThresholdProperty.CIRCLE_JUDE;
		double kk = this.a / this.b;
        if (kk > 1.0/circleJude && kk < circleJude) {
        	kk = (this.a + this.b) / 2;
        	this.a = this.b = kk;
        	return true;
        }
		return false;
	}

}
