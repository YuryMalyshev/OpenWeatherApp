package ee.yorick.gui;

import javax.swing.JPanel;

import ee.yorick.logic.WeatherDay;
import ee.yorick.logic.WeatherHour;
import processing.core.PApplet;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Component;

public class MaximizedPanel extends JPanel
{
	private static final long serialVersionUID = -3704320889049075447L;
	private ArrayList<SimpleWeather> days = new ArrayList<SimpleWeather>();
	private boolean update = false;
	
	public MaximizedPanel()
	{
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (d.height/1.4f);
		int height = (int) (d.height/2.75);
		int swWidth = width/7;
		int swHeight = swWidth+swWidth/3;
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout(0, 0));
		
		JPanel graphPanel = new JPanel();
		graphPanel.setBorder(null);
		graphPanel.setLayout(new BorderLayout(0, 0));
		
		GraphDrawer.setDesiredSize(width, height-swHeight);
		PApplet.main(GraphDrawer.class);
		Component processing = GraphDrawer.getInstance().getPanel();
		graphPanel.add(processing, BorderLayout.CENTER);
		GraphDrawer.getInstance().disposeOfFrame();
		add(graphPanel);
		
		
		setOpaque(false);
		
		JPanel daysPanel = new JPanel();
		daysPanel.setOpaque(false);
		add(daysPanel, BorderLayout.SOUTH);
		daysPanel.setLayout(new GridLayout(1, 0, 0, 0));
		for(int i = 0; i < 7; i++)
		{
			SimpleWeather sw = new SimpleWeather(swWidth, swHeight);
			daysPanel.add(sw);
			days.add(sw);
		}
		
	}
	
	public void updateDailyWeather(ArrayList<WeatherDay> days)
	{
		if(update)
		{
			for(int i = 1; i < days.size(); i++)
			{
				this.days.get(i-1).updateTemperature(days.get(i).getMin(), days.get(i).getMax());
				this.days.get(i-1).updateWeatherIcon(days.get(i).getIcon());
				this.days.get(i-1).updateDate(days.get(i).getDate());
			}
		}
	}
	
	public void updateHourlyWeather(ArrayList<WeatherHour> hours)
	{
		if(update)
		{
			GraphDrawer.getInstance().updateAxis(hours);
			GraphDrawer.getInstance().updateValues(hours);
			GraphDrawer.getInstance().redraw();
		}
	}
	
	public void enableUpdates(boolean enable)
	{
		update = enable;
	}
}
