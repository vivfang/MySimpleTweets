package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

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

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(body);
        out.writeLong(uid);
        out.writeString(createdAt);
    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private Tweet(Parcel in) {
        body = in.readString();
        uid = in.readLong();
        createdAt = in.readString();
    }

    public Tweet() {
        // Normal actions performed by class, since this is still a normal object!
    }
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

}
