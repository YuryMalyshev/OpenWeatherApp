package ee.yorick.gui;

import java.awt.Component;
import java.awt.Frame;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;

public class GraphDrawer extends PApplet
{
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
	
	public void setup()
	{
		noLoop();
	}
	
	private int backgroundColor = 0;
	public void draw()
	{
		System.out.println(backgroundColor);
		background(backgroundColor++);
		if(backgroundColor >= 255)
		{
			backgroundColor = 0;
		}
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
