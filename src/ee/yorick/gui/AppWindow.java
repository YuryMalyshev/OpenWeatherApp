package ee.yorick.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.awt.event.ActionEvent;

public class AppWindow extends JFrame
{
	private static final long serialVersionUID = 71707730991574964L;
	private static char degC = '\u2103';
	
	private JPanel contentPane;
	private JLabel lblTemp;
	private JLabel lblTime;
	private JLabel lblIcon;
	private WeatherUpdateThread wut;
	

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					AppWindow frame = new AppWindow();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppWindow()
	{
		this.setTitle("Weather App");
		this.setResizable(false);
		wut = new WeatherUpdateThread("");
		WelcomeWindow ww = new WelcomeWindow();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int widht = d.height/3;
		if(widht < 290)
			widht = 290;
		
		int height = d.height/5;
		if(height < 150)
			height = 150;
		setBounds(100, 100, widht, height);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 191, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel weatherPanel = new JPanel();
		weatherPanel.setOpaque(false);
		contentPane.add(weatherPanel, BorderLayout.CENTER);
		weatherPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblTemp = new JLabel("n/a"+degC);
		lblTemp.setFont(new Font("Tahoma", Font.PLAIN, 44));
		lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
		lblTemp.setAlignmentX(Component.CENTER_ALIGNMENT);
		weatherPanel.add(lblTemp);
		
		lblIcon = new JLabel("");
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		weatherPanel.add(lblIcon);
		
		JPanel timePanel = new JPanel();
		timePanel.setOpaque(false);
		contentPane.add(timePanel, BorderLayout.NORTH);
		timePanel.setLayout(new BorderLayout(0, 0));
		
		lblTime = new JLabel("n/a");
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		timePanel.add(lblTime);
		
		JButton btnUpdate = new JButton("\u21BB");
		btnUpdate.setForeground(Color.GREEN);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				wut.updateWeather();
			}
		});
		btnUpdate.setBackground(new Color(0, 0, 255));
		btnUpdate.setFocusable(false);
		timePanel.add(btnUpdate, BorderLayout.EAST);
		
		JButton btnInfo = new JButton("info");
		btnInfo.setForeground(Color.GREEN);
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(ww.showDialog() == WelcomeWindow.OK)
				{
					wut.setAPIkey(ww.getAPIkey());
					wut.updateWeather();
				}
			}
		});
		btnInfo.setBackground(new Color(0, 0, 255));
		btnInfo.setFocusable(false);
		timePanel.add(btnInfo, BorderLayout.WEST);
		setVisible(true);
		
		
		if(ww.showFirstTime() == WelcomeWindow.OK)
		{
			wut.setAPIkey(ww.getAPIkey());
		}
		wut.start();
	}
	
	private class WeatherUpdateThread extends Thread
	{
		private Weather weather;
		
		public WeatherUpdateThread(String APIkey)
		{
			weather = new Weather(APIkey);
			weather.updateLocation();
		}
		
		public void setAPIkey(String APIkey)
		{
			this.weather.setAPIkey(APIkey);
		}
		
		public void run()
		{
			while(true)
			{
				updateWeather();
				LocalTime nextMinute = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute()+1);
				long delay = LocalTime.now().until(nextMinute, ChronoUnit.MILLIS);
				try{Thread.sleep(delay);}catch (InterruptedException e)	{e.printStackTrace(); }
			}
		}
		
		public void updateWeather()
		{
			try
			{
				weather.retrieveLatest();
				WeatherHour current = weather.getCurrent();
				double t = current.getTempC();
				if(t > 0)
				{
					lblTemp.setText("+"+t);
				}
				else if(t < 0)
				{
					lblTemp.setText("-"+t);
				}
				else
				{
					lblTemp.setText(""+t);
				}
				
				lblTime.setText(current.getTime());
				
				try
				{
					URL url = new URL("http://openweathermap.org/img/wn/$icon$@2x.png".replace("$icon$", current.getIcon()));
					Image image = ImageIO.read(url);
					lblIcon.setIcon(new ImageIcon(image));
					AppWindow.this.setIconImage(image);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			catch(RuntimeException e)
			{
				if(e.getMessage().contains("Invalid API key"))
				{
					lblTime.setText("Invalid API key");
				}
				else
				{
					System.err.println(e.getMessage());
					lblTime.setText(e.getMessage());
				}
				lblTemp.setText("n/a"+degC);
			}
		}
	}

}
