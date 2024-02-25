package com.mycompany.notebook.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WikiSearchService {

	private static final Logger logger = LoggerFactory.getLogger(WikiSearchService.class);

	@Inject
	public WikiSearchService() {
	}

	public String searchWikiForMoreInformation(String topic) {
		try {
			logger.info("Querying Wikipedia for more information on given topic");

			String encodedTopic = URLEncoder.encode(topic, StandardCharsets.UTF_8);
			String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles="
				+ encodedTopic + "&callback=&utf8=1&formatversion=2&exchars=300&explaintext=1";

			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

			HttpResponse<String> response = client
				.send(request, HttpResponse.BodyHandlers.ofString());

			return extractWikiResult(response.body());

		} catch (Exception e) {
			logger.error("Error querying Wikipedia", e);
			throw new RuntimeException("Error querying Wikipedia", e);
		}

	}

	private String extractWikiResult(String qureyResult) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = qureyResult
				.substring(qureyResult.indexOf("({") + 1, qureyResult.lastIndexOf("})") + 1);

			JsonNode rootNode = mapper.readTree(json);
			String extract = rootNode.path("query").path("pages").get(0).path("extract").asText();
			if (!extract.isEmpty()) {
				return extract;
			}
		} catch (Exception e) {
			logger.error("Error extracting results from Wikipedia search query", e);
		}
		logger.info("No valid results found from Wikipedia search");
		return "";
	}

}
