package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vf608 on 6/26/17.
 */

public class User implements Parcelable{

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public int tweets;
    public int followers;
    public int following;

    public static User fromJson(JSONObject json) throws JSONException {
        User user = new User();
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url").replace("_normal", "");
        user.tweets = json.getInt("listed_count");
        user.followers = json.getInt("followers_count");
        user.following = json.getInt("friends_count");
        return user;
    }


    public User() {
        // Normal actions performed by class, since this is still a normal object!
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeInt(this.tweets);
        dest.writeInt(this.followers);
        dest.writeInt(this.following);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tweets = in.readInt();
        this.followers = in.readInt();
        this.following = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
