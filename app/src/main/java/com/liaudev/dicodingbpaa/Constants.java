package com.liaudev.dicodingbpaa;

/**
 * Created by Budiliauw87 on 2022-05-17.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class Constants {
    public static final String BASE_URL = "https://story-api.dicoding.dev/v1";

    //METHOD POST
    public static final String REGISTER_URL = BASE_URL + "/register";
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String ADD_STORIES = BASE_URL + "/stories";
    public static final String ADD_BY_GUEST = ADD_STORIES + "/guest";

    //METHOD GET
    public static final String GET_STORIES = BASE_URL + "/stories?page=";
    public static final String GET_STORIES_LOCATION = BASE_URL + "/stories?location=";

    //EXTRA INTENT
    public static String EXTRA_STORY = "storyobject";

    //REQUEST PERMISSION CODE
    public static int REQUEST_PERMISSIONS_POSTING_CODE = 777;

}
