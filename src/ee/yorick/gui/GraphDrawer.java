package ee.yorick.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PGraphics;

public class GraphDrawer extends PApplet
{
	public static void main(String[] a)
	{
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (d.height/1.5f);
		int height = d.height/3;
		int swWidth = width/7;
		int swHeight = swWidth+swWidth/3;
		
		GraphDrawer.setDesiredSize(width, height-swHeight);
		PApplet.main(GraphDrawer.class);
		Thread t =new Thread() {
			public void run()
			{
				while(true)
				{
					self.redraw();
					try
					{
						Thread.sleep(30);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	
	
	
	
	
	
	
	private static GraphDrawer self;
	
	public static GraphDrawer getInstance()
	{
		return GraphDrawer.self;
	}
	
	private static int width;
	private static int height;
	public static void setDesiredSize(int width, int height)
	{
		GraphDrawer.width = width;
		GraphDrawer.height = height;
	}
	
	public void settings()
	{
		self = this;
		size(GraphDrawer.width, GraphDrawer.height);
	}
	
	private PGraphics axis;
	private PGraphics axisValues;
	private PGraphics values;
	private float timeInterval;
	private float tempInterval;
	
	private float L = 40;
	private float R = width-10;
	private float U = 10;
	private float D = height-30;
	
	private float co = 10;
	
	private int textSize = 10;
	
	public void setup()
	{
		timeInterval = (R-L-co*2)/48;
		tempInterval = (D-U-co*2)/3;
		
		noLoop();
		axis = this.createGraphics(width, height);
		setupAxis();
		axisValues = this.createGraphics(width, height);
		//updateAxis();
		values = this.createGraphics((int)(R-L-2*co+2), (int)(D-U-2*co+2));
	}
	
	public void draw()
	{
		background(0);
		image(axis, 0, 0);
		image(axisValues, 0, 0);
		image(values, L+co, U+co);
	}
	
	
	
	public void setupAxis()
	{
		axis.beginDraw();
		axis.stroke(255);
		axis.strokeWeight(3);
		axis.line(L, D, R, D); //hor
		axis.line(L, U, L, D); //ver
		
		axis.stroke(255);
		axis.strokeWeight(1);
		for(int i = (int)(L+co); i < R; i += timeInterval*4)
		{
			axis.line(i, D-5,i, D+5);
		}
		for(int i = (int)(D-co); i > U; i -= tempInterval)
		{
			axis.line(L-5, i, L+5, i);
		}
		axis.endDraw();
	}
	
	public void updateAxis(ArrayList<WeatherHour> hours)
	{
		axisValues.beginDraw();
		axisValues.textSize(textSize);
		axisValues.fill(255);
		
		float minT = Float.MAX_VALUE;
		float maxT = Float.MIN_VALUE;
		for(WeatherHour wh : hours)
		{
			if(minT > wh.getTempC())
			{
				minT = (float) wh.getTempC();
			}
			if(maxT < wh.getTempC())
			{
				maxT = (float) wh.getTempC();
			}
		}
		float tInt = (maxT-minT)/3;
		
		int index = 0;
		for(int i = (int)(L+co); i < R; i += timeInterval*4)
		{
			axisValues.text(hours.get(index).getTime(), i-timeInterval, height-10);
			index += 4;
			if(index >= hours.size())
			{
				index = hours.size()-1;
			}
		}
		
		float t = minT;
		for(int i = (int)(D-co); i > U; i -= tempInterval)
		{
			axisValues.text(t + "*C", 0, i+textSize/2);
			t += tInt;
		}
		axisValues.endDraw();
	}
	
	
	public void updateValues(ArrayList<WeatherHour> hours)
	{
		float minT = Float.MAX_VALUE;
		float maxT = Float.MIN_VALUE;
		for(WeatherHour wh : hours)
		{
			if(minT > wh.getTempC())
			{
				minT = (float) wh.getTempC();
			}
			if(maxT < wh.getTempC())
			{
				maxT = (float) wh.getTempC();
			}
		}
		float tRange = (maxT-minT);
		float conv = (values.height-2)/tRange;
		 
		values.beginDraw();
		values.background(0, 0, 0);
		values.stroke(255);
		float wStart = 0;
		for(int i = 0; i < hours.size()-2; i++)
		{
			float sPx = (float) ((hours.get(i).getTempC()-minT)*conv)+1;
			float ePx = (float) ((hours.get(i+1).getTempC()-minT)*conv)+1;
			values.line(wStart, values.height-sPx, wStart+timeInterval, values.height-ePx);
			wStart += timeInterval;
		}
		values.endDraw();
	}
	
	private Frame getFrame()
	{
		PSurfaceAWT surf = (PSurfaceAWT) this.getSurface();
		PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) surf.getNative();
		return canvas.getFrame();
	}
	
	public Component getPanel()
	{
		return getFrame().getComponent(0);
	}

	public void disposeOfFrame()
	{
		getFrame().dispose();
	}
}
