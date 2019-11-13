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

public class Yelp implements MapAPI {
	@Override
	public JsonNode findPosition(String address) throws URISyntaxException, IOException {
		URIBuilder uriBuilder = new URIBuilder("https://api.tomtom.com/search/2/geocode/" + URLEncoder.encode(address, "UTF-8") + ".json");
		uriBuilder.
				setParameter("countrySet", "CA").
				setParameter("limit", "1").
				setParameter("countrySet", "CA").
				setParameter("lat", "49.158512").
				setParameter("lon", "-122.779539")
				.setParameter("radius", "500000").
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
		if (result == null) {
			System.out.println("no address found for address:" + address);
			System.exit(-1);
		}
		return result.get("position");
	}

	@Override
	public JsonNode searchAround(String alt, String lon) throws URISyntaxException, IOException {

		URIBuilder uriBuilder = new URIBuilder("https://api.yelp.com/v3/businesses/search");
		uriBuilder.
				setParameter("latitude", alt).
				setParameter("longitude", lon).
				setParameter("radius", "40000").
				setParameter("locale", "en_CA").
				setParameter("limit", "3");
//				setParameter("price", "1,2").
//				setParameter("open_now", "true").
//		    setParameter("open_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse("2019-11-1 22:30:00 PT").getTime() + "").
//		    setParameter("categories","Food,Restaurants")

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(uriBuilder.toString());
		httpget.addHeader("Bearer API_KEY", "omKlh09yTSBn74cvUHpaVdE9LEaEBT-jUURyBUy_zkYJ1vQlZtBGx18kJ0lho_6NMPOX1I8tkXxzmBbFZ9BRR8VWlOlC0ciJMCQH4VQdJaZfPjInByjgIGIFkJ28XXYx");
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(responseString);

		return root;
	}
}
