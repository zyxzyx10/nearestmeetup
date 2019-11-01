package com.paul.zhang;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class TomTom implements MapAPI {
	@Override
	 public JsonNode findPosition(String address) throws URISyntaxException, IOException {
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


	@Override
	public JsonNode searchAround(String alt, String lon) throws URISyntaxException, IOException {
		URIBuilder uriBuilder = new URIBuilder("https://api.tomtom.com/search/2/nearbySearch/.json");
		uriBuilder.
				setParameter("countrySet", "CA").
				setParameter("limit", "2").
				setParameter("lat", alt).
				setParameter("lon", lon).
				setParameter("radius", MEETUP_SEARCH_RADIUS).
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
		return results.get(0);
	}
}
