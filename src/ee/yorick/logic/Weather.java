package ee.yorick.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Weather
{
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	private WeatherHour current = new WeatherHour();
	private ArrayList<WeatherHour> hours = new ArrayList<WeatherHour>();
	private ArrayList<WeatherDay> days = new ArrayList<WeatherDay>();
	private String lat = "0.0";
	private String lon = "0.0";
	private String APIkey = "";
	
	public Weather(String APIkey)
	{
		this.APIkey = APIkey;
	}
	
	public void setAPIkey(String APIkey)
	{
		this.APIkey = APIkey;
	}
	
	public void updateLocation()
	{
		try
		{
			// get IP
			String ip = getAnswer(openConnection("http://checkip.amazonaws.com/"));
			String geoJSON = getAnswer(openConnection("https://ipwhois.app/json/$ip$".replace("$ip$", ip)));
			JsonNode geoInfo = objectMapper.readValue(geoJSON, JsonNode.class);
			lat = geoInfo.get("latitude").asText();
			lon = geoInfo.get("longitude").asText();
		}
		catch (IOException | RuntimeException e)
		{
			e.printStackTrace();
		}
	}
	
	public void retrieveLatest() throws RuntimeException
	{
		//retrieve data
		final String baseUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat$&lon=$lon$&exclude=minutely&appid=$apikey$";
		String url = baseUrl.replace("$lat$", lat).replace("$lon$", lon).replace("$apikey$", APIkey);
		try
		{
			String weatherJSON = getAnswer(openConnection(url));
			JsonNode weather = objectMapper.readValue(weatherJSON, JsonNode.class);
			// clear
			hours.clear();
			days.clear();
			// parse
			JsonNode current = weather.get("current");
			JsonNode hourly = weather.get("hourly");
			JsonNode daily = weather.get("daily");
			int timezone_offset = weather.get("timezone_offset").asInt();
			LocalDateTime dt;
			double temp;
			String icon;
			// current
			dt = LocalDateTime.ofEpochSecond(current.get("dt").asLong(), 0, ZoneOffset.ofTotalSeconds(timezone_offset));
			temp = current.get("temp").asDouble();
			icon = current.get("weather").get(0).get("icon").asText();
			this.current = new WeatherHour(dt, icon, temp);
			// hourly
			for(JsonNode hour : hourly)
			{
				dt = LocalDateTime.ofEpochSecond(hour.get("dt").asLong(), 0, ZoneOffset.ofTotalSeconds(timezone_offset));
				temp = hour.get("temp").asDouble();
				icon = hour.get("weather").get(0).get("icon").asText();
				this.hours.add(new WeatherHour(dt, icon, temp));
			}
			// daily
			for(JsonNode day : daily)
			{
				dt = LocalDateTime.ofEpochSecond(day.get("dt").asLong(), 0, ZoneOffset.ofTotalSeconds(timezone_offset));
				temp = day.get("temp").get("day").asDouble();
				double night = day.get("temp").get("night").asDouble();
				double eve = day.get("temp").get("eve").asDouble();
				double morn = day.get("temp").get("morn").asDouble();
				icon = day.get("weather").get(0).get("icon").asText();
				this.days.add(new WeatherDay(dt, icon, temp, night, eve, morn));
			}			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private HttpURLConnection openConnection(String sUrl) throws IOException
	{
		URL url = new URL(sUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("GET");
		return conn;
		
	}
	
	private String getAnswer(HttpURLConnection conn) throws IOException, RuntimeException
	{
		// if error
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			try
			{
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
				String line;
				String fullResponse = "";
				while ((line = br.readLine()) != null)
				{
					fullResponse += line;
				}
				throw new RuntimeException("{" + conn.getResponseCode() + " | " + fullResponse + "}") ;
			}
			catch (NullPointerException e)
			{
				throw new RuntimeException("" + conn.getResponseCode());
			}
		}
		// otherwise continue
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String line;
		String fullResponse = "";
		while ((line = br.readLine()) != null)
		{
			fullResponse += line;
		}
		conn.disconnect();
		return fullResponse;
	}

	public WeatherHour getCurrent()
	{
		return current;
	}
	
	public ArrayList<WeatherHour> getHourly()
	{
		return hours;
	}
	
	public ArrayList<WeatherDay> getDaily()
	{
		return days;
	}
}
