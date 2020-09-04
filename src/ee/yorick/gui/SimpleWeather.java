package ee.yorick.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import ee.yorick.AppWindow;

import java.awt.Color;
import java.awt.Dimension;

public class SimpleWeather extends JPanel
{
	private static final long serialVersionUID = -5579049514651655909L;
	
	private JLabel lblIcon;
	private JLabel lblMinTemp;
	private JLabel lblMaxTemp;
	private JLabel lblDay;
	
	public SimpleWeather(int width, int height)
	{
		setOpaque(false);
		setPreferredSize(new Dimension(width, height));
		setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		lblIcon = new JLabel("");
		lblIcon.setBorder(new MatteBorder(0, 0, 1, 1, (Color) new Color(0, 0, 0)));
		add(lblIcon, BorderLayout.CENTER);
		
		JPanel tPanel = new JPanel();
		tPanel.setOpaque(false);
		tPanel.setBorder(null);
		add(tPanel, BorderLayout.SOUTH);
		tPanel.setLayout(new GridLayout(3, 0, 0, 0));
		
		lblMinTemp = new JLabel("Min: # " + AppWindow.degC);
		lblMinTemp.setHorizontalAlignment(SwingConstants.CENTER);
		tPanel.add(lblMinTemp);
		
		lblMaxTemp = new JLabel("Max: #" + AppWindow.degC);
		lblMaxTemp.setHorizontalAlignment(SwingConstants.CENTER);
		tPanel.add(lblMaxTemp);
		
		lblDay = new JLabel("");
		lblDay.setHorizontalAlignment(SwingConstants.CENTER);
		tPanel.add(lblDay);

	}
	
	public void updateWeatherIcon(String icon)
	{
		try
		{
			URL url = new URL("http://openweathermap.org/img/wn/$icon$@2x.png".replace("$icon$", icon));
			Image image = ImageIO.read(url);
			lblIcon.setIcon(new ImageIcon(image));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateTemperature(double min, double max)
	{
		lblMinTemp.setText("Min: "+min + AppWindow.degC);
		lblMaxTemp.setText("Max: "+max + AppWindow.degC);
	}
	
	public void updateDate(String date)
	{
		lblDay.setText(date);
	}

}
