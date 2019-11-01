package com.paul.zhang;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.transformation.SortedList;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Arrays;

public class NearestMeetup {
	public static void main(String[] args) throws URISyntaxException, IOException {
		if (args == null || args.length <= 0) {
			System.out.println("please provide an address at least");
			System.exit(-1);
		}

		NearestMeetup nearestMeetup = new NearestMeetup();
		double lats = 0D;
		double lons = 0D;
		for (int i = 0; i < args.length; i++) {
			JsonNode position = nearestMeetup.findPosition(args[i]);
			lats += position.get("lat").asDouble();
			lons += position.get("lon").asDouble();
		}
		double centralLat = lats / Double.valueOf(args.length);
		double centralLon = lons / Double.valueOf(args.length);

		URIBuilder uriBuilder = new URIBuilder("https://api.tomtom.com/search/2/nearbySearch/.json");
		uriBuilder.
				setParameter("countrySet", "CA").
				setParameter("limit", "2").
				setParameter("lat", centralLat + "").
				setParameter("lon", centralLon + "").
				setParameter("radius", "500").
				setParameter("language", "en-US").
				setParameter("idxSet", "POI").
				setParameter("categorySet", "7315,9376,9937,9379").
				setParameter("openingHours", "nextSevenDays").
				setParameter("key", "vYcwID4jUHcnrY6A0OLzbl1D6SBuYrOw");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(uriBuilder.toString());
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(responseString);
		JsonNode results = root.get("results");
		JsonNode result = results.get(0);
		JsonNode poi = result.get("poi");
		String name = poi.get("name").asText();
		String phone = poi.get("phone").asText();
		JsonNode address = result.get("address");
		String freeformAddress = address.get("freeformAddress").asText();

		JsonNode position = result.get("position");
		String lat = position.get("lat").asText();
		String lon = position.get("lon").asText();

		System.out.println("name:" + name);
		System.out.println("phone:" + phone);
		System.out.println("freeformAddress:" + freeformAddress);
		System.out.println("lat:" + lat);
		System.out.println("lon:" + lon);

		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(new URI("https://www.google.ca/maps/place/"+URLEncoder.encode(freeformAddress, "UTF-8")));
		}
	}

	private JsonNode findPosition(String address) throws URISyntaxException, IOException {
		URIBuilder uriBuilder = new URIBuilder("https://api.tomtom.com/search/2/geocode/" + URLEncoder.encode(address, "UTF-8") + ".json");
		uriBuilder.setParameter("countrySet", "CA").
				setParameter("limit", "1").setParameter("countrySet", "CA").
				setParameter("lat", "49.158512").setParameter("lon", "-122.779539")
				.setParameter("radius", "500000").setParameter("key", "vYcwID4jUHcnrY6A0OLzbl1D6SBuYrOw");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(uriBuilder.toString());
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(responseString);
		JsonNode results = root.get("results");
		JsonNode result = results.get(0);
		if (result == null) {
			System.out.println("no address found for address:" + address);
			System.exit(-1);
		}
		return result.get("position");
	}
}
