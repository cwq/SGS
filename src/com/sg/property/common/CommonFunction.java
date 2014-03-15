package com.sg.property.common;

public class CommonFunction {
	
	/**
	 * 两点间距离
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(Point p1, Point p2) {
		return Math.sqrt((p1.getX() - p2.getX())*(p1.getX() - p2.getX())+
				(p1.getY() - p2.getY())*(p1.getY() - p2.getY()));
	}
	
	/**
	 * curPoint到直线（startpoint，endpoint）的距离
	 * @param startpoint
	 * @param endpoint
	 * @param curPoint
	 * @return curPoint到直线（startpoint，endpoint）的距离
	 */
	public static double pointToLineDistance(Point startpoint,Point endpoint,Point curPoint){
		double x1,x2,y1,y2,curX,curY;
		x1=(double)startpoint.getX();
		x2=(double)endpoint.getX();
		y1=(double)startpoint.getY();
		y2=(double)endpoint.getY();
		curX=(double)curPoint.getX();
		curY=(double)curPoint.getY();
		double distance;
		if(x1 == x2)
			distance = Math.abs(curX - x1);
		else
			distance=Math.abs((y1-y2)/(x1-x2)*curX-curY-(y1-y2)/(x1-x2)*x1+y1)/Math.sqrt(((y1-y2)*(y1-y2))/((x1-x2)*(x1-x2))+1);
		return distance;
	}
	
    public static Point RotatePoint(Point p, double angle, Point center)
    {
        angle = -angle;
        return new Point((float)((p.getX() - center.getX()) * Math.cos(angle) + (p.getY() - center.getY()) * Math.sin(angle) + center.getX()),
        				 (float)((p.getX() - center.getX()) * -Math.sin(angle) + (p.getY() - center.getY()) * Math.cos(angle) + center.getY()));
    }

    public static double VectorToAngle(Point p)
    {
        double tr = Math.atan2(p.getY(), p.getX());
        if (tr < 0) tr += Math.PI * 2;
        return tr;
    }
    public static double VectorToAngle(Point p, Point center)
    {
        return VectorToAngle(new Point(p.getX() - center.getX(), p.getY() - center.getY()));
    }

    public static boolean Gauss(double[][] a, double[] Ans)
    {

        final double eps = ThresholdProperty.FLOAT_OFFSET;
        int i, j, k, n = 5;
        double maxp, t;
        int row = n - 1, col = n - 1;
        int[] index = new int[n];

        for (i = 0; i < n; i++)
            index[i] = i;
        for (k = 0; k < n; k++)
        {
            for (maxp = 0, i = k; i < n; i++)
                for (j = k; j < n; j++)
                    if (Math.abs(a[i][j]) > Math.abs(maxp))
                        maxp = a[row = i][col = j];
            if (Math.abs(maxp) < eps)
                return false;
            if (col != k)
            {
                for (i = 0; i < n; i++)
                {
                    t = a[i][col];
                    a[i][col] = a[i][k];
                    a[i][k] = t;
                }
                j = index[col];
                index[col] = index[k];
                index[k] = j;
            }
            if (row != k)
            {
                for (j = k; j < n; j++)
                {
                    t = a[k][j];
                    a[k][j] = a[row][j];
                    a[row][j] = t;
                }
                t = Ans[k];
                Ans[k] = Ans[row];
                Ans[row] = t;
            }
            for (j = k + 1; j < n; j++)
            {
                a[k][j] /= maxp;
                for (i = k + 1; i < n; i++)
                    a[i][j] -= a[i][k] * a[k][j];
            }
            Ans[k] /= maxp;
            for (i = k + 1; i < n; i++)
                Ans[i] -= Ans[k] * a[i][k];
        }
        for (i = n - 1; i >= 0; i--)
            for (j = i + 1; j < n; j++)
                Ans[i] -= a[i][j] * Ans[j];
        for (k = 0; k < n; k++)
            a[0][index[k]] = Ans[k];
        for (k = 0; k < n; k++)
            Ans[k] = a[0][k];
        return true;
    }
}
