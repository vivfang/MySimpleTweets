package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vf608 on 6/26/17.
 */

public class Tweet {
    public String body;
    public long uid;
    public User user;
    public String createdAt;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }
    
}
