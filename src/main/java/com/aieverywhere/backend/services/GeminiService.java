package com.aieverywhere.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.aieverywhere.backend.models.Posts;
import com.aieverywhere.backend.models.Relationship;
import com.aieverywhere.backend.models.Responses;
import com.aieverywhere.backend.models.Users;
import com.aieverywhere.backend.models.Users.Role;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class GeminiService {
	@Value("${gemini.key}")
	private String apiKey;

	private RestTemplate restTemplate;
	private PostsServices postsServices;
	private RelationshipServices relationshipServices;
	private UsersServices usersServices;
	private LikesServices likesServices;
	private ResponsesServices responsesServices;

	@Autowired
	public GeminiService(RestTemplate restTemplate, PostsServices postsServices,
			RelationshipServices relationshipServices, UsersServices usersServices, LikesServices likesServices,
			ResponsesServices responsesServices) {
		this.restTemplate = restTemplate;
		this.postsServices = postsServices;
		this.relationshipServices = relationshipServices;
		this.usersServices = usersServices;
		this.likesServices = likesServices;
		this.responsesServices = responsesServices;
	}

	private final String API_URL_TEMPLATE = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=%s";

	// this postId is for look which post is ai responding to
	public String AiRespondPost(Long postId) throws Exception {

		// get all the data to send to gemini
		Posts post = postsServices.getPostByPostId(postId);
		Users realUser = usersServices.getUsersByUsersId(post.getUserId());
		List<Relationship> friendsList = relationshipServices.getAllUserFriendsWithRole(post.getUserId(), Role.Ai);
		Random random = new Random();
		Users aiUser = null;
		String context;
		if (friendsList != null && friendsList.isEmpty()) {
			Long usercount = usersServices.getUsersCount();

			boolean check = true;
			while (check) {
				aiUser = usersServices.getUsersByUsersId(random.nextLong(usercount) + 1);
				if (aiUser.getRole().equals(Role.Ai)) {
					check = false;
				}
			}
			context = "this is a post of " + realUser.getNickName() + " content of post is " + post.getContent()
					+ " post have a post tag " + post.getMoodTag()
					+ " and its mean the feeling of the user when post this post "
					+ " and you are a person that scrolling around on social media and you find this post and your personality is "
					+ aiUser.getPersonality() + " and your emotionlevel is " + aiUser.getEmoLevel()
					+ "please give me some respond with your personality and respond with Traditional Chinese and less emoji"
					+ "and if you like the post put 1 first then respond if you dont like put 0";

		} else {
			Relationship randomFriend = friendsList.get(random.nextInt(friendsList.size()));
			aiUser = usersServices.getUsersByUsersId(randomFriend.getFriendId());
			context = "this is a post of " + realUser.getNickName() + " content of post is " + post.getContent()
					+ " post have a post tag " + post.getMoodTag()
					+ " and its mean the feeling of the user when post this post "
					+ " and you are one of his friends and your personality is " + aiUser.getPersonality()
					+ " and your emotionlevel is " + aiUser.getEmoLevel()
					+ "please give me some respond as a friend with your personality and respond with Traditional Chinese and less emoji"
					+ "and if you like the post put 1 first then respond if you dont like put 0";

		}

		// start to format the string send to gemini
		String apiUrl = String.format(API_URL_TEMPLATE, apiKey);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + apiKey); // Include your API key

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode requestBodyNode = objectMapper.createObjectNode();

		ArrayNode contentsArray = objectMapper.createArrayNode();
		ObjectNode contentNode = objectMapper.createObjectNode();
		ArrayNode partsArray = objectMapper.createArrayNode();

		ObjectNode partNode = objectMapper.createObjectNode();

		partNode.put("text", context); // Only include the text prompt

		partsArray.add(partNode);
		contentNode.set("parts", partsArray);
		contentsArray.add(contentNode);

		requestBodyNode.set("contents", contentsArray);

		String requestBody;

		try {
			requestBody = objectMapper.writeValueAsString(requestBodyNode);
			System.out.println(requestBody);

		} catch (Exception e) {
			throw new RuntimeException("Failed to construct JSON request body", e);
		}

		HttpEntity<String> request = new HttpEntity<>(requestBody);
		System.out.println("Request Body: " + request.toString());

		try {
			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

			String responseBody = response.getBody();
			if (responseBody == null) {
				throw new RuntimeException("Received empty response from API");
			}
			List<String> contentAndLike = extractTextFromPost(responseBody);

			String likeOrNot = contentAndLike.get(1);

			if (likeOrNot.equals("1")) {
				likesServices.addPostLike(postId, aiUser.getUserId());
			}

			Responses respond = new Responses();
			respond.setContent(contentAndLike.get(0));
			respond.setPostId(postId);
			respond.setUserId(aiUser.getUserId());
			responsesServices.createResponse(respond);

			return contentAndLike.get(0);
		} catch (HttpClientErrorException e) {
			// Log the error response for debugging
			System.err.println("Error response: " + e.getResponseBodyAsString());
			throw new RuntimeException("API call failed: " + e.getMessage());
		}
	}

	// how to respond a respond and how to trigger
	// use schedule to do the timing
	// select a post and get post's userid
	// and go get friendId or no friend then random an AI
	// and get the all respond's content and the post content and tell AI to focus
	// on user respond
	//
	// if random an AI then send a friend request to add friend after respond

	public String AiRespondToRespond(Long postId) throws Exception {

		Posts post = postsServices.getPostByPostId(postId);

		Users realUser = usersServices.getUsersByUsersId(post.getUserId());
		List<Relationship> friendsList = relationshipServices.getAllUserFriendsWithRole(post.getUserId(), Role.Ai);
		Random random = new Random();
		Users aiUser = null;
		String context;
		if (friendsList != null && friendsList.isEmpty()) {
			Long usercount = usersServices.getUsersCount();

			boolean check = true;
			while (check) {
				aiUser = usersServices.getUsersByUsersId(random.nextLong(usercount) + 1);
				if (aiUser.getRole().equals(Role.Ai)) {
					check = false;
				}
			}
			List<Responses> postAllRespond = responsesServices.getAllResponsesByPostId(postId);
			List<Users> allRespondUser = new ArrayList<>();
			for (Responses respond : postAllRespond) {
				allRespondUser.add(usersServices.getUsersByUsersId(respond.getUserId()));
			}
			List<Boolean> allUserRelationship = new ArrayList<>();
			for (Users user : allRespondUser) {
				allUserRelationship.add(relationshipServices.checkRelationship(user.getUserId(), aiUser.getUserId()));
			}

			String allRespondUserRelationship = "those are responses of the post ";

			for (int i = 0; i < allRespondUser.size(); i++) {
				String areFriendOrNot;
				if (allUserRelationship.get(i)) {
					areFriendOrNot = " are friend ";
				} else {
					areFriendOrNot = " are not friend ";
				}
				allRespondUserRelationship = allRespondUserRelationship.concat(" the " + (i + 1) + " respond is "
						+ postAllRespond.get(i).getContent() + " by " + allRespondUser.get(i).getNickName()
						+ " you and " + allRespondUser.get(i).getNickName() + areFriendOrNot + "and next respond.");
			}

			context = "this is a post of " + realUser.getNickName() + " content of post is " + post.getContent()
					+ " post have a post tag " + post.getMoodTag()
					+ " and its mean the feeling of the user when post this post and you are a person that scrolling around on social media "
					+ " and there are some respond of the post maybe you had respond before here are the responses "
					+ allRespondUserRelationship + "your name is " + aiUser.getNickName() + " and your personality is "
					+ aiUser.getPersonality() + " and your emotionlevel is " + aiUser.getEmoLevel()
					+ "please give me some respond with your personality and The emphasis is slightly on "
					+ realUser.getNickName() + " and respond with Traditional Chinese and less emoji";

		} else {
			Relationship randomFriend = friendsList.get(random.nextInt(friendsList.size()));
			aiUser = usersServices.getUsersByUsersId(randomFriend.getFriendId());
			List<Responses> postAllRespond = responsesServices.getAllResponsesByPostId(postId);
			List<Users> allRespondUser = new ArrayList<>();
			for (Responses respond : postAllRespond) {
				allRespondUser.add(usersServices.getUsersByUsersId(respond.getUserId()));
			}
			List<Boolean> allUserRelationship = new ArrayList<>();
			for (Users user : allRespondUser) {
				allUserRelationship.add(relationshipServices.checkRelationship(user.getUserId(), aiUser.getUserId()));
			}

			String allRespondUserRelationship = "those are responses of the post ";

			for (int i = 0; i < allRespondUser.size(); i++) {
				String areFriendOrNot;
				if (allUserRelationship.get(i)) {
					areFriendOrNot = " are friend ";
				} else {
					areFriendOrNot = " are not friend ";
				}
				allRespondUserRelationship = allRespondUserRelationship.concat(" the " + (i + 1) + " respond is "
						+ postAllRespond.get(i).getContent() + " by " + allRespondUser.get(i).getNickName()
						+ " you and " + allRespondUser.get(i).getNickName() + areFriendOrNot + "and next respond.");
			}

			context = "this is a post of " + realUser.getNickName() + " content of post is " + post.getContent()
					+ " post have a post tag " + post.getMoodTag()
					+ " and its mean the feeling of the user when post this post and you are " + realUser.getNickName()
					+ "'s friend and there are some respond of the post maybe you had respond before here are the responses "
					+ allRespondUserRelationship + " your name is " + aiUser.getNickName() + " and your personality is "
					+ aiUser.getPersonality() + " and your emotionlevel is " + aiUser.getEmoLevel()
					+ "please give me some respond with your personality and The emphasis is slightly on "
					+ realUser.getNickName() + " and respond with Traditional Chinese and less emoji";

		}
		String apiUrl = String.format(API_URL_TEMPLATE, apiKey);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + apiKey); // Include your API key

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode requestBodyNode = objectMapper.createObjectNode();

		ArrayNode contentsArray = objectMapper.createArrayNode();
		ObjectNode contentNode = objectMapper.createObjectNode();
		ArrayNode partsArray = objectMapper.createArrayNode();

		ObjectNode partNode = objectMapper.createObjectNode();

		partNode.put("text", context); // Only include the text prompt

		partsArray.add(partNode);
		contentNode.set("parts", partsArray);
		contentsArray.add(contentNode);

		requestBodyNode.set("contents", contentsArray);

		String requestBody;

		try {
			requestBody = objectMapper.writeValueAsString(requestBodyNode);
			System.out.println(requestBody);

		} catch (Exception e) {
			throw new RuntimeException("Failed to construct JSON request body", e);
		}

		HttpEntity<String> request = new HttpEntity<>(requestBody);
		System.out.println("Request Body: " + request.toString());

		try {
			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

			String responseBody = response.getBody();
			if (responseBody == null) {
				throw new RuntimeException("Received empty response from API");
			}

			String respondFromGemini = extractTextFromRespond(responseBody);

			Responses respond = new Responses();
			respond.setContent(respondFromGemini);
			respond.setPostId(postId);
			respond.setUserId(aiUser.getUserId());
			responsesServices.createResponse(respond);

			return respondFromGemini;
		} catch (HttpClientErrorException e) {
			// Log the error response for debugging
			System.err.println("Error response: " + e.getResponseBodyAsString());
			throw new RuntimeException("API call failed: " + e.getMessage());
		}
	}

	// is use for extract the respond that gemini return
	public List<String> extractTextFromPost(String responseBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode candidates = rootNode.path("candidates");

			if (candidates.isArray() && candidates.size() > 0) {
				JsonNode contentNode = candidates.get(0).path("content").path("parts").get(0).path("text");
				String respond = contentNode.asText();

				List<String> list = new ArrayList<>();
				list.add(respond.substring(1)); // content
				list.add(respond.substring(0, 1)); // like

				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String extractTextFromRespond(String responseBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode candidates = rootNode.path("candidates");

			if (candidates.isArray() && candidates.size() > 0) {
				JsonNode contentNode = candidates.get(0).path("content").path("parts").get(0).path("text");
				String respond = contentNode.asText();

				return respond;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
