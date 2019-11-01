package com.paul.zhang;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URISyntaxException;

interface MapAPI {
	static String MEETUP_SEARCH_RADIUS = "500";
	JsonNode findPosition(String address) throws URISyntaxException, IOException;

	JsonNode searchAround(String alt, String lon) throws URISyntaxException, IOException;
}
