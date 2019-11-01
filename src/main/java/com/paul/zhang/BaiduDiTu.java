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

public class BaiduDiTu implements MapAPI {
	@Override
	public JsonNode findPosition(String address) throws URISyntaxException, IOException {
		URIBuilder uriBuilder = new URIBuilder("http://api.map.baidu.com/place_abroad/v1/search");
		uriBuilder.setParameter("query", address).
				setParameter("scope", "2").
				setParameter("page_size", "10").
				setParameter("page_num", "0").
				setParameter("region", "加拿大").
				setParameter("output", "json").
				setParameter("ak", "2Gcu2DaqqrXzkUjU3kMucNd3PWjU2hdz");

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

		URIBuilder uriBuilder = new URIBuilder("http://api.map.baidu.com/place_abroad/v1/search");
		uriBuilder.setParameter("query", "餐厅$咖啡").
				setParameter("tag", "美食").
				setParameter("location", alt + "," + lon).
				setParameter("radius", MEETUP_SEARCH_RADIUS).
				setParameter("output", "json").
				setParameter("ak", "2Gcu2DaqqrXzkUjU3kMucNd3PWjU2hdz");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(uriBuilder.toString());
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(responseString);

		return root;
	}
}
