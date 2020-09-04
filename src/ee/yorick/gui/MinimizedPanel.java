package ee.yorick.gui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class MinimizedPanel extends JPanel
{
	private static final long serialVersionUID = 1864432645464316357L;
	
	private JLabel lblTemp;
	private JLabel lblIcon;
	
	private boolean update = true;
	
	public MinimizedPanel(WeatherUpdateThread wut)
	{
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (d.height/3.14f);
		if(width < 290)
			width = 290;
		
		int height = d.height/8;
		if(height < 100)
			height = 100;
		
		System.out.println(width + " "  +height);
		
		setPreferredSize(new Dimension(width, height));
		
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		JPanel weatherPanel = new JPanel();
		add(weatherPanel, BorderLayout.CENTER);
		weatherPanel.setOpaque(false);
		weatherPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblTemp = new JLabel("n/a"+AppWindow.degC);
		lblTemp.setFont(new Font("Tahoma", Font.PLAIN, 44));
		lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
		lblTemp.setAlignmentX(Component.CENTER_ALIGNMENT);
		weatherPanel.add(lblTemp);
		
		lblIcon = new JLabel("");
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		weatherPanel.add(lblIcon);
		
		JPanel extendPanel = new JPanel();
		add(extendPanel, BorderLayout.SOUTH);
		extendPanel.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) extendPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.RIGHT);
	}
	
	public void enableUpdates(boolean enable)
	{
		update = enable;
	}
	
	public void updateTemp(double t)
	{
		if(update)
		{
			if (t > 0)
			{
				lblTemp.setText("+" + t);
			}
			else if (t < 0)
			{
				lblTemp.setText("-" + t);
			}
			else
			{
				lblTemp.setText("" + t);
			}
		}
	}

	public void updateWeatherIcon(Image image)
	{
		if(update)
		{
			lblIcon.setIcon(new ImageIcon(image));
		}
	}

}
