package com.aieverywhere.backend.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

import com.aieverywhere.backend.models.Images;
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
	private PostServices postServices;
	private RelationshipServices relationshipServices;
	private UsersServices usersServices;
	private LikesServices likesServices;
	private ResponsesServices responsesServices;
	private ImagesServices imagesService;

	@Autowired
	public GeminiService(RestTemplate restTemplate, PostServices postServices,
			RelationshipServices relationshipServices, UsersServices usersServices, LikesServices likesServices,
			ResponsesServices responsesServices, ImagesServices imagesService) {
		this.restTemplate = restTemplate;
		this.postServices = postServices;
		this.relationshipServices = relationshipServices;
		this.usersServices = usersServices;
		this.likesServices = likesServices;
		this.responsesServices = responsesServices;
		this.imagesService = imagesService;
	}

	private final String API_URL_TEMPLATE = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=%s";

	// this postId is for look which post is ai responding to
	public String AiRespondPost(Long postId) throws Exception {

		// get all the data to send to gemini
		Posts post = postServices.getPostByPostId(postId);
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

		Posts post = postServices.getPostByPostId(postId);

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
				allUserRelationship.add(relationshipServices.checkFollowRelationship(user.getUserId(), aiUser.getUserId()));
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
				allUserRelationship.add(relationshipServices.checkFollowRelationship(user.getUserId(), aiUser.getUserId()));
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

	// first get a random ai
	// second get a rondom picture where is not load by user
	// third send the picture discription and ai informatiom to gemini
	// get the respond and fill the post object
	// and save to the post
	// this is for ai to create a post
	public String aiCreatePost() throws Exception {
		// get random ai user
		Random random = new Random();
		Users aiUser = null;
		Long usercount = usersServices.getUsersCount();
		boolean check = true;
		while (check) {
			aiUser = usersServices.getAiUsersByUsersId(random.nextLong(usercount) + 1);
			if (aiUser.getRole().equals(Role.Ai)) {
				check = false;
			}
		}
		// get a rondom picture from db and pick use count is below average
		Long countSum = imagesService.getUseCountSumNotUserUpload();
		Long picSum = imagesService.getPicUseByAi();
		Double averUseCount = (countSum.doubleValue() / picSum.doubleValue());
		Long imageCount = imagesService.getPicCount();
		Images image = null;
		check = true;
		while (check) {
			image = imagesService.findImageById(random.nextLong(imageCount) + 1);
			if (!image.getIsUploadByUser() && image.getUseCount().doubleValue() < averUseCount) {
				check = false;
			}
		}

		String context = "i will give you a description of a image here is the description " + image.getDescription()
				+ " assume you are a user on a soical platform and you are going to use the image to post a post "
				+ " (imagine the image with the description) and your personality is "
				+ aiUser.getPersonality() + " and your emotion level is " + aiUser.getEmoLevel()
				+ "your gender is " + aiUser.getGender()
				+ " your birthday is at " + aiUser.getBirth().toString()
				+ " if today is near your birthday 40% chance that you can respond that is related to your birthday "
				+ " at the start of of the respond give me a word as moodtag that represent your feeling when you post the post "
				+ " and select a number 1~10 to tell other how strong the felling is and then is the context that your are gpoing to post"
				+ "just give me moodtag(a word) and a number and context you gonna post ,Please follow this specification exactly "
				+ " like this \"happy 5 what a beautiful day! maybe be a bird will be happier!\""
				+ "please dant have * or other kinf of symbol";

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
			System.out.println(respondFromGemini);
			String[] words = respondFromGemini.split(" ");
			String result = Arrays.stream(words).skip(2).collect(Collectors.joining(" "));
			System.out.println(result);
			Posts post = new Posts();
			post.setUserId(aiUser.getUserId());
			post.setContent(result);
			post.setImageId(image.getImageId());
			post.setLikes(0L);
			post.setLocation("paradise");
			post.setMoodScore(Long.parseLong(respondFromGemini.split(" ")[1]));
			post.setMoodTag(respondFromGemini.split(" ")[0]);
			// postServices.createPost(post, null);

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
