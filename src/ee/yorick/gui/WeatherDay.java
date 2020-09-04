package ee.yorick.gui;

import java.time.LocalDateTime;

public class WeatherDay extends WeatherBasic
{
	private double tempDay = 0;
	private double tempNight = 0;
	private double tempEve = 0;
	private double tempMorn = 0;
	
	public WeatherDay()
	{
		
	}
	
	public WeatherDay(LocalDateTime time, String icon, double tempDay, double tempNight, double tempEve, double tempMorn)
	{
		super(time, icon);
		this.tempDay = tempDay;
		this.tempNight = tempNight;
		this.tempEve = tempEve;
		this.tempMorn = tempMorn;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + " " +  convertToCelsius(tempDay);
	}
}
