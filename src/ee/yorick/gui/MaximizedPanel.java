package ee.yorick.gui;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.BorderLayout;

public class MaximizedPanel extends JPanel
{
	private static final long serialVersionUID = -3704320889049075447L;
	private ArrayList<SimpleWeather> days = new ArrayList<SimpleWeather>();
	
	public MaximizedPanel()
	{
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (d.height/1.5f);
		setPreferredSize(new Dimension(width, d.height/3));
		setLayout(new BorderLayout(0, 0));
		JPanel graphPanel = new JPanel();
		add(graphPanel);
		
		setOpaque(false);
		
		JPanel daysPanel = new JPanel();
		daysPanel.setOpaque(false);
		add(daysPanel, BorderLayout.SOUTH);
		daysPanel.setLayout(new GridLayout(1, 0, 0, 0));
		for(int i = 0; i < 7; i++)
		{
			SimpleWeather sw = new SimpleWeather((int) (width/7f));
			daysPanel.add(sw);
			days.add(sw);
		}
	}

}
