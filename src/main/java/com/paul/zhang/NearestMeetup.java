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
import java.text.ParseException;
import java.util.Arrays;

public class NearestMeetup {
	private MapAPI mapAPI = new TomTom();

	private void go(String[] args) throws URISyntaxException, IOException, ParseException {
		{
			if (args == null || args.length <= 0) {
				System.out.println("please provide an address at least");
				System.exit(-1);
			}

			double lats = 0D;
			double lons = 0D;
			for (int i = 0; i < args.length; i++) {
				JsonNode position = mapAPI.findPosition(args[i]);
				lats += position.get("lat").asDouble();
				lons += position.get("lon").asDouble();
			}
			double centralLat = lats / Double.valueOf(args.length);
			double centralLon = lons / Double.valueOf(args.length);

			JsonNode result = mapAPI.searchAround(centralLat + "", centralLon + "");

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
				Desktop.getDesktop().browse(new URI("https://www.google.ca/maps/place/" + URLEncoder.encode(freeformAddress, "UTF-8")));
			}
		}
	}

	public static void main(String[] args) throws URISyntaxException, IOException, ParseException {
		new NearestMeetup().go(args);
	}

}
