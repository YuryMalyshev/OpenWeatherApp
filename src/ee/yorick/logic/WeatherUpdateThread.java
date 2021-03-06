package ee.yorick.logic;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import ee.yorick.AppWindow;

public class WeatherUpdateThread extends TimerTask
{
	private Weather weather;
	private AppWindow frame;

	public WeatherUpdateThread(String APIkey, AppWindow frame)
	{
		this.frame = frame;
		weather = new Weather(APIkey);
		weather.updateLocation();
	}

	public void setAPIkey(String APIkey)
	{
		this.weather.setAPIkey(APIkey);
	}

	public void run()
	{
		updateWeather();
	}
	
	public void updateSoon()
	{
		Thread worker = new Thread() {
			public void run()
			{
				updateWeather();
			}
		};
		worker.start();
	}

	private void updateWeather()
	{
		try
		{
			weather.retrieveLatest();
			WeatherHour current = weather.getCurrent();
			frame.updateTemp(current.getTempC());
			frame.updateTime(current.getTime());

			try
			{
				URL url = new URL("http://openweathermap.org/img/wn/$icon$@2x.png".replace("$icon$", current.getIcon()));
				Image image = ImageIO.read(url);
				frame.updateWeatherIcon(image);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			frame.updateHourlyWeather(weather.getHourly());
			frame.updateDailyWeather(weather.getDaily());
		}
		catch (RuntimeException e)
		{
			if (e.getMessage().contains("Invalid API key"))
			{
				frame.updateTime("Invalid API key");
			}
			else
			{
				System.err.println(e.getMessage());
				frame.updateTime(e.getMessage());
			}
			frame.updateTemp(Double.NaN);
		}
	}
}