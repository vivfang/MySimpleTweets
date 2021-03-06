package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vf608 on 6/26/17.
 */

public class Tweet implements Parcelable {
    public String body;
    public long uid;
    public User user;
    public String createdAt;
    public boolean favorited;
    public boolean retweeted;
    public String mediaUrl;

    public Tweet() {
        // Normal actions performed by class, since this is still a normal object!
    }
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        JSONObject entities = jsonObject.getJSONObject("entities");
        JSONArray media;
        if(entities != null) {
            media = entities.optJSONArray("media");
            if (media != null)
                tweet.mediaUrl = media.getJSONObject(0).getString("media_url");
        }
        return tweet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createdAt);
        dest.writeByte(this.favorited ? (byte) 1 : (byte) 0);
        dest.writeByte(this.retweeted ? (byte) 1 : (byte) 0);
        dest.writeString(this.mediaUrl);
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.favorited = in.readByte() != 0;
        this.retweeted = in.readByte() != 0;
        this.mediaUrl = in.readString();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
