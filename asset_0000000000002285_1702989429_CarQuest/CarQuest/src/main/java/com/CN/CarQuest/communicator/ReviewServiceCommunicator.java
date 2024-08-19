package com.CN.CarQuest.communicator;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import com.CN.CarQuest.dto.ReviewRequest;
import com.CN.CarQuest.dto.ReviewResponse;


@Service
public class ReviewServiceCommunicator {

	private final RestTemplate restTemplate;

	@Autowired
	public ReviewServiceCommunicator(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate=restTemplateBuilder.build();
	}
	
	public void addReview(ReviewRequest reviewRequest, String jwtToken) {
		String url ="http://localhost:8081/review/add/";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
	
	  HttpEntity<ReviewRequest> requestEntity = new HttpEntity<>(reviewRequest, headers);

	    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

		
				
	}


	public List<ReviewResponse> getReview(String carName, String jwtToken){
		String url ="http://localhost:8081/review/"+carName;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
	
		HttpEntity<Map<String, Long>> requestEntity = new HttpEntity<>(headers);

	
		ResponseEntity<List<ReviewResponse>> response = restTemplate.exchange(
	            url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<ReviewResponse>>() {});

	    return response.getBody();
	}
}
