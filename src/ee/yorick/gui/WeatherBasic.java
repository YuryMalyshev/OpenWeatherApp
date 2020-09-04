package ee.yorick.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherBasic
{
	private LocalDateTime time;
	private String icon;
	
	public WeatherBasic()
	{
		
	}
	
	public WeatherBasic(LocalDateTime time, String icon)
	{
		super();
		this.time = time;
		this.icon = icon;
	}
	
	public String getTime()
	{
		return time.format(DateTimeFormatter.ofPattern("HH:mm"));
	}
	
	public String getDate()
	{
		return time.format(DateTimeFormatter.ofPattern("dd/MM"));
	}
	
	public String getIcon()
	{
		return icon;
	}
	
	@Override
	public String toString()
	{
		return time.toString();
	}
	
	public double convertToCelsius(double Kelvin)
	{
		return round(Kelvin-273.15f);
	}
	
	private double round(double val)
	{
		return Math.round(val*1000)/1000;
	}
}
