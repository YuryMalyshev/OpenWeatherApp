package ee.yorick.logic;

import java.time.LocalDateTime;

public class WeatherHour extends WeatherBasic
{
	private double temp = 0;

	public WeatherHour()
	{
		
	}
	
	public WeatherHour(LocalDateTime time, String icon, double temp)
	{
		super(time, icon);
		this.temp = temp;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + " " + convertToCelsius(temp);
	}

	public double getTempC()
	{
		return convertToCelsius(temp);
	}
}
