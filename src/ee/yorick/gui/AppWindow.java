package ee.yorick.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Image;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.awt.event.ActionEvent;

public class AppWindow extends JFrame
{
	private static final long serialVersionUID = 71707730991574964L;
	public static final char degC = '\u2103';
	
	private JPanel contentPane;
	private MinimizedPanel minp;
	private MaximizedPanel maxp;
	private WeatherUpdateThread wut;
	private JPanel weatherPanel;
	private JPanel extendPanel;
	

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
	
	boolean isMin = true;
	private JLabel lblTime;
	
	/**
	 * Create the frame.
	 */
	public AppWindow()
	{
		this.setTitle("Weather App");
		this.setResizable(false);
		wut = new WeatherUpdateThread("", this);
		WelcomeWindow ww = new WelcomeWindow();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 100, 100);
		contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setBackground(new Color(0, 191, 255));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel timePanel = new JPanel();
		getContentPane().add(timePanel, BorderLayout.NORTH);
		timePanel.setOpaque(false);
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
		btnUpdate.setBackground(Color.BLUE);
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
		btnInfo.setBackground(Color.BLUE);
		btnInfo.setFocusable(false);
		timePanel.add(btnInfo, BorderLayout.WEST);
		
		
		
		weatherPanel = new JPanel();
		weatherPanel.setOpaque(false);
		contentPane.add(weatherPanel);
		weatherPanel.setLayout(new BorderLayout(0, 0));
		
		minp = new MinimizedPanel(wut);
		weatherPanel.add(minp);
		
		
		
		
		
		
		
		
		
		
		
		maxp = new MaximizedPanel();
		//weatherPanel.add(maxp, "max");

		extendPanel = new JPanel();
		extendPanel.setOpaque(false);
		FlowLayout fl_extendPanel = (FlowLayout) extendPanel.getLayout();
		fl_extendPanel.setAlignment(FlowLayout.RIGHT);
		fl_extendPanel.setVgap(0);
		fl_extendPanel.setHgap(0);
		contentPane.add(extendPanel, BorderLayout.SOUTH);

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		JButton btnExtend = new JButton("\u2198");
		btnExtend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(isMin)
				{
					weatherPanel.remove(minp);
					minp.enableUpdates(false);
					weatherPanel.add(maxp);
					maxp.enableUpdates(true);
					btnExtend.setText("\u2196");
					pack();
				}
				else
				{
					weatherPanel.remove(maxp);
					maxp.enableUpdates(false);
					weatherPanel.add(minp);
					minp.enableUpdates(true);
					btnExtend.setText("\u2198");
					pack();
				}
				isMin = !isMin;
			}
		});
		extendPanel.add(btnExtend);
		btnExtend.setFocusable(false);
		btnExtend.setForeground(Color.GREEN);
		btnExtend.setBackground(Color.BLUE);
		setVisible(true);
		pack();
		
		if(ww.showFirstTime() == WelcomeWindow.OK)
		{
			wut.setAPIkey(ww.getAPIkey());
		}
		Timer timer = new Timer(); 
      timer.schedule(wut, 0, 60*1000); 
	}
	
	public void updateTemp(double t)
	{
		minp.updateTemp(t);
	}

	public void updateTime(String time)
	{
		lblTime.setText(time);
	}

	public void updateWeatherIcon(Image image)
	{
		minp.updateWeatherIcon(image);
		AppWindow.this.setIconImage(image);
	}
	
	public void updateDailyWeather(ArrayList<WeatherDay> days)
	{
		maxp.updateDailyWeather(days);
	}
	
	public void updateHourlyWeather(ArrayList<WeatherHour> hours)
	{
		
	}

}
