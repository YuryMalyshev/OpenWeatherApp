package ee.yorick.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;

public class SimpleWeather extends JPanel
{
	private static final long serialVersionUID = -5579049514651655909L;
	
	private JLabel lblIcon;
	private JLabel lblMinTemp;
	private JLabel lblMaxTemp;
	
	public SimpleWeather(int width)
	{
		setOpaque(false);
		setPreferredSize(new Dimension(width, width+(int)(width/3f)));
		setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		lblIcon = new JLabel("");
		lblIcon.setBorder(new MatteBorder(0, 0, 1, 1, (Color) new Color(0, 0, 0)));
		add(lblIcon, BorderLayout.CENTER);
		
		JPanel tPanel = new JPanel();
		tPanel.setOpaque(false);
		tPanel.setBorder(null);
		add(tPanel, BorderLayout.SOUTH);
		tPanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		lblMinTemp = new JLabel("Min: # " + AppWindow.degC);
		lblMinTemp.setHorizontalAlignment(SwingConstants.CENTER);
		tPanel.add(lblMinTemp);
		
		lblMaxTemp = new JLabel("Max: #" + AppWindow.degC);
		lblMaxTemp.setHorizontalAlignment(SwingConstants.CENTER);
		tPanel.add(lblMaxTemp);

	}
	
	public void updateWeatherIcon(Image image)
	{
		lblIcon.setIcon(new ImageIcon(image));
	}
	
	public void updateTemperature()
	{
		lblMinTemp.setText("Min: # " + AppWindow.degC);
		lblMaxTemp.setText("Max: #" + AppWindow.degC);
	}

}
